package gaur.himanshu.august.kilogram.local.ui.worker

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import gaur.himanshu.august.kilogram.Constants
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.remote.network.TodoIntercepters
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val POST_ID = "post_id"

const val INCREASE_LIKE = "increase"
const val DECREASE_LIKE = "decrease"

class LikeWorker(private val context: Context, val workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {


    override suspend fun doWork(): Result {
        val pref = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE)
        return try {
            val kilogramApi = init(pref)
            val post_id = workerParams.inputData.getString(POST_ID)!!
            val increase = workerParams.inputData.getBoolean(INCREASE_LIKE, false)
            val decrease = workerParams.inputData.getBoolean(DECREASE_LIKE, false)

            if (increase) {
                val response = kilogramApi.postLike(post_id)
                Log.d("TAG", "doWork: ${response.code()}")
                if (response.isSuccessful) {
                    ListenableWorker.Result.success()
                } else {
                    ListenableWorker.Result.retry()
                }
            }
            if (decrease) {

                val response = kilogramApi.deleteLike(post_id)
                Log.d("TAG", "doWork: ${response.code()}")
                if (response.isSuccessful) {
                    ListenableWorker.Result.success()
                } else {
                    ListenableWorker.Result.retry()
                }

            }
            Result.failure()
        } catch (e: Exception) {
            e.printStackTrace()
            ListenableWorker.Result.retry()
        }

    }


    private fun init(sharedPreferences: SharedPreferences): KilogramApi {
        val httpClient = OkHttpClient.Builder()
        val token = sharedPreferences.getString(Constants.AUTH_TOKEN, "")!!
        Log.d("TAG", "provideHttp: ${token}")
        httpClient.addInterceptor(TodoIntercepters(token))
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(httpClient.build())
            .build().create(KilogramApi::class.java)

    }
}