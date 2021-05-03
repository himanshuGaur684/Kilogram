package gaur.himanshu.august.kilogram.local.ui.mainapp.serach.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import gaur.himanshu.august.kilogram.databinding.ViewHolderSearchBinding
import gaur.himanshu.august.kilogram.remote.response.search.SearchUser

class SearchPagingAdapter : PagingDataAdapter<SearchUser, SearchPagingAdapter.MyViewHolder>(
    DIFF_UTIL
) {
    var onClickListener: ((SearchUser) -> Unit)? = null

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<SearchUser>() {
            override fun areItemsTheSame(oldItem: SearchUser, newItem: SearchUser): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: SearchUser, newItem: SearchUser): Boolean {
                return oldItem == newItem
            }
        }
    }

    fun onClickListenerOfListItem(listener:(SearchUser)->Unit){
        listener.let {
            onClickListener=it
        }
    }

    inner class MyViewHolder(val viewDataBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        val binding: ViewHolderSearchBinding = viewDataBinding as ViewHolderSearchBinding
    }

    override fun onBindViewHolder(holder: SearchPagingAdapter.MyViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.search = item

   

        holder.binding.root.setOnClickListener {
            onClickListener?.let {
                it(item!!)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchPagingAdapter.MyViewHolder {
        val binding =
            ViewHolderSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
}