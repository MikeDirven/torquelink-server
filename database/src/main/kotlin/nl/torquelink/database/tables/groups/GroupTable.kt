package nl.torquelink.database.tables.groups

import nl.torquelink.database.interfaces.CoreTable
import nl.torquelink.shared.enums.group.MemberListVisibility
import org.jetbrains.exposed.sql.Column

object GroupTable : CoreTable("TL_D_Groups") {
    override val active: Column<Boolean> = bool("active")

    val groupName = varchar("groupName", 100).index()
    val description = varchar("description", 255).nullable()

    // Data
    val logoUrl = largeText("logoUrl").nullable()
    val coverPhotoUrl = largeText("coverPhotoUrl").nullable()

    // settings
    val privateGroup = bool("privateGroup").default(false)
    val joinRequestsEnabled = bool("joinRequestsEnabled").default(false)
    val memberListVisibility = enumeration<MemberListVisibility>("memberListVisibility")

    // socials
    val facebookUrl = varchar("facebookUrl", 150).nullable()
    val twitterUrl = varchar("twitterUrl", 150).nullable()
    val instagramUrl = varchar("instagramUrl", 150).nullable()
    val linkedInUrl = varchar("linkedInUrl", 150).nullable()
    val websiteUrl = varchar("websiteUrl", 255).nullable()
}