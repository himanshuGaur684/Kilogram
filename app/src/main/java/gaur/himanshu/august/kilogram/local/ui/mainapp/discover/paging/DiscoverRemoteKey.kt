package gaur.himanshu.august.kilogram.local.ui.mainapp.discover.paging

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class DiscoverRemoteKey(
    @PrimaryKey(autoGenerate = false)
    val _id: String,
    val prevKey: Int?,
    val nextKey: Int?
) {
}