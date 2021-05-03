package gaur.himanshu.august.kilogram.remote.response.comment.post

import gaur.himanshu.august.kilogram.remote.response.comments.Comment

data class PostCommentResponse(
    val comment: Comment,
    val msg: String,
    val success: Boolean
)