package gaur.himanshu.august.kilogram.local.ui.mainapp.follow.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import gaur.himanshu.august.kilogram.local.ui.mainapp.follow.FollowIndicators
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.remote.response.follow.Follow
import retrofit2.HttpException
import java.io.IOException

class FollowPagingSource(
    private val kilogramApi: KilogramApi,
    private val followIndicators: FollowIndicators,
    private val user_id: String
) : PagingSource<Int, Follow>() {
    override fun getRefreshKey(state: PagingState<Int, Follow>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Follow> {
        val nextPageNumber = params.key ?: 1
        val response = if (followIndicators == FollowIndicators.FOLLOWER) {
            kilogramApi.getAllOfMyFollowering(user_id, nextPageNumber, 5)
        } else {
            kilogramApi.getAllOfMyFollowings(user_id, nextPageNumber, 5)
        }


        return try {
            LoadResult.Page(
                data = response.body()?.follow!!,
                prevKey = null,
                nextKey = if (response.body()?.follow!!.isEmpty()) null else nextPageNumber + 1
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