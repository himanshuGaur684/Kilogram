package gaur.himanshu.august.kilogram.local.ui.mainapp.chat.search.repository

import gaur.himanshu.august.kilogram.local.room.kilogram.KilogramDao
import gaur.himanshu.august.kilogram.remote.response.chatsearch.ChatUser

class ChatSearchRepository(private val kilogramDao: KilogramDao) : IChatSearchRepository {

    override suspend fun insertChatUser(chatUser: ChatUser) {
       kilogramDao.insertChatUser(chatUser)
    }
}