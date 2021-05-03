package gaur.himanshu.august.kilogram.remote.response.notification

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Notification(
    val __v: Int,
    @PrimaryKey(autoGenerate = false)
    val _id: String,
    val action_taking_user: String,
    val action_taking_user_profile_image: String,
    val action_taking_user_username: String,
//    val comment_id: String?,
    val message: String,
    val personal_user: String,
    val post_id: String?
)