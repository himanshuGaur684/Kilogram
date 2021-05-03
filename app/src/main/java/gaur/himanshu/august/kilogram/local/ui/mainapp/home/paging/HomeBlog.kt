package gaur.himanshu.august.kilogram.local.ui.mainapp.home.paging

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity
@Parcelize
data class HomeBlog(
    val __v: Int,
    @PrimaryKey(autoGenerate = false)
    val _id: String,
    var comment: Int,
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
) : Parcelable