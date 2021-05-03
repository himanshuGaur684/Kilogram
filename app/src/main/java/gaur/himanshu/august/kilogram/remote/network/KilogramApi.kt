package gaur.himanshu.august.kilogram.remote.network

import gaur.himanshu.august.kilogram.remote.body.LoginBody
import gaur.himanshu.august.kilogram.remote.response.LoginResponse
import gaur.himanshu.august.kilogram.remote.response.RegisterResponse
import gaur.himanshu.august.kilogram.remote.response.addblog.AddBlogResponse
import gaur.himanshu.august.kilogram.remote.response.blog.BlogResponse
import gaur.himanshu.august.kilogram.remote.response.chat.ChatsResponse
import gaur.himanshu.august.kilogram.remote.response.chatsearch.ChatResponse
import gaur.himanshu.august.kilogram.remote.response.comment.post.PostBody
import gaur.himanshu.august.kilogram.remote.response.comment.post.PostCommentResponse
import gaur.himanshu.august.kilogram.remote.response.comments.CommentsResponse
import gaur.himanshu.august.kilogram.remote.response.discover.DiscoverResponse
import gaur.himanshu.august.kilogram.remote.response.follow.FollowResponse
import gaur.himanshu.august.kilogram.remote.response.msg.Msg
import gaur.himanshu.august.kilogram.remote.response.notification.NotificationResponse
import gaur.himanshu.august.kilogram.remote.response.profile.ProfileResponse
import gaur.himanshu.august.kilogram.remote.response.profile.personalblogs.PersonalBlogResponse
import gaur.himanshu.august.kilogram.remote.response.reply.ReplyResponse
import gaur.himanshu.august.kilogram.remote.response.reply.postreply.PostReply
import gaur.himanshu.august.kilogram.remote.response.reply.replydelete.ReplyDeleteResponse
import gaur.himanshu.august.kilogram.remote.response.search.SearchResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface KilogramApi {

    @POST("kilogram/login")
    suspend fun login(@Body loginBody: LoginBody): Response<LoginResponse>

    @Multipart
    @POST("kilogram/register")
    suspend fun register(
        @Part image: MultipartBody.Part,
        @Part("username") userName: RequestBody,
        @Part("password") password: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("fcm_token") token: RequestBody
    ): Response<RegisterResponse>

//-----------------------------------BLOG-------------------------------------//


    @POST("kilogram")
    @Multipart
    suspend fun postBlog(
        @Part part: MultipartBody.Part,
        @Part("title") title: RequestBody,
        @Part("discription") discription: RequestBody
    ): Response<AddBlogResponse>


    @GET("kilogram")
    suspend fun getAllBlogs(
        @Query("page") pageNumber: Int,
        @Query("limit") limit: Int
    ): Response<BlogResponse>


    @GET("kilogram/blog/personal")
    suspend fun getAllBlogs(
        @Query("user_id") userId: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<PersonalBlogResponse>


    @GET("kilogram/singleblog/{post_id}")
    suspend fun getSingleBlog(@Path("post_id") post_id: String): Response<BlogResponse>

//-----------------------------ProfileDetails---------------------------//

    @GET("kilogram/user")
    suspend fun profileDetails(@Query("user_id") id: String): Response<ProfileResponse>

//--------------------------LIKE--------------------------------------//

    @DELETE("kilogram/likes/{post_id}")
    suspend fun deleteLike(@Path("post_id") post_id: String): Response<String>

    @POST("kilogram/likes/{post_id}")
    suspend fun postLike(@Path("post_id") post_id: String): Response<String>

    //---------------------------------Comments---------------------------------//

    @GET("kilogram/comments/blog")
    suspend fun getAllComments(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("postid") post_id: String
    ): Response<CommentsResponse>


    @POST("kilogram/comments/{post_id}")
    suspend fun postComments(
        @Path("post_id") postId: String,
        @Body postBody: PostBody
    ): Response<PostCommentResponse>

    @DELETE("kilogram/comments/{comment_id}")
    suspend fun deleteComment(@Path("comment_id") commentId: String): Response<Msg>

    //--------------------------------------Reply----------------------------------//

    @GET("kilogram/comments/reply")
    suspend fun getAllReply(
        @Query("page") page: Int,
        @Query("limit") limit: Int,
        @Query("comment_id") commentId: String
    ): Response<ReplyResponse>

    @DELETE("kilogram/reply/{reply_id}")
    suspend fun deleteReply(@Path("reply_id") replyId: String): Response<ReplyDeleteResponse>


    @POST("kilogram/reply/{comment_id}")
    suspend fun postReply(
        @Path("comment_id") commentId: String,
        @Body postReply: PostReply
    ): Response<ReplyDeleteResponse>

    //------------------------------ Follow Stuffs ---------------------------------------------//

    @GET("kilogram/follower/personal")
    suspend fun getAllOfMyFollowings(
        @Query("user_id") user_id: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<FollowResponse>

    @GET("kilogram/following/personal")
    suspend fun getAllOfMyFollowering(
        @Query("user_id") user_id: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<FollowResponse>

    @POST("kilogram/follow/{other_user_id}")
    suspend fun follow(@Path("other_user_id") user_id: String): Response<Msg>


    @DELETE("kilogram/unfollow/{other_user_id}")
    suspend fun unFollow(@Path("other_user_id") user_id: String): Response<Msg>

    //-------------------------------------  discover OR Search --------------------------------------------//


    @GET("kilogram/discover/users")
    suspend fun getAllDiscoverUser(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<DiscoverResponse>

    @GET("kilogram/search/users")
    suspend fun getAllSearchedResult(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<SearchResponse>

    //-------------------------------------Notification-------------------------//

    @GET("kilogram/notification/list")
    suspend fun getAllNotification(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<NotificationResponse>

    //-----------------------------------Chat search-------------------------------//

    @GET("kilogram/search/chat_user")
    suspend fun searchChatUser(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<ChatResponse>

    //-----------------------------------------Chat details--------------------------//

    @GET("kilogram/chat/chat_details")
    suspend fun getAllChats(
        @Query("sending_user")sender:String,
        @Query("receiving_user")receiver:String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<ChatsResponse>

}