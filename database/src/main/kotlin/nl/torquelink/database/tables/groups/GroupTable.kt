package nl.torquelink.database.tables.groups

import nl.torquelink.database.interfaces.CoreTable
import nl.torquelink.shared.enums.groups.MemberListVisibility
import org.jetbrains.exposed.sql.Column

object GroupTable : CoreTable("TL_D_Groups") {
    override val active: Column<Boolean> = bool("active")

    val groupName = varchar("groupName", 100)
    val description = varchar("description", 255)

    // Data
    val logoUrl = largeText("logoUrl")
    val coverPhotoUrl = largeText("coverPhotoUrl")

    // members
    val memberCount = integer("memberCount").default(0)
    val adminCount = integer("adminCount").default(0)

    // settings
    val privateGroup = bool("privateGroup").default(false)
    val joinRequestsEnabled = bool("joinRequestsEnabled").default(false)
    val memberListVisibility = enumeration<MemberListVisibility>("memberListVisibility")

    // socials
    val facebookUrl = varchar("facebookUrl", 150)
    val twitterUrl = varchar("twitterUrl", 150)
    val instagramUrl = varchar("instagramUrl", 150)
    val linkedInUrl = varchar("linkedInUrl", 150)
    val websiteUrl = varchar("websiteUrl", 255)
}