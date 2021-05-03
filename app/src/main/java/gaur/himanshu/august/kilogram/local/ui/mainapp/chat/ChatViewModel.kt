package gaur.himanshu.august.kilogram.local.ui.mainapp.chat

import android.content.SharedPreferences
import androidx.lifecycle.*
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import gaur.himanshu.august.kilogram.local.room.kilogram.KilogramDao
import gaur.himanshu.august.kilogram.local.room.kilogram.RemoteDao
import gaur.himanshu.august.kilogram.local.ui.mainapp.chat.repository.ChatRepository
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.remote.response.chat.Chat
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val kilogramApi: KilogramApi,
    private val kilogramDao: KilogramDao,
    private val remoteDao: RemoteDao,
    private val sharedPreferences: SharedPreferences,
    private val chatRepository: ChatRepository
) : ViewModel() {



    @ExperimentalPagingApi
    fun getChatRepository(key: String): Flow<PagingData<Chat>> {
        return chatRepository.getChatStream(key)
    }


    fun getAllChatFlows():Flow<List<Chat>>{
       return kilogramDao.getAllChatFlows()
    }

}