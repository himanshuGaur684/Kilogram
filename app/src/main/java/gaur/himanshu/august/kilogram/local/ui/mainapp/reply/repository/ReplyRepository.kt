package gaur.himanshu.august.kilogram.local.ui.mainapp.reply.repository

import android.util.Log
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.remote.response.reply.postreply.PostReply
import gaur.himanshu.august.kilogram.remote.response.reply.replydelete.ReplyDeleteResponse
import gaur.himanshu.august.kilogram.util.Result
import gaur.himanshu.august.kilogram.util.Status

class ReplyRepository(private val kilogramApi: KilogramApi) : IReplyRepository {
    override suspend fun deleteReply(replyId: String): Result<ReplyDeleteResponse> {
        return try {
            val response = kilogramApi.deleteReply(replyId)
            Log.d("TAG", "deleteReply: ${replyId}")
            if (response.isSuccessful) {
                Result(Status.SUCCESS, response.body(), null)
            } else {
                Result(Status.ERROR, null, response.code().toString())
            }
        } catch (e: Exception) {
            Result(Status.ERROR, null, null)
        }
    }

    override suspend fun postReply(postReply: PostReply,comment_id: String): Result<ReplyDeleteResponse> {
        return try {
            val response = kilogramApi.postReply(comment_id,postReply)
            if (response.isSuccessful) {
                Result(Status.SUCCESS, response.body(), null)
            } else {
                Result(Status.ERROR, null, response.code().toString())
            }
        } catch (e: Exception) {
            Result(Status.ERROR, null, null)
        }
    }
}