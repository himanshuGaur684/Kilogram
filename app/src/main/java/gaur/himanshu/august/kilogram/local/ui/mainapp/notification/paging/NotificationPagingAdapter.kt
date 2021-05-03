package gaur.himanshu.august.kilogram.local.ui.mainapp.notification.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import gaur.himanshu.august.kilogram.databinding.ViewHolderNotificationBinding
import gaur.himanshu.august.kilogram.remote.response.notification.Notification

class NotificationPagingAdapter :
    PagingDataAdapter<Notification, NotificationPagingAdapter.MyViewHolder>(DIFF_UTIL) {


    private var onNotificationClick : ((Notification)->Unit)?=null


    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Notification>() {
            override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class MyViewHolder(val viewDataBinding: ViewHolderNotificationBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root)



    fun onNotificationClick(listener:(Notification)->Unit){
        onNotificationClick= listener
    }

    override fun onBindViewHolder(holder: NotificationPagingAdapter.MyViewHolder, position: Int) {
        holder.viewDataBinding.notification = getItem(position)

        holder.viewDataBinding.root.setOnClickListener {
            onNotificationClick?.let {
                it(getItem(position)!!)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationPagingAdapter.MyViewHolder {
        val binding = ViewHolderNotificationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }
}