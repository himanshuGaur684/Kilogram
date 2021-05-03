package gaur.himanshu.august.kilogram.local.room.kilogram

import androidx.paging.PagingSource
import androidx.room.*
import gaur.himanshu.august.kilogram.local.ui.mainapp.home.paging.HomeBlog
import gaur.himanshu.august.kilogram.local.ui.mainapp.profile.viewpager.fragment.postfragment.paging.PersonalBlog
import gaur.himanshu.august.kilogram.remote.response.blog.Result
import gaur.himanshu.august.kilogram.remote.response.chat.Chat
import gaur.himanshu.august.kilogram.remote.response.chatsearch.ChatUser
import gaur.himanshu.august.kilogram.remote.response.comments.Comment
import gaur.himanshu.august.kilogram.remote.response.discover.Discover
import gaur.himanshu.august.kilogram.remote.response.notification.Notification
import gaur.himanshu.august.kilogram.remote.response.search.SearchUser
import kotlinx.coroutines.flow.Flow

@Dao
interface KilogramDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(blog: Result)

    @Query("SELECT *  FROM Result")
    fun getAllCachedResult(): PagingSource<Int, Result>


    @Delete
    suspend fun deleteOfflineBlog(result: Result)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlogList(list: List<HomeBlog>)

    @Query("SELECT * FROM HomeBlog ORDER BY createdAt DESC")
    fun getAllSaveBlogs(): PagingSource<Int, HomeBlog>

    @Query("DELETE FROM HOMEBLOG")
    suspend fun deleteAllBlogs()

    @Update
    suspend fun updateHomeBlog(updateHomeBlog: HomeBlog)

    // Search

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(list: List<SearchUser>)

    @Query("SELECT * FROM SearchUser ")
    fun getAllSeachUser(): PagingSource<Int, SearchUser>

    @Query("DELETE FROM SearchUser")
    suspend fun deleteAllSearchedUser()

    // Discover

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiscoverList(list: List<Discover>)

    @Query("SELECT * FROM Discover")
    fun getAllDiscoverList(): PagingSource<Int, Discover>

    @Query("DELETE FROM Discover")
    suspend fun deleteAllDiscoverList()

    @Update
    suspend fun updateDiscover(discover: Discover)

    // Personal posts in profile

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersonalBlogs(list: List<PersonalBlog>)

    @Query("SELECT * FROM PersonalBlog ")
    fun getAllPersonalBlogs(): PagingSource<Int, PersonalBlog>

    @Query("DELETE FROM PersonalBlog")
    suspend fun deletePersonalBlogs()

    // Comment

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleComment(comment: Comment)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllComment(list: List<Comment>)

    @Query("SELECT * FROM Comment ")
    fun getAllComments(): PagingSource<Int, Comment>

    @Query("DELETE FROM Comment")
    suspend fun deleteAllFromComment()

    @Query("DELETE FROM Comment WHERE _id=:commentId")
    suspend fun deleteSingleComment(commentId: String)

    // Notification

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListNotification(list: List<Notification>)

    @Query("SELECT * FROM Notification")
    fun getNotification(): PagingSource<Int, Notification>

    @Query("DELETE FROM Notification")
    suspend fun deleteAllNotification()

    //-----------------------------chat-------------------------//

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatUser(chatUser: ChatUser)

    @Delete
    suspend fun deleteChatUser(chatUser: ChatUser)

    @Query("SELECT * FROM ChatUser")
    fun getAllChatUsers(): PagingSource<Int, ChatUser>

    //----------------------------chats-------------------------//

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertChats(list: List<Chat>)


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSingleChat(chat: Chat)

    @Query("DELETE FROM Chat")
    suspend fun deleteAllChat()

    @Query("SELECT * FROM Chat ")
    fun getAllChats(): PagingSource<Int, Chat>

    @Query("SELECT * FROM Chat ORDER BY date ASC")
    fun getAllChatFlows(): Flow<List<Chat>>

}