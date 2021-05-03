package gaur.himanshu.august.kilogram.local.ui.mainapp.chat.search.repository

import gaur.himanshu.august.kilogram.remote.response.chatsearch.ChatUser

interface IChatSearchRepository {



    suspend fun insertChatUser(chatUser: ChatUser)


}