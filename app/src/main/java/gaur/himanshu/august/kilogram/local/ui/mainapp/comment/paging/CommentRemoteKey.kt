package gaur.himanshu.august.kilogram.local.ui.mainapp.comment.paging

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CommentRemoteKey(
    @PrimaryKey(autoGenerate = false)
    val _id:String,
    val prevKey:Int?,
    val nextKey:Int?
)