package gaur.himanshu.august.kilogram.local.ui.mainapp.discover.repository

import gaur.himanshu.august.kilogram.remote.response.discover.Discover

interface IDiscoverRepository {

    suspend fun updateDiscover(discover: Discover)
}