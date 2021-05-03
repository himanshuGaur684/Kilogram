package gaur.himanshu.august.kilogram.local.ui.mainapp.discover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import gaur.himanshu.august.kilogram.local.room.kilogram.KilogramDao
import gaur.himanshu.august.kilogram.local.room.kilogram.RemoteDao
import gaur.himanshu.august.kilogram.local.ui.mainapp.discover.paging.DiscoverRemoteMediator
import gaur.himanshu.august.kilogram.local.ui.mainapp.discover.repository.IDiscoverRepository
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.remote.response.discover.Discover
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiscoverViewModel @Inject constructor(
    private val kilogramApi: KilogramApi,
    private val remoteDao: RemoteDao,
    private val kilogramDao: KilogramDao,
    private val repository:IDiscoverRepository
) : ViewModel() {


    @ExperimentalPagingApi
    val discover = Pager(
        PagingConfig(pageSize = 4),
        remoteMediator = DiscoverRemoteMediator(1, remoteDao, kilogramDao, kilogramApi)
    ) {
        //DiscoverPagingSource(kilogramApi)
        kilogramDao.getAllDiscoverList()
    }.liveData.cachedIn(viewModelScope)


    fun updateDiscover(discover:Discover)=viewModelScope.launch {
        repository.updateDiscover(discover)
    }


}