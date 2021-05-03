package gaur.himanshu.august.kilogram.local.ui.mainapp.chat.model


class Message(
    val username: String,
    val user_id:String,
    val isSent: Boolean = false,
    var message: String = "",
) {
}