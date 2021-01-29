package app

import com.microsoft.azure.functions.*
import com.microsoft.azure.functions.annotation.*
import java.util.*


/**
 * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
 * 1. curl -d "HTTP Body" {your host}/api/HttpExample
 * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
 */
@FunctionName("/greet")
fun run(
  @HttpTrigger(name = "req",
    methods = [HttpMethod.GET, HttpMethod.POST],
    authLevel = AuthorizationLevel.ANONYMOUS) request: HttpRequestMessage<Optional<String?>>,
  context: ExecutionContext,
): HttpResponseMessage? {
  context.logger.info("Java HTTP trigger processed a request.")
  
  // Parse query parameter
  val query = request.queryParameters["name"]
  val name: String? = request.body.orElse(query)
  return if (name == null) {
    request.createResponseBuilder(HttpStatus.BAD_REQUEST)
      .body("Please pass a name on the query string or in the request body").build()
  } else {
    request.createResponseBuilder(HttpStatus.OK).body("Hello, $name").build()
  }
}
