package gaur.himanshu.august.kilogram.local.ui.mainapp.notification.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import gaur.himanshu.august.kilogram.local.room.kilogram.KilogramDao
import gaur.himanshu.august.kilogram.local.room.kilogram.RemoteDao
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.remote.response.notification.Notification
import java.io.InvalidObjectException

@ExperimentalPagingApi
class NotificationRemoteMediator(
    private val initialPage: Int = 1,
    private val kilogramDao: KilogramDao,
    private val remoteDao: RemoteDao,
    private val kilogramApi: KilogramApi
) : RemoteMediator<Int, Notification>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Notification>
    ): MediatorResult {

        try {

            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKey = getClosest(state)
                    remoteKey?.nextKey?.minus(1) ?: initialPage
                }
                LoadType.PREPEND -> {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    val remoteKey = getLast(state) ?: throw  InvalidObjectException("Invalid")
                    remoteKey.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)

                }
            }


            val response = kilogramApi.getAllNotification(page, 10)

            if (response.isSuccessful) {
                val endOfPaginationReached =
                    response.body()?.notifications?.notification?.size!! < state.config.pageSize

                response.body()?.let {
                    if (loadType == LoadType.REFRESH) {
                        remoteDao.deleteNotificationRemoteKey()
                        kilogramDao.deleteAllNotification()
                    }

                    val prevKey = if (page == initialPage) null else page - 1
                    val nextKey = if (endOfPaginationReached) null else page + 1

                    val keys = it.notifications.notification.map {
                        NotificationRemoteKey(it._id, prevKey, nextKey)
                    }

                    remoteDao.insertAllNotificationRemoteKey(keys)
                    kilogramDao.insertListNotification(it.notifications.notification)
                }
            }

        } catch (e: Exception) {
            MediatorResult.Error(e)
        }

        return MediatorResult.Success(endOfPaginationReached = true)
    }


    private suspend fun getLast(state: PagingState<Int, Notification>): NotificationRemoteKey? {
        return state.lastItemOrNull()?.let {
            remoteDao.getNotificationRemoteKey(it._id)
        }
    }

    private suspend fun getClosest(state: PagingState<Int, Notification>): NotificationRemoteKey? {
        return state.anchorPosition?.let {
            state.closestItemToPosition(it)?.let {
                remoteDao.getNotificationRemoteKey(it._id)
            }
        }
    }
}