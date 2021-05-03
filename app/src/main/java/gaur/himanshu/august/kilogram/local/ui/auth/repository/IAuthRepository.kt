package gaur.himanshu.august.kilogram.local.ui.auth.repository

import gaur.himanshu.august.kilogram.remote.body.LoginBody
import gaur.himanshu.august.kilogram.remote.response.LoginResponse
import gaur.himanshu.august.kilogram.remote.response.RegisterResponse
import gaur.himanshu.august.kilogram.util.Result
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface IAuthRepository {

    suspend fun login(loginBody: LoginBody):Result<LoginResponse>

    suspend fun register(part:MultipartBody.Part,userName:RequestBody,password:RequestBody,email:RequestBody,phone:RequestBody,fcmToken:RequestBody):Result<RegisterResponse>


}