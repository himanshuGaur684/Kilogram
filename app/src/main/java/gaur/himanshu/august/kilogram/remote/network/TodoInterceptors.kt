package gaur.himanshu.august.kilogram.remote.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit


class TodoIntercepters(private val token:String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request= chain.request().newBuilder()
            request.addHeader("Content-Type","application/json")

            if(token.isNotEmpty()){
                Log.d("TAG", "intercept: ${token}")
                request.addHeader("Authorization",token)
            }

            return chain.withConnectTimeout(1,TimeUnit.MINUTES).proceed(request.build())
        }
    }

