package gaur.himanshu.august.kilogram.local.ui.mainapp.discover.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import gaur.himanshu.august.kilogram.local.room.kilogram.KilogramDao
import gaur.himanshu.august.kilogram.local.room.kilogram.RemoteDao
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.remote.response.discover.Discover
import java.io.InvalidObjectException
import java.lang.Exception


@ExperimentalPagingApi
class DiscoverRemoteMediator(
    private val initialPage: Int = 1,
    private val remoteDao: RemoteDao,
    private val kilogramDao: KilogramDao,
    private val kilogramApi: KilogramApi
) : RemoteMediator<Int, Discover>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Discover>
    ): MediatorResult {

        try {

            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: initialPage

                }
                LoadType.APPEND -> {
                    val remoteKey = getRemoteKeyForLastItem(state)
                        ?: throw InvalidObjectException("Invalid Object exception")
                    remoteKey.nextKey ?: return MediatorResult.Success(true)
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val response = kilogramApi.getAllDiscoverUser(page, 10)

            val endOfPaginationReached =
                response.body()?.result?.discover?.size!! < state.config.pageSize

            if (response.isSuccessful) {
                response.body()?.let {
                    if (loadType == LoadType.REFRESH) {
                        remoteDao.deleteAllDiscoverKey()
                        kilogramDao.deleteAllDiscoverList()
                    }

                    val prevKey = if (page == initialPage) null else page - 1
                    val nextKey = if (endOfPaginationReached) null else page + 1

                    val keys = it.result.discover.map {discover->
                        DiscoverRemoteKey(discover._id, prevKey, nextKey)
                    }

                    remoteDao.insertDiscoverRemoteKeys(keys)
                    kilogramDao.insertDiscoverList(it.result.discover)

                }
            }


        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }

        return MediatorResult.Success(endOfPaginationReached = true)

    }


    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Discover>): DiscoverRemoteKey? {
        return state.lastItemOrNull()?.let {
            remoteDao.getDiscoverRemoteKey(it._id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Discover>): DiscoverRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.let {
                remoteDao.getDiscoverRemoteKey(it._id)
            }
        }
    }


}