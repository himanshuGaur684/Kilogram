package gaur.himanshu.august.kilogram.local.room.kilogram

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import gaur.himanshu.august.kilogram.local.ui.mainapp.chat.paging.ChatRemoteKey
import gaur.himanshu.august.kilogram.local.ui.mainapp.comment.paging.CommentRemoteKey
import gaur.himanshu.august.kilogram.local.ui.mainapp.discover.paging.DiscoverRemoteKey
import gaur.himanshu.august.kilogram.local.ui.mainapp.home.paging.BlogRemoteKey
import gaur.himanshu.august.kilogram.local.ui.mainapp.notification.paging.NotificationRemoteKey
import gaur.himanshu.august.kilogram.local.ui.mainapp.profile.viewpager.fragment.postfragment.paging.PersonalPostRemoteKey
import gaur.himanshu.august.kilogram.local.ui.mainapp.serach.paging.RemoteSearchUserKey

@Dao
interface RemoteDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlogList(list: List<BlogRemoteKey>)

    @Query("SELECT * FROM BlogRemoteKey WHERE blogId = :blogId")
    suspend fun getRemoteKey(blogId: String): BlogRemoteKey?

    @Query("DELETE FROM BlogRemoteKey")
    suspend fun deleteBlogRemoteKeys()

    // Search section

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchKey(list: List<RemoteSearchUserKey>)

    @Query("SELECT * FROM RemoteSearchUserKey WHERE _id = :serachId")
    suspend fun getSearchRemoteKey(serachId: String): RemoteSearchUserKey

    @Query("DELETE FROM RemoteSearchUserKey")
    suspend fun deleteAllRemoteSearchUserKey()

    // Discover

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiscoverRemoteKeys(keys: List<DiscoverRemoteKey>)

    @Query("SELECT * FROM DiscoverRemoteKey WHERE _id = :id")
    suspend fun getDiscoverRemoteKey(id: String): DiscoverRemoteKey?

    @Query("DELETE FROM DiscoverRemoteKey")
    suspend fun deleteAllDiscoverKey()

    // Profile Personal Blogs [Post Section]

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPersonalBlogs(list: List<PersonalPostRemoteKey>)

    @Query("SELECT * FROM PersonalPostRemoteKey WHERE _id=:id")
    suspend fun getAllPersonalRemoteKey(id: String): PersonalPostRemoteKey?

    @Query("DELETE FROM PersonalPostRemoteKey")
    suspend fun deleteAllPersonalRemoteKey()


    // comment
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommentKey(list: List<CommentRemoteKey>)

    @Query("SELECT * FROM CommentRemoteKey WHERE _id = :id")
    suspend fun getAllCommentRemoteKey(id: String): CommentRemoteKey?

    @Query("DELETE FROM CommentRemoteKey")
    suspend fun deleteAllCommentRemoteKey()

    // Notification

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllNotificationRemoteKey(list: List<NotificationRemoteKey>)

    @Query("SELECT * FROM NotificationRemoteKey WHERE _id= :id")
    suspend fun getNotificationRemoteKey(id: String): NotificationRemoteKey?

    @Query("DELETE FROM NotificationRemoteKey")
    suspend fun deleteNotificationRemoteKey()

    // Chat

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatRemoteKey(list: List<ChatRemoteKey>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChatSingle(list: ChatRemoteKey)


    @Query("DELETE FROM ChatRemoteKey")
    suspend fun deleteAllChatRemoteKey()

    @Query("SELECT * FROM ChatRemoteKey WHERE id = :id")
    suspend fun getChatRemoteKey(id: String): ChatRemoteKey?

}