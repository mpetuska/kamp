package dev.petuska.kamp.core.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Alternative to runBlocking which cancels the inner coroutine on SIGINT.
 * This is useful for kubernetes, openshift which uses
 *
 * If the inner coroutine is `NonCancellable` time-out after the specified [timeout].
 * This means potentially a finalizer cannot finish running, in that case you should increase [timeout].
 * The default value is the same as Kubernetes, which sends `KILLSIG` 30s after `SIGTERM`,
 * if you increase the Kubernetes grace period you should also increase this [timeout]!
 *
 * fun main(): Unit = cancelOnShutdown(timeout = 30.seconds) {
 *   kafkaConsumer(settings(..))
 *     .subscribeTo("MyTopic")
 *      // Storing a record can take up to 10s
 *     .map { record -> persist(record) }
 *     .collect(::println)
 * }
 *
 * If persisting the record can take up to ~10s,
 * then with `runBlocking` and `SIGINT`/`SIGTERM` the JVM would shutdown while our persist function is running.
 * With a timeout of 30 seconds our program gets the opportunity to cancel the `kafkaConsumer` `Flow`,
 * and gracefully exit the program shutting down all resources properly.
 */
fun cancelOnShutdown(
  ctx: CoroutineContext = EmptyCoroutineContext,
  @Suppress("UNUSED_PARAMETER") timeout: Duration = 30.seconds,
  f: suspend CoroutineScope.() -> Unit
): Unit = runBlocking(ctx) {
//  val job = launch(start = CoroutineStart.LAZY, block = f)
//  val isShutdown = AtomicBoolean(false)
//  val hook = Thread({
//    isShutdown.set(true)
//    // We use a CountDownLatch to back-pressure JVM exit
//    val latch = CountDownLatch(1)
//    suspend { job.cancelAndJoin() }
//      .startCoroutineUninterceptedOrReturn(Continuation(EmptyCoroutineContext) {
//        latch.countDown()
//      })
//    latch.await(timeout.inWholeMilliseconds, TimeUnit.MILLISECONDS)
//  }, "Shutdown hook")
//  Runtime.getRuntime().addShutdownHook(hook)
//
//  job.start()
//  job.join()
//
//  if (!isShutdown.getAndSet(true)) {
//    Runtime.getRuntime().removeShutdownHook(hook)
//  }
  runBlocking(ctx, f)
}
