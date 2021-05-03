package gaur.himanshu.august.kilogram.local.ui.mainapp.follow.repository

import gaur.himanshu.august.kilogram.remote.response.msg.Msg
import gaur.himanshu.august.kilogram.util.Result

interface IFollowRepository {

    suspend fun followUser(otherUserId: String): Result<Msg>


    suspend fun unFollowUser(otherUserId: String): Result<Msg>

}