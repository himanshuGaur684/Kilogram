package gaur.himanshu.august.kilogram.local.ui.mainapp.comment.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import gaur.himanshu.august.kilogram.databinding.CommentViewHolderBinding
import gaur.himanshu.august.kilogram.remote.response.comments.Comment

class CommentPagingAdapter() :
    PagingDataAdapter<Comment, CommentPagingAdapter.MyViewHolder>(DIFF_UTIL) {

    var onDeleteCommentListener: ((Comment) -> Unit)? = null

    var showAllReplies: ((Comment) -> Unit)? = null

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class MyViewHolder(val viewDataBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {


        val binding: CommentViewHolderBinding =
            (viewDataBinding as CommentViewHolderBinding)

    }


    fun onDeleteClickListener(listener: (Comment) -> Unit) {
        onDeleteCommentListener = listener
    }

    fun showAllReply(listener: (Comment) -> Unit) {
        showAllReplies = listener
    }

    override fun onBindViewHolder(holder: CommentPagingAdapter.MyViewHolder, position: Int) {

        val item = getItem(position)


        holder.binding.root.setOnLongClickListener {
            onDeleteCommentListener?.let {
                it(item!!)
            }
            return@setOnLongClickListener true
        }

        holder.binding.root.setOnClickListener {
            showAllReplies?.let {
                it(item!!)
            }
        }

//        holder.binding.commentShowReplies.setOnClickListener {
//            showAllReplies?.let {
//                it(item!!)
//            }
//        }

        holder.binding.comment = item


    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CommentPagingAdapter.MyViewHolder {
        val binding =
            CommentViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)

    }
}