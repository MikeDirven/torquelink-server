package nl.torquelink.domain.environment

object Environment {
    val profileAvatarStorage by lazy {
        System.getenv("PROFILE_AVATAR_STORAGE")
            ?: throw RuntimeException("Missing environment variable: PROFILE_AVATAR_STORAGE")
    }

    val profileCarPhotoStorage by lazy {
        System.getenv("PROFILE_CAR_PHOTO_STORAGE")
           ?: throw RuntimeException("Missing environment variable: PROFILE_CAR_PHOTO_STORAGE")
    }
}