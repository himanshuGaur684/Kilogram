package gaur.himanshu.august.kilogram.local.ui.mainapp.comment.repository

import gaur.himanshu.august.kilogram.local.ui.mainapp.home.paging.HomeBlog
import gaur.himanshu.august.kilogram.remote.response.comment.post.PostCommentResponse
import gaur.himanshu.august.kilogram.remote.response.msg.Msg
import gaur.himanshu.august.kilogram.util.Result

interface ICommentRepository {

    suspend fun postComment(postId:String,comment:String):Result<PostCommentResponse>


    suspend fun deleteComment(commentId:String):Result<Msg>

    suspend fun updateHomeBlog(homeBlog: HomeBlog)

    suspend fun insertNewComment(comment:gaur.himanshu.august.kilogram.remote.response.comments.Comment)

    suspend fun deleteLocalComment(commentId: String)

}