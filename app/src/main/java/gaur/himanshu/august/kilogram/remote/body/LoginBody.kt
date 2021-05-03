package gaur.himanshu.august.kilogram.remote.body

data class LoginBody(
    val email: String,
    val password: String,
    val fcm_token:String

)