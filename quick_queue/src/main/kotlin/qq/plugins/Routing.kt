package qq.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
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
        allowHeader("token")
    }

    var admin: AdminServices = AdminServices();

    routing {

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

        get("/userInfo") {
            val formData = call.request.headers
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

        get("/dashboardIndicators") {
            val formData = call.request.queryParameters
            val token = formData["token"] ?: ""
            var staffInfo = admin.get_staff_info_by_token(token)

            if (staffInfo != null) {
                var data = admin.get_dashboard_stat()
                call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
            }
            else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        get("/showDoctypes") {
            val formData = call.request.queryParameters
            val token = formData["token"] ?: ""
            var staffInfo = admin.get_staff_info_by_token(token)

            if (staffInfo != null) {
                var data = admin.get_doctypes_list()
                call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
            }
            else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        get("/deleteDoctype") {
            val formData = call.request.queryParameters
            val token = formData["token"] ?: ""
            val id = formData["id"] ?: ""
            var staffInfo = admin.get_staff_info_by_token(token)

            if (staffInfo != null) {
                var data = admin.delete_doctype(id)
                if (data) {
                    call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
                }
                else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }
            else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        post("/hideDoctype") {
            val formData = call.receiveParameters()
            require(formData["token"] != null && formData["id"] != null) { "Не все поля формы заполнены" }
            val token = formData["token"] ?: ""
            val id = formData["id"] ?: ""
            var staffInfo = admin.get_staff_info_by_token(token)

            if (staffInfo != null) {
                var data = admin.change_doctype_stat(id)
                if (data) {
                    call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
                }
                else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            }
            else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }


        post("/addDoctype") {
            val formData = call.receiveParameters()
            require(formData["token"] != null && formData["label"] != null) { "Не все поля формы заполнены" }

            val token = formData["token"] ?: ""
            val label = formData["label"]
            val description = formData["description"]
            var staffInfo = admin.get_staff_info_by_token(token)

            if (staffInfo != null) {
                var data = admin.add_new_doctype(label, description)
                if (data) {
                    call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
                }
                else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
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

        get("/check_health"){
            call.respond(HttpStatusCode.OK, "OK")
        }
    }

}
