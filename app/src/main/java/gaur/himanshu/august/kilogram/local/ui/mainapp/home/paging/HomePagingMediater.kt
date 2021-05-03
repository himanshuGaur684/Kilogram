package gaur.himanshu.august.kilogram.local.ui.mainapp.home.paging

import android.util.Log
import androidx.paging.*
import gaur.himanshu.august.kilogram.local.room.kilogram.KilogramDao
import gaur.himanshu.august.kilogram.local.room.kilogram.RemoteDao
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import retrofit2.HttpException
import java.io.InvalidObjectException

@ExperimentalPagingApi
class HomePagingMediater(
    private val initialPage: Int = 1,
    private val db: KilogramDao,
    private val remoteDao: RemoteDao,
    private val kilogramApi: KilogramApi
) :
    RemoteMediator<Int, HomeBlog>() {


    override suspend fun initialize(): InitializeAction {
        return super.initialize()
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, HomeBlog>
    ): MediatorResult {


        try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: initialPage
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(true)
                }
                LoadType.APPEND -> {
                    Log.d("TAG", "load: ")
                    val remoteKeys = getRemoteKeyForLastItem(state)
                        ?: throw InvalidObjectException("Result is empty")
                    remoteKeys.nextKey ?: return MediatorResult.Success(true)
                }
            }


            val response = kilogramApi.getAllBlogs(page, state.config.pageSize)


            val endOfPaginationReached =
                response.body()?.blog?.results!!.size < state.config.pageSize

            if (response.isSuccessful) {
                response.body()?.let {
                    if (loadType == LoadType.REFRESH) {
                        db.deleteAllBlogs()
                        remoteDao.deleteBlogRemoteKeys()
                    }

                    Log.d("TAG", "load: ${it.blog.results.size}")
                    val prevKey = if (page == initialPage) null else page - 1
                    val nextKey = if (endOfPaginationReached) null else page + 1
                    val keys = it.blog.results.map { blog ->
                        BlogRemoteKey(blogId = blog._id, prevKey = prevKey, nextKey = nextKey)
                    }
                    remoteDao.insertBlogList(keys)
                    db.insertBlogList(it.blog.results.map { result ->
                        return@map HomeBlog(
                            __v = result.__v,
                            _id = result._id,
                            comment = result.comment,
                            createdAt = result.createdAt,
                            image = result.image,
                            like_by_me = result.like_by_me,
                            likes = result.likes,
                            profileimage = result.profileimage,
                            title = result.title,
                            user = result.user,
                            username = result.username,
                            discription = result.discription,
                            liked_by = result.liked_by
                        )
                    })
                }
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)


        } catch (e: Exception) {
            MediatorResult.Error(e)

        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
        return MediatorResult.Success(endOfPaginationReached = false)
    }


    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, HomeBlog>): BlogRemoteKey? {
        return state.lastItemOrNull()?.let { blog ->
            remoteDao.getRemoteKey(blogId = blog._id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, HomeBlog>): BlogRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?._id?.let {
                remoteDao.getRemoteKey(it)
            }

        }
    }
}