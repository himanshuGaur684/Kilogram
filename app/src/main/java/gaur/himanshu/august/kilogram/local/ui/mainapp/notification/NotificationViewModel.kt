package gaur.himanshu.august.kilogram.local.ui.mainapp.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import gaur.himanshu.august.kilogram.local.room.kilogram.KilogramDao
import gaur.himanshu.august.kilogram.local.room.kilogram.RemoteDao
import gaur.himanshu.august.kilogram.local.ui.mainapp.notification.paging.NotificationRemoteMediator
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val kilogramApi: KilogramApi,
    private val kilogramDao: KilogramDao,
    private val remoteDao: RemoteDao
) : ViewModel() {


    @ExperimentalPagingApi
    val notification = Pager(
        PagingConfig(pageSize = 10),
        remoteMediator = NotificationRemoteMediator(1, kilogramDao, remoteDao, kilogramApi)
    ) {
        kilogramDao.getNotification()
    }.flow.cachedIn(viewModelScope)


}