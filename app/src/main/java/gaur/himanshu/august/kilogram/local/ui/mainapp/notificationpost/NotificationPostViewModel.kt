package gaur.himanshu.august.kilogram.local.ui.mainapp.notificationpost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gaur.himanshu.august.kilogram.local.ui.mainapp.notificationpost.repository.INotificationPostRepository
import gaur.himanshu.august.kilogram.util.Events
import gaur.himanshu.august.kilogram.util.Result
import gaur.himanshu.august.kilogram.util.Status
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationPostViewModel @Inject constructor(
    private val repository: INotificationPostRepository
) : ViewModel() {


    private val _blog =
        MutableLiveData<Events<Result<gaur.himanshu.august.kilogram.remote.response.blog.Result>>>()
    val blog: LiveData<Events<Result<gaur.himanshu.august.kilogram.remote.response.blog.Result>>> =
        _blog


    fun getSingleBlog(postId: String) = viewModelScope.launch {
        _blog.postValue(Events(Result(Status.LOADING, null, null)))
        _blog.postValue(Events(repository.getSingleBlog(postId)))
    }


}