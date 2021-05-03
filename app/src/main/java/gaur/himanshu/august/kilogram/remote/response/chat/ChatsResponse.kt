package gaur.himanshu.august.kilogram.remote.response.chat

data class ChatsResponse(
    val msg: String,
    val results: Results,
    val success: Boolean
)