package gaur.himanshu.august.kilogram.local.ui.mainapp.chat.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import gaur.himanshu.august.kilogram.databinding.ViewHolderRecievedItemBinding
import gaur.himanshu.august.kilogram.databinding.ViewHolderSentItemsBinding
import gaur.himanshu.august.kilogram.remote.response.chat.Chat

private const val TYPE_MESSAGE_SENT = 0
private const val TYPE_MESSAGE_RECEIVED = 1
private const val TYPE_IMAGE_SENT = 2
private const val TYPE_IMAGE_RECEIVED = 3

class ChatPagingAdapter() :
    PagingDataAdapter<Chat, ChatPagingAdapter.MyViewHolder>(DIFF_UTIL) {

    lateinit var message:Chat

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Chat>() {
            override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class MyViewHolder(val viewDataBinding: ViewDataBinding?) :
        RecyclerView.ViewHolder(viewDataBinding!!.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return when (viewType) {

            TYPE_MESSAGE_SENT -> {
                val binding =
                    ViewHolderSentItemsBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                MyViewHolder(binding)
            }

            TYPE_MESSAGE_RECEIVED -> {
                val binding =
                    ViewHolderRecievedItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                MyViewHolder(binding)
            }

            else -> {
                MyViewHolder(null)
            }

        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

         message = getItem(position)?:this.message
        message.let { messages ->
            if (messages.isSent) {
                if (messages.message.isNotEmpty()) {
                    holder.viewDataBinding?.setVariable(BR.message, message)
                }
            } else {
                if (messages.message.isNotEmpty()) {
                    holder.viewDataBinding?.setVariable(BR.message, message)
                }
            }
        }

    }

    override fun getItemViewType(position: Int): Int {

         message =getItem(position)?:this.message
            return if (message.isSent) {
                if (message.message.isNotEmpty()) {
                    TYPE_MESSAGE_SENT
                } else {
                    TYPE_IMAGE_SENT
                }

            } else {
                if (message.message.isNotEmpty()) {
                    TYPE_MESSAGE_RECEIVED
                } else {
                    TYPE_IMAGE_RECEIVED
                }
            }
        }

    }


