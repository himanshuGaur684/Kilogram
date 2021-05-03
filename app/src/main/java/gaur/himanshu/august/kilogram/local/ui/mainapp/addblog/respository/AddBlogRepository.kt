package gaur.himanshu.august.kilogram.local.ui.mainapp.addblog.respository

import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.remote.response.addblog.AddBlogResponse
import gaur.himanshu.august.kilogram.util.Result
import gaur.himanshu.august.kilogram.util.Status
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddBlogRepository(private val kilogramApi: KilogramApi) : IAddBlogRepository {

    override suspend fun postBlog(
        part: MultipartBody.Part,
        title: RequestBody,
        discription: RequestBody
    ): Result<AddBlogResponse> {
        return try {
            val response = kilogramApi.postBlog(part, title, discription)
            if (response.isSuccessful) {
                Result(Status.SUCCESS, response.body(), null)
            } else {
                Result(Status.ERROR, null, "Retry")
            }
        } catch (e: Exception) {
            Result(Status.ERROR, null, null)
        }
    }
}