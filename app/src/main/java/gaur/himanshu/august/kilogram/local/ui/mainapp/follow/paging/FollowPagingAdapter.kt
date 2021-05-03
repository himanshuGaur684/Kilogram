package gaur.himanshu.august.kilogram.local.ui.mainapp.follow.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import gaur.himanshu.august.kilogram.databinding.ViewHolderFollowBinding
import gaur.himanshu.august.kilogram.local.ui.mainapp.follow.FollowIndicators
import gaur.himanshu.august.kilogram.local.ui.mainapp.follow.repository.IFollowRepository
import gaur.himanshu.august.kilogram.remote.response.follow.Follow
import gaur.himanshu.august.kilogram.util.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FollowPagingAdapter (val indicators: String,private val repository:IFollowRepository): PagingDataAdapter<Follow, FollowPagingAdapter.MyViewHolder>(DIFF_UTIL) {




    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<Follow>() {
            override fun areItemsTheSame(oldItem: Follow, newItem: Follow): Boolean {
                return oldItem.follower == newItem.follower
            }

            override fun areContentsTheSame(oldItem: Follow, newItem: Follow): Boolean {
                return oldItem == newItem
            }
        }
    }


    inner class MyViewHolder(val viewDataBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {
        val binding: ViewHolderFollowBinding = (viewDataBinding as ViewHolderFollowBinding)
    }

    override fun onBindViewHolder(holder: FollowPagingAdapter.MyViewHolder, position: Int) {
        val item=getItem(position)
        holder.binding.follow = item


        if(indicators==FollowIndicators.FOLLOWER.name){
            holder.binding.followUnfollowInFollowViewHolder.text= "Follow"
        }else{
            holder.binding.followUnfollowInFollowViewHolder.text= "Unfollow"
        }

        holder.binding.followUnfollowInFollowViewHolder.setOnClickListener {
            if( holder.binding.followUnfollowInFollowViewHolder.text== "Follow"){
                item?.let {
                    followUser(item,holder.binding,item.user_id)
                }

            }else if(holder.binding.followUnfollowInFollowViewHolder.text== "Unfollow"){
                item?.let {
                    unfollowUser(item,holder.binding,item.user_id)
                }
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FollowPagingAdapter.MyViewHolder {
        val binding =
            ViewHolderFollowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)

    }

    private fun followUser(item:Follow,binding: ViewHolderFollowBinding,userId:String){

            CoroutineScope(IO).launch {
                val response= repository.followUser(item.user_id)
                if(response.status == Status.SUCCESS){
                    withContext(Main){
                        binding.followUnfollowInFollowViewHolder.text="Following"
                    }
                }
            }


    }

    private fun unfollowUser(item:Follow,binding: ViewHolderFollowBinding,otherUserId:String){
        CoroutineScope(IO).launch {
            val response= repository.unFollowUser(item.user_id)
            if(response.status == Status.SUCCESS){
                withContext(Main){
                    binding.followUnfollowInFollowViewHolder.text="Follow"
                }
            }
        }
    }
}