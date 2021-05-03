package gaur.himanshu.august.kilogram.local.ui.mainapp.notification.paging

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class NotificationRemoteKey(
    @PrimaryKey(autoGenerate = false)
    val _id:String,
    val prevKey:Int?,
    val nextKey:Int?
)  {
}