package gaur.himanshu.august.kilogram.local.ui.mainapp.profile.viewpager.fragment.postfragment.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import gaur.himanshu.august.kilogram.local.room.kilogram.KilogramDao
import gaur.himanshu.august.kilogram.local.room.kilogram.RemoteDao
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import java.io.InvalidObjectException

@ExperimentalPagingApi
class PersonalPostsRemoteMediator(
    private val initialPage: Int = 1,
    private val userId: String,
    private val kilogramDao: KilogramDao,
    private val remoteDao: RemoteDao,
    private val kilogramApi: KilogramApi
) : RemoteMediator<Int, PersonalBlog>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PersonalBlog>
    ): MediatorResult {


        try {

            val page = when (loadType) {
                LoadType.REFRESH -> {

                    val remoteKey = getClosestKey(state)
                    remoteKey?.nextKey?.minus(1) ?: initialPage
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val remoteKey = getLast(state) ?: throw  InvalidObjectException("Invalid")
                    remoteKey.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val response = kilogramApi.getAllBlogs(userId, page, 10)

            val endOfPaginationReached = response.body()?.blog?.blog?.size!! > state.config.pageSize

            if (response.isSuccessful) {
                response.body()?.let {
                    if (loadType == LoadType.REFRESH) {
                        remoteDao.deleteAllPersonalRemoteKey()
                        kilogramDao.deletePersonalBlogs()
                    }

                    val prevKey = if (page == initialPage) null else page - 1
                    val nextKey = if (endOfPaginationReached) null else page + 1

                    val keys = it.blog.blog.map {
                        PersonalPostRemoteKey(it._id, prevKey, nextKey)
                    }

                    remoteDao.insertPersonalBlogs(keys)

                    kilogramDao.insertPersonalBlogs(it.blog.blog)


                }
            }

        } catch (e: Exception) {
            return MediatorResult.Error(e)
        }
        return MediatorResult.Success(endOfPaginationReached = true)

    }

    private suspend fun getClosestKey(state: PagingState<Int, PersonalBlog>): PersonalPostRemoteKey? {
        return state.anchorPosition?.let {
            state.closestItemToPosition(it)?.let {
                remoteDao.getAllPersonalRemoteKey(it._id)
            }
        }
    }

    private suspend fun getLast(state: PagingState<Int, PersonalBlog>): PersonalPostRemoteKey? {
        return state.lastItemOrNull()?.let {
            remoteDao.getAllPersonalRemoteKey(it._id)
        }
    }
}