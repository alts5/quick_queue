package qq

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import qq.plugins.*


fun main() {
    val docs = DocumentTypesService()
    print(docs.get_all_document_types().toString())

    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureSerialization()
    configureSecurity()
    configureHTTP()
    configureSockets()
    configureRouting()
}
