package gaur.himanshu.august.kilogram.local.ui.mainapp.follow.repository

import android.util.Log
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.remote.response.msg.Msg
import gaur.himanshu.august.kilogram.util.Result
import gaur.himanshu.august.kilogram.util.Status

class FollowRepository(
    private val kilogramApi: KilogramApi
) : IFollowRepository {

    override suspend fun followUser(otherUserId: String): Result<Msg> {
        return try {

            val response = kilogramApi.follow(otherUserId)
            Log.d("TAG", "followUser: SLjgashgkl")
            if (response.isSuccessful) {
                Result(Status.SUCCESS, response.body(), null)
            } else {
                Result(Status.ERROR, null, "failed")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Result(Status.ERROR, null, e.message)
        }
    }

    override suspend fun unFollowUser(otherUserId: String): Result<Msg> {
        return try {

            val response = kilogramApi.unFollow(otherUserId)
            if (response.isSuccessful) {
                Result(Status.SUCCESS, response.body(), null)
            } else {
                Result(Status.ERROR, null, "failed")
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Result(Status.ERROR, null, e.message)
        }
    }
}