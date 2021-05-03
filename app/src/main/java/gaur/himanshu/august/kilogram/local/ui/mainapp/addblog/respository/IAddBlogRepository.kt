package gaur.himanshu.august.kilogram.local.ui.mainapp.addblog.respository

import gaur.himanshu.august.kilogram.remote.response.addblog.AddBlogResponse
import gaur.himanshu.august.kilogram.util.Result
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface IAddBlogRepository {

    suspend fun postBlog(
        part:MultipartBody.Part,
        title:RequestBody,
        discription:RequestBody
    ):Result<AddBlogResponse>


}