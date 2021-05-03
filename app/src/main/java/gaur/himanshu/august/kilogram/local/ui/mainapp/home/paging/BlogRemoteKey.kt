package gaur.himanshu.august.kilogram.local.ui.mainapp.home.paging

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class BlogRemoteKey(
    @PrimaryKey(autoGenerate = false)
    val blogId: String,
    val prevKey: Int?,
    val nextKey: Int?
)