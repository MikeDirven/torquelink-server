package nl.torquelink.shared.routing.subRouting

import io.ktor.resources.*

interface TorqueLinkAuthRouting {
    @Resource("login")
    class Login

    @Resource("register")
    class Register

    @Resource("refresh")
    class Refresh

    @Resource("password")
    class Password {
        @Resource("Reset")
        class Reset(
            val parent: Password = Password()
        )
    }

    @Resource("email")
    class Email {
        @Resource("verify")
        class Verify(
            val parent: Email = Email(),
            val verification: String
        )
    }
}