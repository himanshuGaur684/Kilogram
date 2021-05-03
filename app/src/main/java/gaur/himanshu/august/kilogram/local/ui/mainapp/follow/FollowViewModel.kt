package gaur.himanshu.august.kilogram.local.ui.mainapp.follow

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import gaur.himanshu.august.kilogram.local.ui.mainapp.follow.paging.FollowPagingSource
import gaur.himanshu.august.kilogram.local.ui.mainapp.follow.repository.IFollowRepository
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.remote.response.msg.Msg
import gaur.himanshu.august.kilogram.util.Events
import gaur.himanshu.august.kilogram.util.Result
import gaur.himanshu.august.kilogram.util.Status
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FollowViewModel @Inject constructor(private val kilogramApi: KilogramApi,private val repository:IFollowRepository) : ViewModel() {

    private val userId = MutableLiveData<String>()
    private val indicators = MutableLiveData<FollowIndicators>()


    private val _follows= MutableLiveData<Events<Result<Msg>>>()
    val follows: LiveData<Events<Result<Msg>>> = _follows

    private val _unFollows= MutableLiveData<Events<Result<Msg>>>()
    val unFollows: LiveData<Events<Result<Msg>>> = _unFollows


    val follow = indicators.switchMap { followIndicators ->
        userId.switchMap {
            if (followIndicators == FollowIndicators.FOLLOWER) {
                Pager(PagingConfig(pageSize = 4)) {
                    FollowPagingSource(kilogramApi, followIndicators, it)
                }.liveData.cachedIn(viewModelScope)
            } else {
                Pager(PagingConfig(pageSize = 4)) {
                    FollowPagingSource(kilogramApi, followIndicators, it)
                }.liveData.cachedIn(viewModelScope)
            }
        }

    }





    fun postInicators(followIndicators: FollowIndicators) {
        indicators.postValue(followIndicators)
    }

    fun postUserId(userId: String) {
        this.userId.postValue(userId)
    }

    fun followUser(otherUserID:String)=viewModelScope.launch {
        _follows.postValue(Events(Result(Status.LOADING,null,null)))
        _follows.postValue(Events(repository.followUser(otherUserID)))
    }

    fun unFollowUser(otherUserID:String)=viewModelScope.launch {
        _unFollows.postValue(Events(Result(Status.LOADING,null,null)))
        _unFollows.postValue(Events(repository.unFollowUser(otherUserID)))
    }
}