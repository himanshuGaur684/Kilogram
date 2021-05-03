package gaur.himanshu.august.kilogram.remote.response.comments

data class CommentsResponse(
    val comment: List<Comment>,
    val msg: String,
    val success: Boolean
)