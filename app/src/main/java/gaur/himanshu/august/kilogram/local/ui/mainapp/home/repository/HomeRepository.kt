package gaur.himanshu.august.kilogram.local.ui.mainapp.home.repository

import android.util.Log
import gaur.himanshu.august.kilogram.local.room.kilogram.KilogramDao
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.util.Result
import gaur.himanshu.august.kilogram.util.Status

class HomeRepository(private val kilogramApi: KilogramApi, private val kilogramDao: KilogramDao) :
    IHomeRepository {


    override suspend fun postLike(post_id: String) {
        try {
            val response = kilogramApi.postLike(post_id)
            if (response.isSuccessful) {
                Log.d("TAG", "getAllBlogs: ${response.body()}")
                Result(Status.SUCCESS, response.body(), null)
            } else {
                Result(Status.ERROR, null, "Not Successfull")
            }


        } catch (e: Exception) {
            e.printStackTrace()
            Result(Status.ERROR, null, e.message)
        }
    }

    override suspend fun deleteLike(post_id: String) {
        try {
            val response = kilogramApi.deleteLike(post_id)
            if (response.isSuccessful) {
                Log.d("TAG", "getAllBlogs: ${response.body()}")
                Result(Status.SUCCESS, response.body(), null)
            } else {
                Result(Status.ERROR, null, "Not Successfull")
            }


        } catch (e: Exception) {
            e.printStackTrace()
            Result(Status.ERROR, null, e.message)
        }
    }

    override suspend fun saveBlogOffline(blog: gaur.himanshu.august.kilogram.remote.response.blog.Result) {
        kilogramDao.insertAll(blog)
    }
}