package qq.plugins

import io.ktor.http.*
import io.ktor.http.content.*
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
        /*
        post("/authenticate") {
            val formData = call.receiveParameters()
            require(formData["user_login"] != null && formData["user_password"] != null) { "Не указаны логин и пароль" }
            val login = formData["user_login"] ?: ""
            val password = formData["user_password"] ?: ""

            var token_return = admin.check_user_creds(login, password)
            if (token_return != null) {
                var token = "{\"token\" : \"${token_return}\"}"
                call.respondText(token, ContentType.Application.Json, HttpStatusCode.OK)
            }
            else {
                call.respond(HttpStatusCode.Unauthorized, message = "Неверный логин или пароль")
            }
        }
        post("/userInfo") {
            val formData = call.receiveParameters()
            require(formData["token"] != null ) { "Не указан token" }
            val token = formData["token"] ?: ""

            var data = admin.get_staff_info_by_token(token)
            if (data != null) {
                var data = "{\"name\" : \"${data["name"]}\"}"
                call.respondText(data, ContentType.Application.Json, HttpStatusCode.OK)
            }
            else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        post("/add_window") {
            val formData = call.receiveParameters()
            require(formData["name"] != null) { "Name field is missing" }
            val name = formData["name"] ?: ""

            admin.add_window(name)
            call.respondText("Form submitted successfully!")
        }
        post("/delete_window") {
            val formData = call.receiveParameters()
            require(formData["wid"] != null) { "Wid field is missing" }
            val wid : Int = formData["wid"]?.toInt() ?: 0

            admin.delete_window(wid)
            call.respondText("Form submitted successfully!")

        }
        */
        get("/check_health"){
            call.respond(HttpStatusCode.OK, "OK")
        }
    }

}
