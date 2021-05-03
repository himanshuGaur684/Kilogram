package gaur.himanshu.august.kilogram.local.ui.mainapp.reply

import androidx.lifecycle.*
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import gaur.himanshu.august.kilogram.local.ui.mainapp.reply.paging.ReplyPagingSource
import gaur.himanshu.august.kilogram.local.ui.mainapp.reply.repository.IReplyRepository
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.remote.response.reply.postreply.PostReply
import gaur.himanshu.august.kilogram.remote.response.reply.replydelete.ReplyDeleteResponse
import gaur.himanshu.august.kilogram.util.Events
import gaur.himanshu.august.kilogram.util.Result
import gaur.himanshu.august.kilogram.util.Status
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReplyViewModel @Inject constructor(
    private val kilogramApi: KilogramApi,
    private val repository: IReplyRepository
) : ViewModel() {

    private val comment_id = MutableLiveData<String>()

    val reply = comment_id.switchMap {
        Pager(PagingConfig(pageSize = 3)) {
            ReplyPagingSource(kilogramApi, it)
        }.liveData.cachedIn(viewModelScope)
    }


    private val _replyDelete = MutableLiveData<Events<Result<ReplyDeleteResponse>>>()
    val replyDelete: LiveData<Events<Result<ReplyDeleteResponse>>> = _replyDelete

    private val _replyPost = MutableLiveData<Events<Result<ReplyDeleteResponse>>>()
    val replyPost: LiveData<Events<Result<ReplyDeleteResponse>>> = _replyPost

    fun postCommentId(commentId: String) {
        comment_id.postValue(commentId)
    }

    fun deleteReply(reply_id: String) = viewModelScope.launch {
        _replyDelete.postValue(Events(Result(Status.LOADING, null, null)))
        _replyDelete.postValue(Events(repository.deleteReply(reply_id)))
    }

    fun postReply(commentId: String,postReply: PostReply) = viewModelScope.launch {
        _replyDelete.postValue(Events(Result(Status.LOADING, null, null)))
        _replyDelete.postValue(Events(repository.postReply(postReply,commentId)))
    }


}