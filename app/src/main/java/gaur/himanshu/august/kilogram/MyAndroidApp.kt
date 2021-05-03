package gaur.himanshu.august.kilogram

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyAndroidApp : Application() {

    companion object {
        const val CHANNEL_ID = "notification_channel"
        const val CHAT_CHANNEL_ID="notification_chat"
    }

    private lateinit var instance: MyAndroidApp

    override fun onCreate() {
        super.onCreate()
        instance = this



    }


}