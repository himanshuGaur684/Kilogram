package gaur.himanshu.august.kilogram.remote.response.profile

data class User(
    val __v: Int,
    val _id: String,
    val email: String,
    val follower: Int,
    val following: Int,
    val password: String,
    val phone: String,
    val profileimage: String,
    val username: String,
    val posts:Int?
)