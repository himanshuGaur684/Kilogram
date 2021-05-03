package gaur.himanshu.august.kilogram.local.ui.mainapp.home.repository

interface IHomeRepository {




    suspend fun postLike(post_id:String)

    suspend fun deleteLike(post_id:String)

    suspend fun saveBlogOffline(blog:gaur.himanshu.august.kilogram.remote.response.blog.Result)

}