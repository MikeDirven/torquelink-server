package nl.torquelink.domain.environment

object Environment {
    val port by lazy {
        System.getenv("PORT")?.toInt()
            ?: throw RuntimeException("Missing environment variable: PORT")
    }

    val profileAvatarStorage by lazy {
        System.getenv("PROFILE_AVATAR_STORAGE")
            ?: throw RuntimeException("Missing environment variable: PROFILE_AVATAR_STORAGE")
    }

    val profileCarPhotoStorage by lazy {
        System.getenv("PROFILE_CAR_PHOTO_STORAGE")
           ?: throw RuntimeException("Missing environment variable: PROFILE_CAR_PHOTO_STORAGE")
    }

    val databaseUrl by lazy {
        System.getenv("DB_URL")
            ?: throw RuntimeException("Missing environment variable: DB_URL")
    }

    val databaseUser by lazy {
        System.getenv("DB_USER")
            ?: throw RuntimeException("Missing environment variable: DB_USER")
    }

    val databasePass by lazy {
        System.getenv("DB_PASS")
            ?: throw RuntimeException("Missing environment variable: DB_PASS")
    }
}