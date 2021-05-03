package gaur.himanshu.august.kilogram.remote.response.search

data class SearchResponse(
    val msg: String,
    val success: Boolean,
    val users: Users
)