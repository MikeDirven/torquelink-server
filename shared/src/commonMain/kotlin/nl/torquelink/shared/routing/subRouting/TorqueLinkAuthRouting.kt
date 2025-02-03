package nl.torquelink.shared.routing.subRouting

import io.ktor.resources.*

interface TorqueLinkAuthRouting {
    @Resource("login")
    class Login

    @Resource("register")
    class Register

    @Resource("refresh")
    class Refresh
}