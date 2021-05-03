package gaur.himanshu.august.kilogram.remote.response.search

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchUser(
    @PrimaryKey(autoGenerate = false)
    val _id: String,
    val profileimage: String,
    val username: String
)