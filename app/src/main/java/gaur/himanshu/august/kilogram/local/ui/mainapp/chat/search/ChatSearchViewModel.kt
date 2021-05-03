package gaur.himanshu.august.kilogram.local.ui.mainapp.chat.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import gaur.himanshu.august.kilogram.local.room.kilogram.KilogramDao
import gaur.himanshu.august.kilogram.local.ui.mainapp.chat.search.paging.ChatSearchPagingSource
import gaur.himanshu.august.kilogram.local.ui.mainapp.chat.search.repository.IChatSearchRepository
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.remote.response.chatsearch.ChatUser
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatSearchViewModel @Inject constructor(
    private val kilogramApi: KilogramApi,
    private val repository: IChatSearchRepository,
    private val kilogramDao: KilogramDao
) : ViewModel() {

    val query = MutableLiveData<String>()

    val list = query.switchMap {
        Pager(PagingConfig(pageSize = 10)) {
            ChatSearchPagingSource(kilogramApi, it)
        }.liveData.cachedIn(viewModelScope)
    }

    val charUserList = Pager(PagingConfig(pageSize = 5)) {
        kilogramDao.getAllChatUsers()
    }.flow


    fun postQuery(str: String) {
        query.postValue(str)
    }

    fun insertChatUser(chatUser: ChatUser) = viewModelScope.launch {
        repository.insertChatUser(chatUser)
    }


}