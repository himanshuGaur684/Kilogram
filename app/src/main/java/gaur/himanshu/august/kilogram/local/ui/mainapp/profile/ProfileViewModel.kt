package gaur.himanshu.august.kilogram.local.ui.mainapp.profile

import android.content.SharedPreferences
import androidx.lifecycle.*
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import gaur.himanshu.august.kilogram.local.room.kilogram.KilogramDao
import gaur.himanshu.august.kilogram.local.room.kilogram.RemoteDao
import gaur.himanshu.august.kilogram.local.ui.mainapp.follow.repository.IFollowRepository
import gaur.himanshu.august.kilogram.local.ui.mainapp.profile.repository.IProfileRepository
import gaur.himanshu.august.kilogram.local.ui.mainapp.profile.viewpager.fragment.postfragment.paging.PersonalPostsRemoteMediator
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.remote.response.blog.BlogResponseTwo
import gaur.himanshu.august.kilogram.remote.response.msg.Msg
import gaur.himanshu.august.kilogram.remote.response.profile.ProfileResponse
import gaur.himanshu.august.kilogram.util.Events
import gaur.himanshu.august.kilogram.util.Result
import gaur.himanshu.august.kilogram.util.Status
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: IProfileRepository,
    private val followRepository:IFollowRepository,
    private val kilogramApi: KilogramApi,
    private val sharedPreferences: SharedPreferences,
    private val kilogramDao: KilogramDao,
    private val remoteDao: RemoteDao
) :
    ViewModel() {


    private val userId=MutableLiveData<String>("")


    @ExperimentalPagingApi
    val personalBlogs= userId.switchMap {
        Pager(PagingConfig(pageSize = 4),remoteMediator = PersonalPostsRemoteMediator(1,it,kilogramDao,remoteDao,kilogramApi)){
          kilogramDao.getAllPersonalBlogs()
        }.liveData.cachedIn(viewModelScope)
    }

    val saveBlog= Pager(PagingConfig(pageSize = 2)){
        kilogramDao.getAllCachedResult()
    }.liveData.cachedIn(viewModelScope)




    private val _profile = MutableLiveData<Events<Result<ProfileResponse>>>()
    val profile: LiveData<Events<Result<ProfileResponse>>> = _profile

    private val _blogs = MutableLiveData<Events<Result<BlogResponseTwo>>>()
    val blogs: LiveData<Events<Result<BlogResponseTwo>>> = _blogs


    private val _follows= MutableLiveData<Events<Result<Msg>>>()
    val follows: LiveData<Events<Result<Msg>>> = _follows

    private val _unFollows= MutableLiveData<Events<Result<Msg>>>()
    val unFollows: LiveData<Events<Result<Msg>>> = _unFollows


    fun postUserId(s:String){
        userId.postValue(s)
    }


    fun getProfileDetails(user_id: String) = viewModelScope.launch {
        _profile.postValue(Events(Result(Status.LOADING, null, null)))
        _profile.postValue(Events(repository.profileDetails(user_id)))
    }

    fun delteteSaveBlog(result:gaur.himanshu.august.kilogram.remote.response.blog.Result)=viewModelScope.launch {
        repository.deleteOfflineBlog(result)
    }

    fun followUser(otherUserID:String)=viewModelScope.launch {
        _follows.postValue(Events(Result(Status.LOADING,null,null)))
        _follows.postValue(Events(followRepository.followUser(otherUserID)))
    }

    fun unFollowUser(otherUserID:String)=viewModelScope.launch {
        _unFollows.postValue(Events(Result(Status.LOADING,null,null)))
        _unFollows.postValue(Events(followRepository.unFollowUser(otherUserID)))
    }


}