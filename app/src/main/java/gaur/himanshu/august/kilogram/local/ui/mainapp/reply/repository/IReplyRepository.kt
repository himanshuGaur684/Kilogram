package gaur.himanshu.august.kilogram.local.ui.mainapp.reply.repository

import gaur.himanshu.august.kilogram.remote.response.reply.postreply.PostReply
import gaur.himanshu.august.kilogram.remote.response.reply.replydelete.ReplyDeleteResponse
import gaur.himanshu.august.kilogram.util.Result

interface IReplyRepository {


    suspend fun deleteReply(replyId: String): Result<ReplyDeleteResponse>
    suspend fun postReply(postReply: PostReply, comment_id: String): Result<ReplyDeleteResponse>

}