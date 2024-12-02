package qq

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import qq.plugins.configureRouting


fun main() {
    embeddedServer(Netty, port = 8080) {
        configureRouting()
    }.start(wait = true)

    val docs = DocumentTypesDAO()
    for (elem in docs.get_all_document_types()) {
        println(elem)
    }
}

