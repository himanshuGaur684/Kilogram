package gaur.himanshu.august.kilogram.remote.response.comments

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Comment(
    @PrimaryKey(autoGenerate = false)
    val _id: String,
    val comments: String,
    val profileimage: String,
    val username: String
)