package gaur.himanshu.august.kilogram.local.ui.mainapp.home.paging

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


class HomeMediaterPagingAdapter(private val context: Context, private val classname: String) :
    PagingDataAdapter<HomeBlog, HomeMediaterPagingAdapter.MyViewHolder>(DIFF_UTIL) {

    var onLikeClickListener: ((HomeBlog) -> Unit?)? = null

    var onCommentClickListener: ((HomeBlog) -> Unit)? = null

    var saveBlogOffline: ((HomeBlog) -> Unit)? = null
    var deleteBlogOffline: ((HomeBlog) -> Unit)? = null

    var onSeeUserDetails: ((HomeBlog) -> Unit)? = null

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<HomeBlog>() {
            override fun areItemsTheSame(oldItem: HomeBlog, newItem: HomeBlog): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: HomeBlog, newItem: HomeBlog): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class MyViewHolder(private val viewDataBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {

        val binding: BlogViewHolderBinding = viewDataBinding as BlogViewHolderBinding

    }

    fun onLikeClickListener(listener: (HomeBlog) -> Unit) {
        onLikeClickListener = listener
    }

    fun onDeleteOfflineBlog(listener: (HomeBlog) -> Unit) {
        deleteBlogOffline = listener
    }

    fun saveBlogOffline(listener: (HomeBlog) -> Unit) {
        saveBlogOffline = listener
    }

    fun onCommentClickListener(listener: (HomeBlog) -> Unit) {
        onCommentClickListener = listener
    }

    fun onSeeUserDetails(listener: (HomeBlog) -> Unit) {
        onSeeUserDetails = listener
    }


    override fun onBindViewHolder(holder: HomeMediaterPagingAdapter.MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.blog = item


        if (classname == SaveBlogFragment::class.java.name) {
            holder.binding.ll2.visibility = View.GONE
        }


        item?.let { items ->

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
    ): HomeMediaterPagingAdapter.MyViewHolder {
        val binding =
            BlogViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    // show the user menu to delete or saving offline blog
    private fun showMenu(items: HomeBlog, holder: MyViewHolder, position: Int) {
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