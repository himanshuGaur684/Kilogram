package gaur.himanshu.august.kilogram.local.ui.mainapp.addblog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import gaur.himanshu.august.kilogram.local.ui.mainapp.addblog.respository.IAddBlogRepository
import gaur.himanshu.august.kilogram.remote.response.addblog.AddBlogResponse
import gaur.himanshu.august.kilogram.util.Events
import gaur.himanshu.august.kilogram.util.Result
import gaur.himanshu.august.kilogram.util.Status
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class AddBlogViewModel @Inject constructor(
    private val repository: IAddBlogRepository
) : ViewModel() {


    private val _addBlogResponse = MutableLiveData<Events<Result<AddBlogResponse>>>(
    )
    val addBlog: LiveData<Events<gaur.himanshu.august.kilogram.util.Result<AddBlogResponse>>> =
        _addBlogResponse


    fun postBlog(part: MultipartBody.Part, title: RequestBody, discription: RequestBody) =
        viewModelScope.launch {
            _addBlogResponse.postValue(Events(Result(Status.LOADING, null, null)))
            _addBlogResponse.postValue(Events(repository.postBlog(part, title, discription)))
        }


}