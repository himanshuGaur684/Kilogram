package gaur.himanshu.august.kilogram.local.ui.mainapp.profile.viewpager.fragment.postfragment.paging

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PersonalPostRemoteKey(
    @PrimaryKey(autoGenerate = false)
    val _id:String,
    val prevKey:Int?,
    val nextKey:Int?
)