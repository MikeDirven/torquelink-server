package nl.torquelink.shared.routing.subRouting

import io.ktor.resources.*
import nl.torquelink.shared.routing.TorqueLinkRouting

interface TorqueLinkAuthRouting {
    @Resource("login")
    class Login(
        val parent: TorqueLinkRouting.Auth = TorqueLinkRouting.Auth()
    )

    @Resource("register")
    class Register(
        val parent: TorqueLinkRouting.Auth = TorqueLinkRouting.Auth()
    )

    @Resource("refresh")
    class Refresh(
        val parent: TorqueLinkRouting.Auth = TorqueLinkRouting.Auth()
    )

    @Resource("password")
    class Password(
        val parent: TorqueLinkRouting.Auth = TorqueLinkRouting.Auth()
    ) {
        @Resource("Reset")
        class Reset(
            val parent: Password = Password()
        )
    }

    @Resource("notifications")
    class Notifications(
        val parent: TorqueLinkRouting.Auth = TorqueLinkRouting.Auth()
    ) {
        @Resource("token")
        class Token(
            val parent: Notifications = Notifications()
        )
    }

    @Resource("email")
    class Email(
        val parent: TorqueLinkRouting.Auth = TorqueLinkRouting.Auth()
    ) {
        @Resource("verify")
        class Verify(
            val parent: Email = Email(),
            val verification: String
        )
    }
}