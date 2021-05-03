package gaur.himanshu.august.kilogram.util

import gaur.himanshu.august.kilogram.local.ui.mainapp.chat.model.Message
import gaur.himanshu.august.kilogram.local.ui.mainapp.home.paging.HomeBlog
import gaur.himanshu.august.kilogram.local.ui.mainapp.profile.viewpager.fragment.postfragment.paging.PersonalBlog
import gaur.himanshu.august.kilogram.remote.response.blog.Result
import gaur.himanshu.august.kilogram.remote.response.chat.Chat

fun HomeBlog.toResultMapper(): Result {
    return Result(
        this.__v,
        this._id,
        this.comment,
        this.createdAt,
        this.image,
        this.like_by_me,
        this.likes,
        this.profileimage,
        this.title,
        this.user,
        this.username,
        this.discription,
        this.liked_by,
    )
}

fun Result.toHomeBlog(): HomeBlog {
    return HomeBlog(
        this.__v,
        this._id,
        this.comment,
        this.createdAt,
        this.image,
        this.like_by_me,
        this.likes,
        this.profileimage,
        this.title,
        this.user,
        this.username,
        this.discription,
        this.liked_by,
    )
}


fun PersonalBlog.toHomeBlog(): HomeBlog {
    return HomeBlog(
        this.__v,
        this._id,
        this.comment,
        this.createdAt,
        this.image,
        this.like_by_me,
        this.likes,
        this.profileimage,
        this.title,
        this.user,
        this.username,
        this.discription,
        this.liked_by,
    )
}

fun PersonalBlog.toResult(): Result {
    return Result(
        this.__v,
        this._id,
        this.comment,
        this.createdAt,
        this.image,
        this.like_by_me,
        this.likes,
        this.profileimage,
        this.title,
        this.user,
        this.username,
        this.discription,
        this.liked_by,
    )
}

fun Chat.toMessage(userId: String): Message {

    return Message(
        username = this.sending_username,
        isSent = userId == this.sending_user,
        user_id = "",
        message = this.message
    )

}


