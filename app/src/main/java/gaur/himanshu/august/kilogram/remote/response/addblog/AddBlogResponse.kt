package gaur.himanshu.august.kilogram.remote.response.addblog

import gaur.himanshu.august.kilogram.remote.response.blog.Result

data class AddBlogResponse(
    val blog: Result,
    val msg: String,
    val success: Boolean
)