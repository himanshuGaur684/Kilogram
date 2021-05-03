package gaur.himanshu.august.kilogram.local.ui.mainapp.serach.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.remote.response.search.SearchUser
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class SearchPagingSource(private val kilogramApi: KilogramApi, private val q: String) :
    PagingSource<Int, SearchUser>() {
    override fun getRefreshKey(state: PagingState<Int, SearchUser>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchUser> {
        val nextPageNumber = params.key ?: 1
        val response = kilogramApi.getAllSearchedResult(q, nextPageNumber, 5)
        Log.d("TAG", "load: ${response.body()}")
        return try {
            if(q==null){return LoadResult.Error(Exception())}
            LoadResult.Page(
                data = response.body()?.users?.results!!,
                prevKey = null,
                nextKey = if (response.body()?.users?.results?.isEmpty()!!) null else nextPageNumber + 1
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