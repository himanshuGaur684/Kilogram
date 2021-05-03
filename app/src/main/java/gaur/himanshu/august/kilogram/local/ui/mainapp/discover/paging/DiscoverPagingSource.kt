package gaur.himanshu.august.kilogram.local.ui.mainapp.discover.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.remote.response.discover.Discover
import retrofit2.HttpException
import java.io.IOException

class DiscoverPagingSource(private val kilogramApi: KilogramApi) : PagingSource<Int,Discover>() {

    override fun getRefreshKey(state: PagingState<Int, Discover>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Discover> {
        val nextPageNumber = params.key ?: 1
        val response = kilogramApi.getAllDiscoverUser(nextPageNumber, 10)
        return try {
            LoadResult.Page(
                data = response.body()?.result?.discover!!,
                prevKey = null,
                nextKey = if (response.body()?.result?.discover!!.isEmpty()) null else nextPageNumber + 1
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