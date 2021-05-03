package gaur.himanshu.august.kilogram.local.ui.mainapp.profile.viewpager.fragment.postfragment.paging

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
import gaur.himanshu.august.kilogram.local.ui.mainapp.home.paging.HomeBlog
import gaur.himanshu.august.kilogram.local.ui.mainapp.profile.viewpager.fragment.SaveBlogFragment
import gaur.himanshu.august.kilogram.local.ui.mainapp.profile.viewpager.fragment.postfragment.PostFragment
import gaur.himanshu.august.kilogram.util.toHomeBlog


class PersonalBlogPagingAdapter(private val context: Context, private val classname: String) :
    PagingDataAdapter<PersonalBlog, PersonalBlogPagingAdapter.MyViewHolder>(DIFF_UTIL) {

    var onLikeClickListener: ((PersonalBlog) -> Unit?)? = null

    var onCommentClickListener: ((PersonalBlog) -> Unit)? = null

    var saveBlogOffline: ((PersonalBlog) -> Unit)? = null
    var deleteBlogOffline: ((PersonalBlog) -> Unit)? = null

    var onSeeUserDetails: ((PersonalBlog) -> Unit)? = null

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<PersonalBlog>() {
            override fun areItemsTheSame(oldItem: PersonalBlog, newItem: PersonalBlog): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: PersonalBlog, newItem: PersonalBlog): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class MyViewHolder(private val viewDataBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {

        val binding: BlogViewHolderBinding = viewDataBinding as BlogViewHolderBinding

    }

    fun onLikeClickListener(listener: (PersonalBlog) -> Unit) {
        onLikeClickListener = listener
    }

    fun onDeleteOfflineBlog(listener: (PersonalBlog) -> Unit) {
        deleteBlogOffline = listener
    }

    fun saveBlogOffline(listener: (PersonalBlog) -> Unit) {
        saveBlogOffline = listener
    }

    fun onCommentClickListener(listener: (PersonalBlog) -> Unit) {
        onCommentClickListener = listener
    }

    fun onSeeUserDetails(listener: (PersonalBlog) -> Unit) {
        onSeeUserDetails = listener
    }


    override fun onBindViewHolder(holder: PersonalBlogPagingAdapter.MyViewHolder, position: Int) {
        val item = getItem(position)



        if (classname == SaveBlogFragment::class.java.name) {
            holder.binding.ll2.visibility = View.GONE
        }


        item?.let { items ->
            holder.binding.blog = items.toHomeBlog()
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
    ): PersonalBlogPagingAdapter.MyViewHolder {
        val binding =
            BlogViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    // show the user menu to delete or saving offline blog
    private fun showMenu(items: PersonalBlog, holder: MyViewHolder, position: Int) {
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