package gaur.himanshu.august.kilogram.local.ui.mainapp.discover.repository

import gaur.himanshu.august.kilogram.local.room.kilogram.KilogramDao
import gaur.himanshu.august.kilogram.remote.response.discover.Discover

class DiscoverRepository(private val kilogramDao: KilogramDao) : IDiscoverRepository {
    override suspend fun updateDiscover(discover: Discover) {
        kilogramDao.updateDiscover(discover)
    }
}