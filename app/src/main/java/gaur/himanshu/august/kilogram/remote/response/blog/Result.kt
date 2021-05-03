package gaur.himanshu.august.kilogram.remote.response.blog

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Result(
    val __v: Int,
    @PrimaryKey(autoGenerate = false)
    val _id: String,
    val comment: Int,
    val createdAt: String,
    val image: String,
    var like_by_me: Boolean,
    var likes: Int,
    val profileimage: String,
    val title: String,
    val user: String,
    val username: String,
    val discription:String?,
    val liked_by:String
)