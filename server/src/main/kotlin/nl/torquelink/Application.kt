package nl.torquelink

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.torquelink.config.*
import nl.torquelink.domain.environment.Environment
import nl.torquelink.routing.groups.configureGroupsRouting
import nl.torquelink.routing.users.configureUsersRouting
import java.io.File

fun main(args: Array<String>) {
    embeddedServer(CIO, port = Environment.port) {
        // Configure plugins
        install(Resources)
        configureSerialization()
        configureMonitoring()
        configureExceptions()
        configureHTTP()
        configureAuthentication()
        configureSwagger()

        // Configure routing
        configureUsersRouting()
        configureGroupsRouting()

        routing {
            get("/.well-known/assetlinks.json") {
                call.respondFile(File("C:\\Users\\Mike\\Desktop\\Torque Link\\server\\torquelink\\assetlinks.json"))
            }

            get("test") {
                call.respondFile(File("C:\\Users\\Mike\\Desktop\\Torque Link\\server\\torquelink\\test.html"))
            }
            get("assets/text_logo") {
                call.respondFile(File("C:\\Users\\Mike\\Desktop\\Torque Link\\text_logo.png"))
            }
        }

//    routing {
//        get("/api/{registrationNumber}") {
//            val registrationNumber = call.parameters["registrationNumber"]?: return@get call.respondText("Registration number is required", status = HttpStatusCode.BadRequest)
//
//            val data = OpenDataApi.vehicleData.getVehicleData("ZB696L")
//
//            when (data) {
//                is OpenDataResult.OpenDataSuccessResult<List<OpenDataVehicle>> -> {
//                    call.respond(data.status, data.data)
//                }
//                is OpenDataResult.OpenDataFailureResult<List<OpenDataVehicle>> -> {
//                    call.respond(data.status, data.error)
//                }
//                else -> {
//                    call.respond(HttpStatusCode.InternalServerError)
//                }
//            }
//        }
//    }
    }.start(wait = true)
}