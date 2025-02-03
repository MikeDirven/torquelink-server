package nl.torquelink

import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.resources.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import nl.torquelink.config.*
import nl.torquelink.nl.torquelink.routing.users.configureUsersRouting

fun main(args: Array<String>) {
    EngineMain.main(args)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun Application.module() {
    // Configure plugins
    install(Resources)
    configureSerialization()
    configureDatabases()
    configureMonitoring()
    configureHTTP()
    configureAuthentication()
    configureSwagger()

    // Configure routing
    configureUsersRouting()

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
}