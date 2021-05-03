package gaur.himanshu.august.kilogram.local.ui.mainapp.comment.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import gaur.himanshu.august.kilogram.local.room.kilogram.KilogramDao
import gaur.himanshu.august.kilogram.local.room.kilogram.RemoteDao
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.remote.response.comments.Comment
import java.io.InvalidObjectException

@ExperimentalPagingApi
class CommentRemoteMediator(
    private val postId: String,
    private val initialPage: Int=1,
    private val kilogramApi: KilogramApi,
    private val kilogramDao: KilogramDao,
    private val remoteDao: RemoteDao
) : RemoteMediator<Int, Comment>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Comment>
    ): MediatorResult {

        try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKey = getClosest(state)
                    remoteKey?.nextKey?.minus(1) ?: initialPage

                }
                LoadType.APPEND -> {
                    val remoteKey = getLastKey(state) ?: throw InvalidObjectException("Invalid")
                    remoteKey.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
            }


            if (postId.isNotEmpty()) {


                val response = kilogramApi.getAllComments(page, 10, postId)

                val endOfPaginatedResult = response.body()?.comment?.size!! < state.config.pageSize

                if (response.isSuccessful) {

                    response.body()?.let {
                        if (loadType == LoadType.REFRESH) {
                            kilogramDao.deleteAllFromComment()
                            remoteDao.deleteAllCommentRemoteKey()
                        }

                        val prevKey = if (page == initialPage) null else page - 1
                        val nextKey = if (endOfPaginatedResult) null else page + 1

                        val keys = it.comment.map {comment->
                            CommentRemoteKey(comment._id, prevKey, nextKey)
                        }

                        kilogramDao.insertAllComment(it.comment)
                        remoteDao.insertCommentKey(keys)


                    }


                }
            }

        } catch (e: Exception) {
            MediatorResult.Error(e)
        }

        return MediatorResult.Success(endOfPaginationReached = true)

    }

    private suspend fun getLastKey(state: PagingState<Int, Comment>): CommentRemoteKey? {
        return state.lastItemOrNull()?.let {
            remoteDao.getAllCommentRemoteKey(it._id)
        }
    }

    private suspend fun getClosest(state: PagingState<Int, Comment>): CommentRemoteKey? {
        return state.anchorPosition?.let {
            state.closestItemToPosition(it)?.let {
                remoteDao.getAllCommentRemoteKey(it._id)
            }
        }
    }
}