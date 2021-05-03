package gaur.himanshu.august.kilogram.remote.response.chat

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Chat(
    val __v: Int,
    @PrimaryKey(autoGenerate = false)
    val _id: String,
    val date: String,
    val message: String,
    val receiving_user: String,
    val sending_user: String,
    val sending_username: String,
    val unique_key: String,
    var isSent: Boolean = false
)