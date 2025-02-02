package nl.torquelink.routing

import io.github.smiley4.ktorswaggerui.dsl.routes.OpenApiRoute
import io.github.smiley4.ktorswaggerui.dsl.routing.post
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import nl.torquelink.constants.AUTHENTICATION_TAG
import nl.torquelink.database.TorqueLinkDatabase
import nl.torquelink.database.dao.IdentityDao
import nl.torquelink.database.dao.UserProfileDao
import nl.torquelink.database.tables.UserProfileTable
import nl.torquelink.services.AuthenticationService
import nl.torquelink.shared.models.auth.AuthenticationResponses
import nl.torquelink.shared.models.auth.LoginRequests

fun postLoginRouteDoc(ref: OpenApiRoute) = ref.apply {
    tags = setOf(AUTHENTICATION_TAG)
    description = "Login to torque link."
    request {
        body<LoginRequests>()
    }
    response {
        HttpStatusCode.OK to {
            body<AuthenticationResponses>()
        }
        HttpStatusCode.Unauthorized to {
            body<String>()
        }
    }
}

fun Routing.postLoginRoute() {
    post("auth/login", ::postLoginRouteDoc) {
        val request = call.receive<LoginRequests>()
        val responseObject : Pair<IdentityDao, AuthenticationResponses> = when(request) {
            is LoginRequests.UsernameLoginRequest -> {
                AuthenticationService().loginByUsername(
                    request.username,
                    request.password,
                    request.remember == true
                )
            }
            is LoginRequests.EmailLoginRequest -> {
                AuthenticationService().loginByEmail(
                    request.email,
                    request.password,
                    request.remember == true
                )
            }
            is LoginRequests.RememberTokenLoginRequest -> {
                AuthenticationService().loginByRememberToken(
                    request.rememberToken
                )
            }
        }

        // Try to find user profile
        val userProfile = TorqueLinkDatabase.executeAsync {
            UserProfileDao.find {
                UserProfileTable.identity eq  responseObject.first.id
            }.singleOrNull()
        }

        userProfile?.let { userProfileDao ->
            when(responseObject.second) {
                is AuthenticationResponses.AuthenticationResponseWithRemember -> {
                    call.respond(
                        AuthenticationResponses.AuthenticationResponseWithRememberAndProfile(
                            responseObject.second.accessToken,
                            responseObject.second.refreshToken,
                            (responseObject.second as AuthenticationResponses.AuthenticationResponseWithRemember).rememberToken,
                            userProfileDao.toResponseWithSettings()
                        )
                    )
                }
                else -> {
                    AuthenticationResponses.AuthenticationResponseWithProfile(
                        responseObject.second.accessToken,
                        responseObject.second.refreshToken,
                        userProfileDao.toResponseWithSettings()
                    )
                }
            }
        } ?: call.respond(HttpStatusCode.OK, responseObject.second)
    }
}