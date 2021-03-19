package app.config

import app.service.*
import app.util.*
import com.azure.core.credential.*
import com.azure.search.documents.*
import io.ktor.application.*
import kamp.domain.*
import org.kodein.di.*
import org.kodein.di.ktor.*
import org.litote.kmongo.coroutine.*
import org.litote.kmongo.reactivestreams.*

fun Application.diConfig() = di {
  import(services)
}

private val services by DIModule {
  bind<CoroutineClient>() with singleton { KMongo.createClient(PrivateEnv.MONGO_STRING).coroutine }
  bind<CoroutineCollection<KotlinMPPLibrary>>() with singleton { instance<CoroutineClient>().getDatabase(PrivateEnv.MONGO_DATABASE).getCollection("libraries") }
  
  bind() from singleton { SearchClientBuilder().endpoint(PrivateEnv.SEARCH_SERVICE_URL).credential(AzureKeyCredential(PrivateEnv.SEARCH_SERVICE_KEY)).buildAsyncClient() }
  
  bind<LibraryService>() with scoped(CallScope).singleton { LibraryService(context, instance()) }
}
