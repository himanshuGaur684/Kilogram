package gaur.himanshu.august.kilogram.local.hilt

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import gaur.himanshu.august.kilogram.Constants
import gaur.himanshu.august.kilogram.local.room.kilogram.KilogramDao
import gaur.himanshu.august.kilogram.local.room.kilogram.KilogramDatabase
import gaur.himanshu.august.kilogram.local.room.kilogram.RemoteDao
import gaur.himanshu.august.kilogram.local.ui.auth.repository.AuthRepository
import gaur.himanshu.august.kilogram.local.ui.auth.repository.IAuthRepository
import gaur.himanshu.august.kilogram.local.ui.mainapp.addblog.respository.AddBlogRepository
import gaur.himanshu.august.kilogram.local.ui.mainapp.addblog.respository.IAddBlogRepository
import gaur.himanshu.august.kilogram.local.ui.mainapp.chat.repository.ChatRepository
import gaur.himanshu.august.kilogram.local.ui.mainapp.chat.search.repository.ChatSearchRepository
import gaur.himanshu.august.kilogram.local.ui.mainapp.chat.search.repository.IChatSearchRepository
import gaur.himanshu.august.kilogram.local.ui.mainapp.comment.repository.CommentRepossitory
import gaur.himanshu.august.kilogram.local.ui.mainapp.comment.repository.ICommentRepository
import gaur.himanshu.august.kilogram.local.ui.mainapp.discover.repository.DiscoverRepository
import gaur.himanshu.august.kilogram.local.ui.mainapp.discover.repository.IDiscoverRepository
import gaur.himanshu.august.kilogram.local.ui.mainapp.follow.repository.FollowRepository
import gaur.himanshu.august.kilogram.local.ui.mainapp.follow.repository.IFollowRepository
import gaur.himanshu.august.kilogram.local.ui.mainapp.home.repository.HomeRepository
import gaur.himanshu.august.kilogram.local.ui.mainapp.home.repository.IHomeRepository
import gaur.himanshu.august.kilogram.local.ui.mainapp.notificationpost.repository.INotificationPostRepository
import gaur.himanshu.august.kilogram.local.ui.mainapp.notificationpost.repository.NotificationPostRepository
import gaur.himanshu.august.kilogram.local.ui.mainapp.profile.repository.IProfileRepository
import gaur.himanshu.august.kilogram.local.ui.mainapp.profile.repository.ProfileRepository
import gaur.himanshu.august.kilogram.local.ui.mainapp.reply.repository.IReplyRepository
import gaur.himanshu.august.kilogram.local.ui.mainapp.reply.repository.ReplyRepository
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.remote.network.TodoIntercepters
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object HiltModules {


    @Provides
    fun provideSharedPref(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE)
    }


    @Provides
    fun provideAuthRepository(
        kilogramApi: KilogramApi,
        sharedPreferences: SharedPreferences
    ): IAuthRepository {
        return AuthRepository(kilogramApi, sharedPreferences)
    }


    @Provides
    fun providekilogramApi(retrofit: Retrofit): KilogramApi {
        return retrofit.create(KilogramApi::class.java)
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient)
            .build()
    }

    @Provides
    fun provideHttp(sharedPreferences: SharedPreferences): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        val token = sharedPreferences.getString(Constants.AUTH_TOKEN, "")!!
        Log.d("TAG", "provideHttp: ${token}")
        httpClient.addInterceptor(TodoIntercepters(token))
        return httpClient.build()
    }


    @Provides
    fun provideDatabase(@ApplicationContext context: Context): KilogramDatabase {
        return KilogramDatabase.get(context)
    }

    @Provides
    fun provideKilogramDao(db: KilogramDatabase): KilogramDao {
        return db.getKilogramDao()
    }

    @Provides
    fun provideRemoteDao(db: KilogramDatabase): RemoteDao {
        return db.getRemoteDao()
    }


    @Provides
    fun provideHomeRepository(kilogramApi: KilogramApi, kilogramDao: KilogramDao): IHomeRepository {
        return HomeRepository(kilogramApi, kilogramDao)
    }

    @Provides
    fun provideAddBlogRepository(kilogramApi: KilogramApi): IAddBlogRepository {
        return AddBlogRepository(kilogramApi)
    }

    @Provides
    fun provideProfileRespository(
        kilogramApi: KilogramApi,
        kilogramDao: KilogramDao
    ): IProfileRepository {
        return ProfileRepository(kilogramApi, kilogramDao)
    }

    @Provides
    fun provideCommentRepository(
        kilogramApi: KilogramApi,
        kilogramDao: KilogramDao
    ): ICommentRepository {
        return CommentRepossitory(kilogramApi, kilogramDao)
    }

    @Provides
    fun provideReplyRepository(kilogramApi: KilogramApi): IReplyRepository {
        return ReplyRepository(kilogramApi)
    }

    @Provides
    fun provideIFollowRepository(kilogramApi: KilogramApi): IFollowRepository {
        return FollowRepository(kilogramApi)
    }

    @Provides
    fun provideIDiscoverRepository(kilogramDao: KilogramDao): IDiscoverRepository {
        return DiscoverRepository(kilogramDao)
    }

    @Provides
    fun provideINotificationPostRepository(kilogramApi: KilogramApi): INotificationPostRepository {
        return NotificationPostRepository(kilogramApi)
    }

    @Provides
    fun provideSearchChatRepository(kilogramDao: KilogramDao): IChatSearchRepository {
        return ChatSearchRepository(kilogramDao)
    }

    @Provides
    fun provideChatRepository(
        kilogramApi: KilogramApi,
        remoteDao: RemoteDao,
        kilogramDao: KilogramDao,
        sharedPreferences: SharedPreferences
    ): ChatRepository {
        return ChatRepository(remoteDao,kilogramDao, kilogramApi, sharedPreferences)
    }
}