package gaur.himanshu.august.kilogram.local.ui.mainapp.chat.paging

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChatRemoteKey(
    @PrimaryKey(autoGenerate = false) val id: String,
    val prevKey: Int?,
    val nextKey: Int?
)