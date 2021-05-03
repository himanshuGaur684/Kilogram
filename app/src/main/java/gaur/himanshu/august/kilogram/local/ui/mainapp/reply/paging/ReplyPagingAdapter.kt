package gaur.himanshu.august.kilogram.local.ui.mainapp.reply.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import gaur.himanshu.august.kilogram.databinding.ViewholderReplyBinding
import gaur.himanshu.august.kilogram.remote.response.reply.Data

class ReplyPagingAdapter : PagingDataAdapter<Data, ReplyPagingAdapter.MyViewHolder>(DIFF_UTIL) {


    var onDeleteReplyListener: ((Data) -> Unit)? = null

    fun onDeleteReplyListener(listener: (Data) -> Unit) {
        onDeleteReplyListener = listener

    }


    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Data>() {
            override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
                return oldItem.reply_id == newItem.reply_id
            }

            override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class MyViewHolder(val viewDataBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {


        val binding: ViewholderReplyBinding =
            (viewDataBinding as ViewholderReplyBinding)

    }


    override fun onBindViewHolder(holder: ReplyPagingAdapter.MyViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.reply = item

        holder.binding.root.setOnLongClickListener {
            onDeleteReplyListener?.let {
                it(item!!)
            }
            return@setOnLongClickListener true
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReplyPagingAdapter.MyViewHolder {
        val binding =
            ViewholderReplyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
}