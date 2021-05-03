package gaur.himanshu.august.kilogram.local.ui.auth.repository

import android.content.SharedPreferences
import gaur.himanshu.august.kilogram.Constants
import gaur.himanshu.august.kilogram.remote.body.LoginBody
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.remote.response.LoginResponse
import gaur.himanshu.august.kilogram.remote.response.RegisterResponse
import gaur.himanshu.august.kilogram.util.Result
import gaur.himanshu.august.kilogram.util.Status
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AuthRepository(
    private val kilogramApi: KilogramApi,
    private val sharedPreferences: SharedPreferences
) : IAuthRepository {
    override suspend fun login(loginBody: LoginBody): Result<LoginResponse> {
        return try {
            val response = kilogramApi.login(loginBody)
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    sharedPreferences.edit().putString(Constants.AUTH_TOKEN, it.token).apply()
                    sharedPreferences.edit().putString(Constants.USER_EMAIL, it.user.email).apply()
                    sharedPreferences.edit().putString(Constants.USER_NAME, it.user.username).apply()
                    sharedPreferences.edit().putString(Constants.USER_ID, it.user._id)
                        .apply()
                    sharedPreferences.edit().putString(Constants.USER_IMAGE, it.user.profileimage)
                        .apply()
                }
                Result(Status.SUCCESS, response.body(), null)
            } else {
                Result(Status.ERROR, null, null)
            }
        } catch (e: Exception) {
            Result(Status.ERROR, null, e.message)
        }
    }

    override suspend fun register(
        part: MultipartBody.Part,
        userName: RequestBody,
        password: RequestBody,
        email: RequestBody,
        phone: RequestBody,
        fcmtoken:RequestBody
    ): Result<RegisterResponse> {

        return try {
                val response = kilogramApi.register(part, userName, password, email, phone,fcmtoken)
                if (response.isSuccessful) {
                    sharedPreferences.edit().putString(Constants.AUTH_TOKEN, response.body()?.token)
                        .apply()
                    Result(Status.SUCCESS, response.body(), null)
                } else {
                    Result(Status.ERROR, null, response.code().toString())
                }


        } catch (e: Exception) {
            e.printStackTrace()
            Result(Status.ERROR, null, e.message)
        }
    }
}