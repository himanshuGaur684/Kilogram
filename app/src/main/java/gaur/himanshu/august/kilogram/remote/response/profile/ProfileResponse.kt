package gaur.himanshu.august.kilogram.remote.response.profile

data class ProfileResponse(
    val msg: String,
    val success: Boolean,
    val followed_by_me:Boolean,
    val user: User
)