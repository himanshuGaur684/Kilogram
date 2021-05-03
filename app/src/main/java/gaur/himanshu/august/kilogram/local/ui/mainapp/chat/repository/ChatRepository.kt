package gaur.himanshu.august.kilogram.local.ui.mainapp.chat.repository

import android.content.SharedPreferences
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import gaur.himanshu.august.kilogram.local.room.kilogram.KilogramDao
import gaur.himanshu.august.kilogram.local.room.kilogram.RemoteDao
import gaur.himanshu.august.kilogram.local.ui.mainapp.chat.paging.ChatPagingSource
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.remote.response.chat.Chat
import kotlinx.coroutines.flow.Flow

class ChatRepository(
    private val remoteDao: RemoteDao,
    private val kilogramDao: KilogramDao,
    private val kilogramApi: KilogramApi,
    private val sharedPreferences: SharedPreferences
) {


    @ExperimentalPagingApi
    fun getChatStream(key: String): Flow<PagingData<Chat>> {
        return Pager(
            PagingConfig(pageSize = 10, enablePlaceholders = true),
            remoteMediator = ChatPagingSource(
                1,
                kilogramDao,
                remoteDao,
                kilogramApi,
                key,
                sharedPreferences
            )
        ) {
            kilogramDao.getAllChats()
        }.flow
    }


}