package app.service

expect class GreetService {
  suspend fun greet(name: String? = null): String
}
