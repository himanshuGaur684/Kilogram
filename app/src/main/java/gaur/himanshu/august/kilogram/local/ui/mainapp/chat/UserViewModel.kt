package gaur.himanshu.august.kilogram.local.ui.mainapp.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import gaur.himanshu.august.kilogram.local.room.kilogram.KilogramDao
import gaur.himanshu.august.kilogram.local.ui.mainapp.chat.search.repository.IChatSearchRepository
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: IChatSearchRepository,
    private val kilogramDao: KilogramDao
) : ViewModel() {

    val list = Pager(PagingConfig(pageSize = 5)) {
        kilogramDao.getAllChatUsers()
    }.flow


}