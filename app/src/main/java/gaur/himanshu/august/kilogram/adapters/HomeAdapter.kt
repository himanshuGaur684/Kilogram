package gaur.himanshu.august.kilogram.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gaur.himanshu.august.kilogram.BR
import gaur.himanshu.august.kilogram.R
import gaur.himanshu.august.kilogram.databinding.BlogViewHolderBinding
import gaur.himanshu.august.kilogram.remote.response.blog.Result

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {

    var list = listOf<Result>()

    private var onLikeClickListener: ((Result) -> Unit?)? =null

    fun submitList(list: List<Result>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class MyViewHolder(val viewDataBinding: BlogViewHolderBinding) :
        RecyclerView.ViewHolder(viewDataBinding.root) {

        var binding: BlogViewHolderBinding = viewDataBinding


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.MyViewHolder {
        val binding =
            BlogViewHolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeAdapter.MyViewHolder, position: Int) {
        val item = list[position]

        holder.viewDataBinding.setVariable(BR.blog, item)
        holder.binding.blogLike.setOnClickListener {
            if (item.like_by_me) {
                item.like_by_me = false
                holder.binding.blogLike.setImageResource(R.drawable.punch)
                item.likes--
                holder.binding.postViewholderLikeCount.text = item.likes.toString()
                onLikeClickListener?.let {
                    it(item)
                }
            } else {
                item.like_by_me = true
                holder.binding.blogLike.setImageResource(R.drawable.color_punch)
                item.likes++
                holder.binding.postViewholderLikeCount.text = item.likes.toString()
                onLikeClickListener?.let {
                    it(item)
                }

            }
        }
    }
    fun onLikeClickListener(listener:(Result)->Unit){
        onLikeClickListener=listener
    }

    override fun getItemCount(): Int {
        return this.list.size
    }
}