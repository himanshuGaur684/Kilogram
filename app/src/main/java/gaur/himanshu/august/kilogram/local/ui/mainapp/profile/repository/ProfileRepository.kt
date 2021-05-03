package gaur.himanshu.august.kilogram.local.ui.mainapp.profile.repository

import android.util.Log
import gaur.himanshu.august.kilogram.local.room.kilogram.KilogramDao
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.remote.response.profile.ProfileResponse
import gaur.himanshu.august.kilogram.util.Result
import gaur.himanshu.august.kilogram.util.Status

class ProfileRepository(private val kilogramApi: KilogramApi,private val kilogramDao: KilogramDao) : IProfileRepository {
    override suspend fun profileDetails(userId: String): Result<ProfileResponse> {
        return try {

            val response = kilogramApi.profileDetails(userId)
            if (response.isSuccessful) {
                Log.d("TAG", "profileDetails: ${response.body()?.user}")
                Result(Status.SUCCESS, response.body(), null)
            } else {
                Log.d("TAG", "profileDetails: ${response.code()}")
                Result(Status.ERROR, null, null)
            }


        } catch (e: Exception) {
            e.printStackTrace()
            Result(Status.ERROR, null, e.message)
        }
    }

    override suspend fun deleteOfflineBlog(result: gaur.himanshu.august.kilogram.remote.response.blog.Result) {
        kilogramDao.deleteOfflineBlog(result)
    }


}