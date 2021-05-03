package gaur.himanshu.august.kilogram.remote.response

import gaur.himanshu.august.kilogram.remote.response.profile.User

data class LoginResponse(
    val success: Boolean,
    val token: String,
    val user:User
)