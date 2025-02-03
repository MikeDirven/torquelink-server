package nl.torquelink.shared.routing

import io.ktor.resources.*

object TorqueLinkRouting {
    @Resource("auth")
    class Auth

    @Resource("api")
    class Api {
        @Resource("V1")
        class V1(
            val parent: Api = Api()
        )
    }
}