package gaur.himanshu.august.kilogram.local.ui.mainapp.comment.repository

import gaur.himanshu.august.kilogram.local.room.kilogram.KilogramDao
import gaur.himanshu.august.kilogram.local.ui.mainapp.home.paging.HomeBlog
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.remote.response.comment.post.PostBody
import gaur.himanshu.august.kilogram.remote.response.comment.post.PostCommentResponse
import gaur.himanshu.august.kilogram.remote.response.comments.Comment
import gaur.himanshu.august.kilogram.remote.response.msg.Msg
import gaur.himanshu.august.kilogram.util.Result
import gaur.himanshu.august.kilogram.util.Status

class CommentRepossitory(
    private val kilogramApi: KilogramApi,
    private val kilogramDao: KilogramDao
) : ICommentRepository {

    override suspend fun postComment(postId: String, comment: String): Result<PostCommentResponse> {
        return try {
            val postBody = PostBody(comment)
            val response = kilogramApi.postComments(postId, postBody)

            if (response.isSuccessful) {
                Result(Status.SUCCESS, response.body(), null)
            } else {
                Result(Status.ERROR, null, null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result(Status.ERROR, null, null)
        }
    }

    override suspend fun deleteComment(commentId: String): Result<Msg> {
        return try {

            val response = kilogramApi.deleteComment(commentId)
            if (response.isSuccessful) {
                Result(Status.SUCCESS, response.body(), null)
            } else {
                Result(Status.ERROR, null, null)
            }


        } catch (e: Exception) {
            e.printStackTrace()
            Result(Status.ERROR, null, null)
        }
    }

    override suspend fun updateHomeBlog(homeBlog: HomeBlog) {
        kilogramDao.updateHomeBlog(homeBlog)
    }

    override suspend fun insertNewComment(comment: Comment) {
kilogramDao.insertSingleComment(comment)
    }

    override suspend fun deleteLocalComment(commentId: String) {
        kilogramDao.deleteSingleComment(commentId)
    }
}