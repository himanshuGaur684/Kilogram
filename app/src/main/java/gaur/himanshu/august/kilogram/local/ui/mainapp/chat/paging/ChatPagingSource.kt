package gaur.himanshu.august.kilogram.local.ui.mainapp.chat.paging

import android.content.SharedPreferences
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import gaur.himanshu.august.kilogram.Constants
import gaur.himanshu.august.kilogram.local.room.kilogram.KilogramDao
import gaur.himanshu.august.kilogram.local.room.kilogram.RemoteDao
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.remote.response.chat.Chat
import java.io.InvalidObjectException

@ExperimentalPagingApi
class ChatPagingSource(
    private val initialPage: Int = 1,
    private val kilogramDao: KilogramDao,
    private val remoteDao: RemoteDao,
    private val kilogramApi: KilogramApi,
    private val uniqueKey: String,
    private val sharedPreferences: SharedPreferences
) : RemoteMediator<Int, Chat>() {
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Chat>): MediatorResult {


        try {

            val page = when (loadType) {

                LoadType.REFRESH -> {
                    val remoteKeys = getClosestKey(state)
                    remoteKeys?.nextKey?.minus(1) ?: initialPage
                }

                LoadType.APPEND -> {
                    val remoteKey = getLastKey(state)
                        ?: throw InvalidObjectException("Invalid object exception")
                    remoteKey.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

            }

            var length = uniqueKey.length
            length /= 2;
            val sender = uniqueKey.subSequence(0, length)
            val receiver = uniqueKey.subSequence(length, (2 * length))

            val response =
                kilogramApi.getAllChats(
                    sender.toString(),
                    receiver.toString(),
                    page,
                    state.config.pageSize
                )


            val endOfPaginationReached =
                response.body()?.results?.chat?.size!! < state.config.pageSize

            response.body()?.let { chatsResponse ->
                if (loadType == LoadType.REFRESH) {
                    kilogramDao.deleteAllChat()
                    remoteDao.deleteAllChatRemoteKey()
                }
                val prevKey = if (page == initialPage) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1

                val list = chatsResponse.results.chat.map {
                    ChatRemoteKey(it._id, prevKey, nextKey)
                }

                val user = sharedPreferences.getString(Constants.USER_ID, "")
                    .toString()
                val chatList = chatsResponse.results.chat.map {
                    Chat(
                        __v = it.__v,
                        _id = it._id,
                        date = it.date,
                        message = it.message,
                        receiving_user = it.receiving_user,
                        sending_user = it.sending_user,
                        sending_username = it.sending_username,
                        unique_key = it.unique_key,
                        isSent = user == it.sending_user
                    )
                }

                Log.d("TAG", "load${page}: ${chatList}")
                remoteDao.insertChatRemoteKey(list)
                kilogramDao.insertChats(chatList)

            }


        } catch (e: Exception) {
            MediatorResult.Error(e)
        }

        return MediatorResult.Success(endOfPaginationReached = false)
    }


    private suspend fun getLastKey(state: PagingState<Int, Chat>): ChatRemoteKey? {
        return state.lastItemOrNull()?.let {
            remoteDao.getChatRemoteKey(it._id)
        }
    }

    private suspend fun getClosestKey(state: PagingState<Int, Chat>): ChatRemoteKey? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.let {
                remoteDao.getChatRemoteKey(it._id)
            }
        }
    }

}