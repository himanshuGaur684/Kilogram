package gaur.himanshu.august.kilogram.local.ui.mainapp.chat.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import gaur.himanshu.august.kilogram.Constants
import gaur.himanshu.august.kilogram.MyAndroidApp
import gaur.himanshu.august.kilogram.R
import gaur.himanshu.august.kilogram.local.room.kilogram.KilogramDao
import gaur.himanshu.august.kilogram.local.room.kilogram.RemoteDao
import gaur.himanshu.august.kilogram.local.ui.mainapp.chat.model.Message
import gaur.himanshu.august.kilogram.remote.response.chat.Chat
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@AndroidEntryPoint
class ChatService : Service() {

    companion object {
        val list = MutableLiveData<Message>()
        val chat = MutableLiveData<Chat>()
        val chats = MutableLiveData<Chat>()
        var checkIsForeground: Boolean = false
    }



    @Inject
    lateinit var kilogramDao: KilogramDao

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var remoteDao: RemoteDao

    val mBinder = MyBinder()
    lateinit var socket: Socket


    val echo = Emitter.Listener { args ->
        val obj = args[0]
        CoroutineScope(IO).launch {

            val chat = Gson().fromJson(obj.toString(), Chat::class.java)
            chat.isSent = true
            chats.postValue(chat)
            kilogramDao.insertSingleChat(chat)
        }
    }

    val listener = Emitter.Listener { args ->
        val obj1 = args[0].toString()
        val obj = Gson().fromJson(obj1, Chat::class.java)
        obj.isSent = false
        CoroutineScope(IO).launch {
            kilogramDao.insertSingleChat(obj)
        }
        chats.postValue(obj)
        if (!checkIsForeground) {
            sendNotification(obj)
        }

    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        val pref = sharedPreferences.getString(Constants.USER_ID, "")
        Log.d("TAG", "onDestroy: ${pref}")
        socket.emit("disconnected", pref)
      //  checkIsForeground = false
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val userId = sharedPreferences.getString(Constants.USER_ID, "").toString()
        socket = io.socket.client.IO.socket(Constants.BASE_URL)
        socket.connect()

        socket.let {
            it.connect().on(Socket.EVENT_CONNECT) {
                Log.d("TAG", "onBind: bind successfully")
            }
            it.emit("new user", userId)
            it.on("sending", listener)
            it.on("message_echo", echo)
        }
     //   checkIsForeground = false
        return Service.START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder {
        val _id = sharedPreferences.getString(Constants.USER_ID, "").toString()
        socket = io.socket.client.IO.socket(Constants.BASE_URL)
        socket.connect()

        socket.let {
            it.connect().on(Socket.EVENT_CONNECT) {
                Log.d("TAG", "onBind: bind successfully")
            }

            it.emit("new user", _id)
            it.on("sending", listener)
            it.on("message_echo", echo)
        }
      //  checkIsForeground = true
        return mBinder
    }


    inner class MyBinder : Binder() {
        fun getService(): ChatService {
            return this@ChatService
        }

    }

    fun sendMessage(username: String, data: String, user_id: String) {
        val obj = JSONObject()
        obj.put("sending_user_id", sharedPreferences.getString(Constants.USER_ID, "").toString())
        obj.put("user_id", user_id)
        obj.put("data", data)
        obj.put("username", username)
        list.postValue(Message(username, user_id, isSent = true, data))

        socket.emit("message", obj)
    }

    fun sendNotification(chat: Chat) {
        val data = chat
        val notification = NotificationCompat.Builder(this, MyAndroidApp.CHANNEL_ID)
        notification.setAutoCancel(true)
        notification.setSmallIcon(R.drawable.ic_back)
        notification.setContentTitle(data.sending_username + " sent you a message")
        notification.setContentText(data.message)
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

    override fun onUnbind(intent: Intent?): Boolean {
      //  checkIsForeground = false
        return super.onUnbind(intent)
    }

    override fun stopService(name: Intent?): Boolean {
      //  checkIsForeground = false
        Log.d("TAG", "stopService: onUnBind")
        return super.stopService(name)
    }
}