package gaur.himanshu.august.kilogram.local.room.kilogram

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import gaur.himanshu.august.kilogram.local.ui.mainapp.chat.paging.ChatRemoteKey
import gaur.himanshu.august.kilogram.local.ui.mainapp.comment.paging.CommentRemoteKey
import gaur.himanshu.august.kilogram.local.ui.mainapp.discover.paging.DiscoverRemoteKey
import gaur.himanshu.august.kilogram.local.ui.mainapp.home.paging.BlogRemoteKey
import gaur.himanshu.august.kilogram.local.ui.mainapp.home.paging.HomeBlog
import gaur.himanshu.august.kilogram.local.ui.mainapp.notification.paging.NotificationRemoteKey
import gaur.himanshu.august.kilogram.local.ui.mainapp.profile.viewpager.fragment.postfragment.paging.PersonalBlog
import gaur.himanshu.august.kilogram.local.ui.mainapp.profile.viewpager.fragment.postfragment.paging.PersonalPostRemoteKey
import gaur.himanshu.august.kilogram.local.ui.mainapp.serach.paging.RemoteSearchUserKey
import gaur.himanshu.august.kilogram.remote.response.blog.Result
import gaur.himanshu.august.kilogram.remote.response.chat.Chat
import gaur.himanshu.august.kilogram.remote.response.chatsearch.ChatUser
import gaur.himanshu.august.kilogram.remote.response.comments.Comment
import gaur.himanshu.august.kilogram.remote.response.discover.Discover
import gaur.himanshu.august.kilogram.remote.response.notification.Notification
import gaur.himanshu.august.kilogram.remote.response.search.SearchUser

@Database(
    entities = [Result::class, HomeBlog::class,
        BlogRemoteKey::class, RemoteSearchUserKey::class,
        SearchUser::class, Discover::class,
        DiscoverRemoteKey::class, PersonalPostRemoteKey::class,
        PersonalBlog::class, CommentRemoteKey::class, Comment::class,
        Notification::class, NotificationRemoteKey::class, ChatUser::class,
        ChatRemoteKey::class, Chat::class],
    exportSchema = false,
    version = 13
)
abstract class KilogramDatabase : RoomDatabase() {


    companion object {
        fun get(context: Context): KilogramDatabase {
            return Room.databaseBuilder(context, KilogramDatabase::class.java, "kilo_db")
                .fallbackToDestructiveMigration().build()
        }
    }

    abstract fun getKilogramDao(): KilogramDao

    abstract fun getRemoteDao(): RemoteDao


}