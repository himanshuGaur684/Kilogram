package gaur.himanshu.august.kilogram.local.ui.mainapp.notificationpost.repository

import android.util.Log
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.util.Result
import gaur.himanshu.august.kilogram.util.Status
import java.lang.Exception

class NotificationPostRepository(private val kilogramApi: KilogramApi) : INotificationPostRepository {
    override suspend fun getSingleBlog(userId: String): Result<gaur.himanshu.august.kilogram.remote.response.blog.Result> {
        return try{
            val response= kilogramApi.getSingleBlog(userId)

            if(response.isSuccessful){

                Log.d("TAG", "getSingleBlog: ${response.body()}")
             if(response.body()?.blog?.results?.size!=0){
                 Result(Status.SUCCESS, response.body()?.blog?.results!![0],null)
             }else{
                 Result(Status.ERROR,null,null)
             }
            }else{
                Result(Status.ERROR,null,"bsdk")
            }




        }catch (e:Exception){
            Log.d("TAG", "getSingleBlog: ${e.printStackTrace()}")
            Result(Status.ERROR,null,e.message)
        }
    }
}