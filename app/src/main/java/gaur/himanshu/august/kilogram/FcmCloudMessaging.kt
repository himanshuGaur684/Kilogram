package gaur.himanshu.august.kilogram

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import gaur.himanshu.august.kilogram.local.room.kilogram.KilogramDao
import gaur.himanshu.august.kilogram.remote.response.chat.Chat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FcmCloudMessaging : FirebaseMessagingService() {


    @Inject
    lateinit var kilogramDao: KilogramDao


    override fun onNewToken(token: String) {
        val pref = this.applicationContext.getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE)
        pref.edit().putString(Constants.FCM_TOKEN, token).apply()
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (message.data.toString().contains("unique_key")) {
            sendNotification(message)
            handleChatEvent(message)
        } else {
            sendNotification(message)
        }
    }

    private fun handleChatEvent(message: RemoteMessage) {
        val chat = extractChat(message)
        sendChatNotification(chat.sending_username, chat.message)
        CoroutineScope(IO).launch {
            kilogramDao.insertSingleChat(chat)
        }
    }


    private fun sendNotification(message: RemoteMessage) {

        val data = message.data
        val notification = NotificationCompat.Builder(this, MyAndroidApp.CHANNEL_ID)
        notification.setAutoCancel(true)
        notification.setSmallIcon(R.drawable.ic_back)
        if(message.data.toString().contains("unique_key")){
            notification.setContentTitle(data["sending_username"]+" send you a message")
            notification.setContentText("message: "+data["message"])
        }else{
            notification.setContentTitle(data["username"])
            notification.setContentText(data["message"])
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(
                    MyAndroidApp.CHANNEL_ID,
                    "noti",
                    NotificationManager.IMPORTANCE_HIGH
                )
            notificationManager.createNotificationChannel(notificationChannel)
            notificationManager.notify(0, notification.build())
        } else {

        }

        // notificationManager.notify(0, notification.build())


    }


    private fun sendChatNotification(title: String, subTitle: String) {


        val notification = NotificationCompat.Builder(this, MyAndroidApp.CHAT_CHANNEL_ID)
        notification.setAutoCancel(true)
        notification.setSmallIcon(R.drawable.ic_back)
        notification.setContentTitle(title)
        notification.setContentText(subTitle)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(
                    MyAndroidApp.CHAT_CHANNEL_ID,
                    "chat_notification",
                    NotificationManager.IMPORTANCE_HIGH
                )
            notificationManager.createNotificationChannel(notificationChannel)
            notificationManager.notify(1, notification.build())
        } else {

        }

        // notificationManager.notify(0, notification.build())
    }


    private fun extractChat(message: RemoteMessage): Chat {
        val data = message.data
        val chat = Chat(
            __v = 0,
            _id = data["_id"].toString(),
            date = data["date"].toString(),
            message = data["message"].toString(),
            receiving_user = data["receiving_user"].toString(),
            sending_username = data["sending_username"].toString(),
            sending_user = data["sending_user"].toString(),
            unique_key = data["unique_key"].toString(),
            isSent = false
        )
        return chat

    }

}