package gaur.himanshu.august.kilogram.local.ui.mainapp.comment

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import gaur.himanshu.august.kilogram.local.room.kilogram.KilogramDao
import gaur.himanshu.august.kilogram.local.room.kilogram.RemoteDao
import gaur.himanshu.august.kilogram.local.ui.mainapp.comment.paging.CommentRemoteMediator
import gaur.himanshu.august.kilogram.local.ui.mainapp.comment.repository.ICommentRepository
import gaur.himanshu.august.kilogram.local.ui.mainapp.home.paging.HomeBlog
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.remote.response.comment.post.PostCommentResponse
import gaur.himanshu.august.kilogram.remote.response.comments.Comment
import gaur.himanshu.august.kilogram.remote.response.msg.Msg
import gaur.himanshu.august.kilogram.util.Events
import gaur.himanshu.august.kilogram.util.Result
import gaur.himanshu.august.kilogram.util.Status
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommentViewModel @Inject constructor(
    private val remoteDao: RemoteDao,
    private val kilogramApi: KilogramApi,
    private val repository: ICommentRepository,
    private val kilogramDao: KilogramDao
) : ViewModel() {


    private val _postComments =
        MutableLiveData<Events<gaur.himanshu.august.kilogram.util.Result<PostCommentResponse>>>()
    val postComments: LiveData<Events<gaur.himanshu.august.kilogram.util.Result<PostCommentResponse>>> =
        _postComments

    private val _deleteComments =
        MutableLiveData<Events<gaur.himanshu.august.kilogram.util.Result<Msg>>>()
    val deleteComments: LiveData<Events<gaur.himanshu.august.kilogram.util.Result<Msg>>> =
        _deleteComments


    val comments = MutableLiveData<String>()


    @ExperimentalPagingApi
    val data = comments.switchMap {
        Pager(
            PagingConfig(pageSize = 4),
            remoteMediator = CommentRemoteMediator(it, 1, kilogramApi, kilogramDao, remoteDao)
        ) {
            kilogramDao.getAllComments()
            // CommentPagingSource(kilogramApi, it)
        }.liveData.cachedIn(viewModelScope)
    }


    fun postCommentsPostId(string: String) {
        comments.postValue(string)
    }

    fun postComments(postId: String, msg: String) = viewModelScope.launch {
        _postComments.postValue(Events(Result(Status.LOADING, null, null)))
        Log.d("TAG", "postComments: $msg")
        _postComments.postValue(Events(repository.postComment(postId, msg)))
    }

    fun deleteComments(commentId: String) = viewModelScope.launch {
        _deleteComments.postValue(Events(Result(Status.LOADING, null, null)))
        _deleteComments.postValue(Events(repository.deleteComment(commentId)))
    }

    fun updateHomeBlog(homeBlog: HomeBlog) = viewModelScope.launch {
        repository.updateHomeBlog(homeBlog)
    }

    fun insertComment(comment: Comment) = viewModelScope.launch {
        repository.insertNewComment(comment)
    }

    fun deleteSingleComment(commentId: String) = viewModelScope.launch {
        repository.deleteLocalComment(commentId)
    }

}