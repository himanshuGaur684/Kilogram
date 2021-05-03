package gaur.himanshu.august.kilogram.local.ui.mainapp.notificationpost.repository

import gaur.himanshu.august.kilogram.util.Result

interface INotificationPostRepository {


    suspend fun getSingleBlog(userId:String): Result<gaur.himanshu.august.kilogram.remote.response.blog.Result>

}