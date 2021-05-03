package gaur.himanshu.august.kilogram.local.ui.mainapp.chat.search.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import gaur.himanshu.august.kilogram.databinding.ViewHolderChatSearchBinding
import gaur.himanshu.august.kilogram.remote.response.chatsearch.ChatUser

class ChatSearchPagingAdapter : PagingDataAdapter<ChatUser, ChatSearchPagingAdapter.MyViewHolder>(
    DIFF_UTIL
) {

    var onClickListener: ((ChatUser) -> Unit)? = null

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<ChatUser>() {
            override fun areItemsTheSame(oldItem: ChatUser, newItem: ChatUser): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: ChatUser, newItem: ChatUser): Boolean {
                return oldItem == newItem
            }
        }
    }

    fun onClickListenerOfListItem(listener: (ChatUser) -> Unit) {
        listener.let {
            onClickListener = it
        }
    }

    inner class MyViewHolder(val viewDataBinding: ViewHolderChatSearchBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {

    }

    override fun onBindViewHolder(holder: ChatSearchPagingAdapter.MyViewHolder, position: Int) {
        val item = getItem(position)

        holder.viewDataBinding.chatuser = item



        holder.viewDataBinding.root.setOnClickListener {
            onClickListener?.let {
                it(item!!)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatSearchPagingAdapter.MyViewHolder {
        val binding =
            ViewHolderChatSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
}