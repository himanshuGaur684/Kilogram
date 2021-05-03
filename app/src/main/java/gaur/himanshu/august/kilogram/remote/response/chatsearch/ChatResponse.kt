package gaur.himanshu.august.kilogram.remote.response.chatsearch

data class ChatResponse(
    val chat_users: ChatUsers,
    val msg: String,
    val success: Boolean
)