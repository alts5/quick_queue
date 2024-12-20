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
import qq.SystemServices
import qq.UserServices

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

    var admin: AdminServices = AdminServices()
    var user: UserServices = UserServices()
    var system: SystemServices = SystemServices()

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
            } else {
                call.respond(HttpStatusCode.Unauthorized, message = "Неверный логин или пароль")
            }
        }

        get("/userInfo") {
            val formData = call.request.headers
            val token = formData["token"] ?: ""

            var data = admin.get_staff_info_by_token(token)
            if (data != null) {
                var data = "{\"name\" : \"${data["name"]}\", \"is_admin\" : \"${data["is_admin"]}\"}"
                call.respondText(data, ContentType.Application.Json, HttpStatusCode.OK)
            } else {
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
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        /* ----  Типы документов ----*/
        get("/showDoctypes") {
            val formData = call.request.queryParameters
            val token = formData["token"] ?: ""
            var staffInfo = admin.get_staff_info_by_token(token)

            if (staffInfo != null) {
                var data = admin.get_doctypes_list()
                call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        get("/deleteDoctypes") {
            val formData = call.request.queryParameters
            val token = formData["token"] ?: ""
            val id = formData["id"] ?: ""
            var staffInfo = admin.get_staff_info_by_token(token)

            if (staffInfo != null) {
                var data = admin.delete_doctype(id)
                if (data) {
                    call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        post("/hideDoctypes") {
            val formData = call.receiveParameters()
            require(formData["token"] != null && formData["id"] != null) { "Не все поля формы заполнены" }
            val token = formData["token"] ?: ""
            val id = formData["id"] ?: ""
            var staffInfo = admin.get_staff_info_by_token(token)

            if (staffInfo != null) {
                var data = admin.change_doctype_stat(id)
                if (data) {
                    call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }


        post("/addDoctypes") {
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
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        post("/addConnect") {
            val formData = call.receiveParameters()
            require(formData["token"] != null && formData["service"] != null && formData["category"] != null) { "Не все поля формы заполнены" }

            val token = formData["token"] ?: ""
            val serv = formData["service"]
            val categ = formData["category"]
            var staffInfo = admin.get_staff_info_by_token(token)

            if (staffInfo != null) {
                var data = admin.add_new_connect(serv, categ)
                if (data) {
                    call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        /* ---- Категории заявителей ----*/

        get("/showCategories") {
            val formData = call.request.queryParameters
            val token = formData["token"] ?: ""
            var staffInfo = admin.get_staff_info_by_token(token)

            if (staffInfo != null) {
                var data = admin.get_categories_list()
                call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        get("/deleteCategories") {
            val formData = call.request.queryParameters
            val token = formData["token"] ?: ""
            val id = formData["id"] ?: ""
            var staffInfo = admin.get_staff_info_by_token(token)

            if (staffInfo != null) {
                var data = admin.delete_category(id)
                if (data) {
                    call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        post("/hideCategories") {
            val formData = call.receiveParameters()
            require(formData["token"] != null && formData["id"] != null) { "Не все поля формы заполнены" }
            val token = formData["token"] ?: ""
            val id = formData["id"] ?: ""
            var staffInfo = admin.get_staff_info_by_token(token)

            if (staffInfo != null) {
                var data = admin.change_category_stat(id)
                if (data) {
                    call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }


        post("/addCategories") {
            val formData = call.receiveParameters()
            require(formData["token"] != null && formData["label"] != null) { "Не все поля формы заполнены" }

            val token = formData["token"] ?: ""
            val name = formData["label"]
            val description = formData["description"]
            var staffInfo = admin.get_staff_info_by_token(token)

            if (staffInfo != null) {
                var data = admin.add_new_category(name, description)
                if (data) {
                    call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        /* ---- Сервисы ----*/

        get("/showServices") {
            val formData = call.request.queryParameters
            val token = formData["token"] ?: ""
            var staffInfo = admin.get_staff_info_by_token(token)

            if (staffInfo != null) {
                var data = admin.get_services_list()
                call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        get("/deleteServices") {
            val formData = call.request.queryParameters
            val token = formData["token"] ?: ""
            val id = formData["id"] ?: ""
            var staffInfo = admin.get_staff_info_by_token(token)

            if (staffInfo != null) {
                var data = admin.delete_service(id)
                if (data) {
                    call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        post("/hideServices") {
            val formData = call.receiveParameters()
            require(formData["token"] != null && formData["id"] != null) { "Не все поля формы заполнены" }
            val token = formData["token"] ?: ""
            val id = formData["id"] ?: ""
            var staffInfo = admin.get_staff_info_by_token(token)

            if (staffInfo != null) {
                var data = admin.change_service_stat(id)
                if (data) {
                    call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }


        post("/addServices") {
            val formData = call.receiveParameters()
            require(formData["token"] != null && formData["label"] != null) { "Не все поля формы заполнены" }

            val token = formData["token"] ?: ""
            val name = formData["label"]
            val description = formData["description"]
            var staffInfo = admin.get_staff_info_by_token(token)

            if (staffInfo != null) {
                var data = admin.add_new_service(name, description)
                if (data) {
                    call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }


        /* ---- Окна ----*/

        get("/showWindows") {
            val formData = call.request.queryParameters
            val token = formData["token"] ?: ""
            var staffInfo = admin.get_staff_info_by_token(token)

            if (staffInfo != null) {
                var data = admin.get_windows_list()
                call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        get("/deleteWindows") {
            val formData = call.request.queryParameters
            val token = formData["token"] ?: ""
            val id = formData["id"] ?: ""
            var staffInfo = admin.get_staff_info_by_token(token)

            if (staffInfo != null) {
                var data = admin.delete_window(id)
                if (data) {
                    call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        post("/hideWindows") {
            val formData = call.receiveParameters()
            require(formData["token"] != null && formData["id"] != null) { "Не все поля формы заполнены" }
            val token = formData["token"] ?: ""
            val id = formData["id"] ?: ""
            var staffInfo = admin.get_staff_info_by_token(token)

            if (staffInfo != null) {
                var data = admin.change_window_stat(id)
                if (data) {
                    call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }


        post("/addWindows") {
            val formData = call.receiveParameters()
            require(formData["token"] != null && formData["label"] != null) { "Не все поля формы заполнены" }

            val token = formData["token"] ?: ""
            val name = formData["label"]
            var staffInfo = admin.get_staff_info_by_token(token)

            if (staffInfo != null) {
                var data = admin.add_new_window(name)
                if (data) {
                    call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }


        /* ---- Окна ----*/

        get("/showStaff") {
            val formData = call.request.queryParameters
            val token = formData["token"] ?: ""
            var staffInfo = admin.get_staff_info_by_token(token)

            if (staffInfo != null) {
                var data = admin.get_staff_list()
                call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }



        get("/deleteStaff") {
            val formData = call.request.queryParameters
            val token = formData["token"] ?: ""
            val id = formData["id"] ?: ""
            var staffInfo = admin.get_staff_info_by_token(token)

            if (staffInfo != null) {
                var data = admin.delete_staff(id)
                if (data) {
                    call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        post("/hideStaff") {
            val formData = call.receiveParameters()
            require(formData["token"] != null && formData["id"] != null) { "Не все поля формы заполнены" }
            val token = formData["token"] ?: ""
            val id = formData["id"] ?: ""
            var staffInfo = admin.get_staff_info_by_token(token)

            if (staffInfo != null) {
                var data = admin.change_staff_stat(id)
                if (data) {
                    call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        post("/addStaff") {
            val formData = call.receiveParameters()
            require(formData["token"] != null && formData["name"] != null && formData["login"] != null) { "Не все поля формы заполнены" }

            val token = formData["token"] ?: ""
            val name = formData["name"]
            val login = formData["login"]
            var staffInfo = admin.get_staff_info_by_token(token)

            if (staffInfo != null) {
                var data = admin.add_new_staff(name, login)
                if (data) {
                    call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
                } else {
                    call.respond(HttpStatusCode.InternalServerError)
                }
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        get("/systemMode") {
            var data = system.getSysMode()
            println(data)
            call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
        }
        get("/system_settings"){
            call.respondText(Json.encodeToString(system.getSys()), ContentType.Application.Json, HttpStatusCode.OK )
        }
        get("/check_health") {
            call.respond(HttpStatusCode.OK, "OK")
        }
        get("/get_mode"){
            call.respond(HttpStatusCode.OK,user.getMode("systemMode"))
        }
        get("/get_services") {
                var data = user.get_visible_services_list()
                call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
        }

        get("/121queue") {
            val data = mapOf("hash" to user.simpleApplicant())
            call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
        }
        get("/121ticket") {
            val headers = call.request.headers
            val hash = headers["hash"]

            if (hash == null || hash.isEmpty()) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }
            val i = user.get121Ticket(hash)
            if (i != null) {
                val data = mapOf("items_before" to i)
                call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
            }
        }
        get("/queue") {
            call.respondText(Json.encodeToString(user.getQueue()), ContentType.Application.Json, HttpStatusCode.OK)
        }
        get("/queueAdmin") {
            if (system.getSysMode() == "multi") {
                call.respondText(
                    Json.encodeToString(system.getQueue()),
                    ContentType.Application.Json,
                    HttpStatusCode.OK
                )
            }
            else {
                call.respondText(
                    Json.encodeToString(system.getQueueSingle()),
                    ContentType.Application.Json,
                    HttpStatusCode.OK
                )
            }
        }
        get("/change_status_in_queue") {
            val formData = call.request.queryParameters
            call.respondText(Json.encodeToString(admin.set_app_status(formData["person"], formData["stat"], formData["win"])), ContentType.Application.Json, HttpStatusCode.OK)
        }

        post("/updateSettings") {
            val formData = call.receiveParameters()
            require(formData["token"] != null) { "Не все поля формы заполнены" }

            val token = formData["token"] ?: ""
            val systemMode = formData["systemMode"] ?: ""
            val startTime = formData["startTime"] ?: ""
            val endTime = formData["endTime"]  ?: ""
            val footerName = formData["footerName"] ?: ""
            val logoPath = formData["logoPath"] ?: ""

            var staffInfo = admin.get_staff_info_by_token(token)

            if (staffInfo != null) {
                var data = system.update_settings(systemMode, startTime, endTime,footerName, logoPath)
                call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        get("/getListCategories") {
            val formData = call.request.queryParameters
            val token = formData["token"] ?: ""
            var staffInfo = admin.get_staff_info_by_token(token)

            if (staffInfo != null) {
                var data = admin.get_categories_list()
                call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }
        get("/getListServices") {
            val formData = call.request.queryParameters
            val token = formData["token"] ?: ""
            var staffInfo = admin.get_staff_info_by_token(token)

            if (staffInfo != null) {
                var data = admin.get_services_list()
                call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

        get("/showStaff") {
            val formData = call.request.queryParameters
            val token = formData["token"] ?: ""
            var staffInfo = admin.get_staff_info_by_token(token)

            if (staffInfo != null) {
                var data = admin.get_staff_list()
                call.respondText(Json.encodeToString(data), ContentType.Application.Json, HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.Unauthorized)
            }
        }

    }


}
