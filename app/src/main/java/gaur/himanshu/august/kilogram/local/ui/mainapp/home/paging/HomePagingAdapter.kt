package gaur.himanshu.august.kilogram.local.ui.mainapp.home.paging

import android.content.Context
import android.view.*
import android.widget.PopupMenu
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import gaur.himanshu.august.kilogram.R
import gaur.himanshu.august.kilogram.databinding.BlogViewHolderBinding
import gaur.himanshu.august.kilogram.local.ui.mainapp.home.HomeFragment
import gaur.himanshu.august.kilogram.local.ui.mainapp.profile.viewpager.fragment.postfragment.PostFragment
import gaur.himanshu.august.kilogram.local.ui.mainapp.profile.viewpager.fragment.SaveBlogFragment
import gaur.himanshu.august.kilogram.remote.response.blog.Result
import gaur.himanshu.august.kilogram.util.toHomeBlog

class HomePagingAdapter(private val context: Context, private val classname: String) :
    PagingDataAdapter<Result, HomePagingAdapter.MyViewHolder>(DIFF_UTIL) {

    var onLikeClickListener: ((Result) -> Unit?)? = null

    var onCommentClickListener: ((Result) -> Unit)? = null

    var saveBlogOffline: ((Result) -> Unit)? = null
    var deleteBlogOffline: ((Result) -> Unit)? = null

    var onSeeUserDetails: ((Result) -> Unit)? = null

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Result>() {
            override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class MyViewHolder(private val viewDataBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {

        val binding: BlogViewHolderBinding = viewDataBinding as BlogViewHolderBinding

    }

    fun onLikeClickListener(listener: (Result) -> Unit) {
        onLikeClickListener = listener
    }

    fun onDeleteOfflineBlog(listener: (Result) -> Unit) {
        deleteBlogOffline = listener
    }

    fun saveBlogOffline(listener: (Result) -> Unit) {
        saveBlogOffline = listener
    }

    fun onCommentClickListener(listener: (Result) -> Unit) {
        onCommentClickListener = listener
    }

    fun onSeeUserDetails(listener: (Result) -> Unit) {
        onSeeUserDetails = listener
    }


    override fun onBindViewHolder(holder: HomePagingAdapter.MyViewHolder, position: Int) {
        val item = getItem(position)



        if (classname == SaveBlogFragment::class.java.name) {
            holder.binding.ll2.visibility = View.GONE
        }


        item?.let { items ->
            holder.binding.blog = item.toHomeBlog()
            holder.binding.apply {
                blogLike.setOnClickListener {
                    if (items.like_by_me) {
                        items.like_by_me = false
                        holder.binding.blogLike.setImageResource(R.drawable.punch)
                        items.likes--
                        holder.binding.postViewholderLikeCount.text = items.likes.toString()
                        onLikeClickListener?.let {
                            it(items)
                        }
                    } else {
                        items.like_by_me = true
                        holder.binding.blogLike.setImageResource(R.drawable.color_punch)
                        items.likes++
                        holder.binding.postViewholderLikeCount.text = items.likes.toString()
                        onLikeClickListener?.let {
                            it(items)
                        }

                    }
                }

                // POPUP Menu
                postViewholderSaveBlog.setOnClickListener {
                    showMenu(items, holder, position)
                }
                // Profile details
                ll1.setOnClickListener {
                    onSeeUserDetails?.let {
                        it(items)
                    }
                }

                blogComment.setOnClickListener {
                    onCommentClickListener?.let {
                        it(items)
                    }
                }
            }


        }


    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomePagingAdapter.MyViewHolder {
        val binding =
            BlogViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    // show the user menu to delete or saving offline blog
    private fun showMenu(items: Result, holder: MyViewHolder, position: Int) {
        val popUp = PopupMenu(context, holder.binding.postViewholderSaveBlog)
        if (classname == HomeFragment::class.java.name) {
            popUp.menuInflater.inflate(R.menu.popup_menu_2, popUp.menu)
        }
        if (classname == PostFragment::class.java.name) {
            popUp.menuInflater.inflate(R.menu.popup_menu, popUp.menu)
        }
        if (classname == SaveBlogFragment::class.java.name) {
            popUp.menuInflater.inflate(R.menu.popup_menu_3, popUp.menu)
        }
        popUp.setOnMenuItemClickListener {
            return@setOnMenuItemClickListener when (it.itemId) {
                R.id.ProfilePostFragmentDelete -> {
                    true
                }
                R.id.ProfilePostFragmentSaveOffline -> {
                    saveBlogOffline?.let {
                        it(items)
                    }
                    true
                }
                R.id.homeFragmentSaveOffline -> {
                    saveBlogOffline?.let {
                        it(items)
                    }
                    true
                }
                R.id.saveBlogFragmentDelete -> {
                    notifyItemRemoved(position)
                    deleteBlogOffline?.let {
                        it(items)
                    }
                    true
                }
                else -> {
                    false
                }
            }
        }
        popUp.show()
    }
}