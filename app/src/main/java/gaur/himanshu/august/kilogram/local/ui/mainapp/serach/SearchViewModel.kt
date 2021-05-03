package gaur.himanshu.august.kilogram.local.ui.mainapp.serach

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import gaur.himanshu.august.kilogram.local.room.kilogram.KilogramDao
import gaur.himanshu.august.kilogram.local.room.kilogram.RemoteDao
import gaur.himanshu.august.kilogram.local.ui.mainapp.serach.paging.SearchRemoteMediator
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val kilogramApi: KilogramApi,
    private val kilogramDao: KilogramDao,
    private val remoteDao: RemoteDao
) : ViewModel() {


    private val query = MutableLiveData<String>(null)

    @ExperimentalPagingApi
    val queryResult = query.switchMap {
        Pager(
            PagingConfig(pageSize = 4),
            remoteMediator = SearchRemoteMediator(1, kilogramDao, kilogramApi, remoteDao, it)
        ) {
            kilogramDao.getAllSeachUser()
        }.liveData.cachedIn(viewModelScope)
    }


    fun query(q: String) {
        query.postValue(q)
    }


}