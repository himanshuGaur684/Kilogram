package gaur.himanshu.august.kilogram.local.ui.mainapp.chat.search.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import gaur.himanshu.august.kilogram.remote.network.KilogramApi
import gaur.himanshu.august.kilogram.remote.response.chatsearch.ChatUser

class ChatSearchPagingSource(private val kilogramApi: KilogramApi, private val query: String) :
    PagingSource<Int, ChatUser>() {

    override fun getRefreshKey(state: PagingState<Int, ChatUser>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ChatUser> {
        return try {
            val position = params.key ?: 1;

            val response =
                kilogramApi.searchChatUser(query, position, 10)

            if (response.isSuccessful) {
                LoadResult.Page(
                    data = response.body()?.chat_users?.results!!,
                    prevKey = if (params.key == 1) null else position - 1,
                    nextKey = if (response.body()!!.chat_users.results.isEmpty()) null else position + 1
                )
            } else {
                LoadResult.Error(Exception("Nhi hua"))
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}