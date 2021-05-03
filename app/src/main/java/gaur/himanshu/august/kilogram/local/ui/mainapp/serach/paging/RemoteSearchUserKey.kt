package gaur.himanshu.august.kilogram.local.ui.mainapp.serach.paging

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class RemoteSearchUserKey(
    @PrimaryKey(autoGenerate = false)
    val _id:String,
    val prevKey:Int?,
    val nextKey:Int?
) {
}