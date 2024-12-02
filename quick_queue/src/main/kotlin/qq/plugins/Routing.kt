package qq.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import qq.AdminServices

fun Application.configureRouting() {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
    }

    var admin: AdminServices = AdminServices();

    routing {
        post("/login") {
            val formData = call.receiveParameters()
            require(formData["name"] != null) { "Name field is missing" }
            val name = formData["name"] ?: ""

            admin.add(name)
            call.respondText("Form submitted successfully!")
        }
        post("/add_window") {
            val formData = call.receiveParameters()
            require(formData["name"] != null) { "Name field is missing" }
            val name = formData["name"] ?: ""

            admin.add(name)
            call.respondText("Form submitted successfully!")
        }
        post("/delete_window") {
            val formData = call.receiveParameters()
            require(formData["wid"] != null) { "Wid field is missing" }
            val wid : Int = formData["wid"]?.toInt() ?: 0

            admin.delete(wid)
            call.respondText("Form submitted successfully!")
        }
    }

}
