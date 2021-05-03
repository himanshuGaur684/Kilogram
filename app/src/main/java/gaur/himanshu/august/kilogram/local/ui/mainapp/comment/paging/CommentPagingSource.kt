package gaur.himanshu.august.kilogram.local.ui.mainapp.comment.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.remote.response.comments.Comment
import retrofit2.HttpException
import java.io.IOException

class CommentPagingSource(private val kilogramApi: KilogramApi,private val post_id:String) : PagingSource<Int,Comment>() {
    override fun getRefreshKey(state: PagingState<Int, Comment>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comment> {
        val nextPageNumber = params.key ?: 1
        val response = kilogramApi.getAllComments(nextPageNumber, 10,post_id)
        return try {
            LoadResult.Page(
                data = response.body()?.comment!!,
                prevKey = null,
                nextKey = if (response.body()?.comment!!.isEmpty()) null else nextPageNumber + 1
            )
        } catch (e: HttpException) {
            e.printStackTrace()
            LoadResult.Error(e)
        } catch (e: IOException) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }
}