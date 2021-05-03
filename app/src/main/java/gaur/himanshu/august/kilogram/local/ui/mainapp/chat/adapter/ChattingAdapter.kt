package gaur.himanshu.august.kilogram.local.ui.mainapp.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import gaur.himanshu.august.kilogram.databinding.ViewHolderRecievedItemBinding
import gaur.himanshu.august.kilogram.databinding.ViewHolderSentItemsBinding
import gaur.himanshu.august.kilogram.remote.response.chat.Chat


private const val TYPE_MESSAGE_SENT = 0
private const val TYPE_MESSAGE_RECEIVED = 1
private const val TYPE_IMAGE_SENT = 2
private const val TYPE_IMAGE_RECEIVED = 3


class ChattingAdapter() : RecyclerView.Adapter<ChattingAdapter.MyViewHolder>() {


     var messages = mutableListOf<Chat>()


    fun setChattingAdapterData(list:List<Chat>){
        this.messages=list.toMutableList()
        notifyDataSetChanged()
    }

    inner class MyViewHolder(val viewDataBinding: ViewDataBinding?) :
        RecyclerView.ViewHolder(viewDataBinding!!.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return when (viewType) {

            TYPE_MESSAGE_SENT -> {
                val binding =
                    ViewHolderSentItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MyViewHolder(binding)
            }

            TYPE_MESSAGE_RECEIVED -> {
                val binding =
                    ViewHolderRecievedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MyViewHolder(binding)
            }

            else -> {
                MyViewHolder(null)
            }

        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val message = messages[position]
        if (message.isSent) {
            if (message.message.isNotEmpty()) {
                holder.viewDataBinding?.setVariable(BR.message, message)
            }
        } else {
            if (message.message.isNotEmpty()) {
                holder.viewDataBinding?.setVariable(BR.message, message)
            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
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

    fun addItem(chat: Chat) {
        messages.add(chat)
        notifyItemInserted(messages.size - 1)
    }
}