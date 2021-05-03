package gaur.himanshu.august.kilogram.local.ui.mainapp.reply.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.remote.response.reply.Data
import retrofit2.HttpException
import java.io.IOException

class ReplyPagingSource(private val kilogramApi: KilogramApi,private val commentId:String): PagingSource<Int,Data>() {
    override fun getRefreshKey(state: PagingState<Int, Data>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Data> {
        val nextPageNumber = params.key ?: 1
        val response = kilogramApi.getAllReply(nextPageNumber, 5,commentId)
        Log.d("TAG", "load: ${response.body()}")
        return try {
            LoadResult.Page(
                data = response.body()?.reply?.data!!,
                prevKey = null,
                nextKey = if (response.body()?.reply?.data!!.isEmpty()) null else nextPageNumber + 1
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