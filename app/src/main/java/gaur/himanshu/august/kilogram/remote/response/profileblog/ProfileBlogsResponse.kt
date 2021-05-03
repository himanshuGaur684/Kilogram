package gaur.himanshu.august.kilogram.remote.response.profileblog

data class ProfileBlogsResponse(
    val blog: Blog,
    val msg: String,
    val success: Boolean
)