package gaur.himanshu.august.kilogram.local.ui.mainapp.profile.repository

import gaur.himanshu.august.kilogram.remote.response.profile.ProfileResponse
import gaur.himanshu.august.kilogram.util.Result

interface IProfileRepository {

    suspend fun profileDetails(userId: String): Result<ProfileResponse>

    suspend fun deleteOfflineBlog(result: gaur.himanshu.august.kilogram.remote.response.blog.Result)




}