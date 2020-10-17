package scanner.util

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import org.slf4j.*
import java.io.*
import kotlin.reflect.*
import org.slf4j.Logger as SLF4JLogger

class Logger(klass: KClass<*>) {
  private val logger = LoggerFactory.getLogger(klass.qualifiedName)
  
  private suspend inline fun log(
    crossinline logFn: SLF4JLogger.(String) -> Unit,
    noinline msg: suspend () -> String,
  ) {
    sendAction {
      val txt = msg()
      logger.logFn(txt)
    }
  }
  
  suspend fun debug(msg: suspend () -> String) = log({ debug(it) }, msg)
  
  suspend fun info(msg: suspend () -> String) = log({ info(it) }, msg)
  
  suspend fun warn(msg: suspend () -> String) = log({ warn(it) }, msg)
  
  suspend fun error(msg: suspend () -> String) = log({ error(it) }, msg)
  
  companion object : Closeable {
    private val context = newSingleThreadContext("logger")
    private val actionChannel = Channel<suspend () -> Unit>(1000)
    
    init {
      GlobalScope.launch(context) {
        for (action in actionChannel) {
          errorSafe {
            action()
          }
        }
      }
    }
    
    fun writeToFile(file: File, log: suspend () -> String) = runBlocking {
      sendAction {
        val txt = log()
        with(file) {
          parentFile.mkdirs()
          writeText(txt)
        }
      }
    }
    
    private suspend inline fun sendAction(noinline action: suspend () -> Unit) {
      actionChannel.send(action)
    }
    
    override fun close() {
      runBlocking {
        actionChannel.close()
        context.close()
      }
    }
  }
}
