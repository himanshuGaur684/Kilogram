package gaur.himanshu.august.kilogram.remote.response.discover

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Discover(
    @PrimaryKey(autoGenerate = false)
    val _id: String,
    val profileimage: String,
    val username: String
)