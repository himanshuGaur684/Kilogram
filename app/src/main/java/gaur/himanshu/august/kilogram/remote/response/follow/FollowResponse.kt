package gaur.himanshu.august.kilogram.remote.response.follow

data class FollowResponse(
    val follow: List<Follow>,
    val msg: String,
    val success: Boolean
)