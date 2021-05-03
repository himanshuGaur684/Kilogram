package gaur.himanshu.august.kilogram.remote.response.notification

data class NotificationResponse(
    val msg: String,
    val notifications: Notifications,
    val success: Boolean
)