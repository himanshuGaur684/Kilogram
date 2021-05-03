package gaur.himanshu.august.kilogram.local.ui.mainapp.serach.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import gaur.himanshu.august.kilogram.local.room.kilogram.KilogramDao
import gaur.himanshu.august.kilogram.local.room.kilogram.RemoteDao
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.remote.response.search.SearchUser
import java.io.InvalidObjectException
import java.lang.Exception

@ExperimentalPagingApi
class SearchRemoteMediator(
    private val initialPage: Int = 1,
    private val kilogramDao: KilogramDao,
    private val kilogramApi: KilogramApi,
    private val remoteDao: RemoteDao,
    private val query: String?
) : RemoteMediator<Int, SearchUser>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, SearchUser>
    ): MediatorResult {
        try {

            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: initialPage
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state) ?: throw InvalidObjectException(
                        "Invalid Object Exception"
                    )
                    remoteKeys.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val response = query?.let { kilogramApi.getAllSearchedResult(it, page, 10) }

            val endOfPaginationReached = if (query == null) false else
                response?.body()?.users?.results?.size!! < state.config.pageSize

            if (response?.isSuccessful == true) {
                response.body()?.let {
                    if (loadType == LoadType.REFRESH) {
                        remoteDao.deleteAllRemoteSearchUserKey()
                        kilogramDao.deleteAllSearchedUser()
                    }

                    val nextKey = if (page == initialPage) null else page + 1
                    val prevKey = if (endOfPaginationReached) null else page - 1

                    val keys = it.users.results.map {
                        RemoteSearchUserKey(it._id, prevKey, nextKey)
                    }

                    remoteDao.insertSearchKey(keys)

                    kilogramDao.insertSearch(it.users.results)

                }

            }

        } catch (e: Exception) {
            MediatorResult.Error(e)
        }

        return MediatorResult.Success(endOfPaginationReached = true)
    }


    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, SearchUser>): RemoteSearchUserKey? {
        return state.lastItemOrNull()?.let {
            remoteDao.getSearchRemoteKey(it._id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, SearchUser>): RemoteSearchUserKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.let {
                remoteDao.getSearchRemoteKey(it._id)
            }

        }
    }

}