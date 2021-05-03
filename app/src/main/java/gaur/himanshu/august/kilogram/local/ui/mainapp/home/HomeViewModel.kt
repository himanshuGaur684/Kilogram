package gaur.himanshu.august.kilogram.local.ui.mainapp.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import gaur.himanshu.august.kilogram.local.room.kilogram.KilogramDao
import gaur.himanshu.august.kilogram.local.ui.mainapp.home.paging.HomePagingMediater
import gaur.himanshu.august.kilogram.local.room.kilogram.RemoteDao
import gaur.himanshu.august.kilogram.local.ui.mainapp.home.repository.IHomeRepository
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.remote.response.blog.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: IHomeRepository,
    private val kilogramApi: KilogramApi,
    private val kilogramDao: KilogramDao,
    private val remoteDao: RemoteDao

) : ViewModel() {


    @ExperimentalPagingApi
    val blog = Pager(
        PagingConfig(pageSize = 10,enablePlaceholders = true),

        remoteMediator = HomePagingMediater(1,kilogramDao,remoteDao,kilogramApi),

    ) {
        kilogramDao.getAllSaveBlogs()
        //HomePagingSource(kilogramApi)
    }.flow


    fun postLike(post_id: String) = viewModelScope.launch {

        repository.postLike(post_id)
    }

    fun deleteLike(post_id: String) = viewModelScope.launch {
        repository.deleteLike(post_id)
    }

    fun saveBlogOffline(blog: Result) = viewModelScope.launch {
        repository.saveBlogOffline(blog)
    }


}