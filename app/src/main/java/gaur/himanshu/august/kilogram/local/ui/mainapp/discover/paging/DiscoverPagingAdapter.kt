package gaur.himanshu.august.kilogram.local.ui.mainapp.discover.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import gaur.himanshu.august.kilogram.databinding.ViewHolderDiscoverBinding
import gaur.himanshu.august.kilogram.local.ui.mainapp.follow.repository.IFollowRepository
import gaur.himanshu.august.kilogram.remote.response.discover.Discover
import gaur.himanshu.august.kilogram.util.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiscoverPagingAdapter(private val repository: IFollowRepository) : PagingDataAdapter<Discover, DiscoverPagingAdapter.MyViewHolder>(
    DIFF_UTIL
) {


    var onClickListener:((Discover)->Unit)?=null



    inner class MyViewHolder(val viewDataBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {

        val binding: ViewHolderDiscoverBinding = viewDataBinding as ViewHolderDiscoverBinding
    }


    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Discover>() {
            override fun areItemsTheSame(oldItem: Discover, newItem: Discover): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: Discover, newItem: Discover): Boolean {
                return oldItem == newItem
            }
        }
    }


    fun onClickListener(listener:(Discover)->Unit){
       onClickListener= listener
    }

    override fun onBindViewHolder(holder: DiscoverPagingAdapter.MyViewHolder, position: Int) {

        val item= getItem(position)

        holder.binding.discover = getItem(position)

        holder.binding.root.setOnClickListener {
            onClickListener?.let {
                it(item!!)
            }
        }

        holder.binding.discoverButtonFollow.setOnClickListener {
            if( holder.binding.discoverButtonFollow.text== "Follow"){
                item?.let {
                    followUser(item,holder.binding,item._id)
                }

            }else if(holder.binding.discoverButtonFollow.text== "Unfollow"){
                item?.let {
                    unfollowUser(item,holder.binding,item._id)
                }
            }
        }


    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DiscoverPagingAdapter.MyViewHolder {
        val binding =
            ViewHolderDiscoverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    private fun followUser(item: Discover, binding: ViewHolderDiscoverBinding, userId:String){

        CoroutineScope(Dispatchers.IO).launch {
            val response= repository.followUser(item._id)
            if(response.status == Status.SUCCESS){
                withContext(Dispatchers.Main){
                    binding.discoverButtonFollow.text="Following"
                }
            }
        }


    }

    private fun unfollowUser(item: Discover, binding: ViewHolderDiscoverBinding, otherUserId:String){
        CoroutineScope(Dispatchers.IO).launch {
            val response= repository.unFollowUser(item._id)
            if(response.status == Status.SUCCESS){
                withContext(Dispatchers.Main){
                    binding.discoverButtonFollow.text="Follow"
                }
            }
        }
    }
}