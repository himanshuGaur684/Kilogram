package gaur.himanshu.august.kilogram.remote.response.chatsearch

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class ChatUser(
    val __v: Int,
    @PrimaryKey(autoGenerate = false)
    val _id: String,
    val email: String,
    val fcm_token: String?,
    val follower: Int,
    val following: Int,
    val password: String,
    val phone: String,
    val posts: Int,
    val profileimage: String,
    val username: String
):Parcelable