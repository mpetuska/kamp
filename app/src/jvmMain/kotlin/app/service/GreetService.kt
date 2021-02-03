package app.service

actual class GreetService {
  actual suspend fun greet(name: String?): String = "Hi, ${name ?: "Anon!"}"
}
