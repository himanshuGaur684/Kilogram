package gaur.himanshu.august.kilogram.local.ui.mainapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import gaur.himanshu.august.kilogram.databinding.LoadStateAdapterBinding

class AdapterLoadState(private val retry:()->Unit) : LoadStateAdapter<AdapterLoadState.MyViewHolder>() {

    inner class MyViewHolder(val binding: LoadStateAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

    init {

        binding.refreshButton.setOnClickListener {
            retry.invoke()
        }
    }
    }


    override fun onBindViewHolder(holder: AdapterLoadState.MyViewHolder, loadState: LoadState) {
        holder.binding.apply {
            this.refreshButton.isVisible= loadState !is LoadState.Loading
            this.refreshProgressBar.isVisible= loadState is LoadState.Loading
            this.refreshButton.isVisible=loadState is LoadState.NotLoading
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): AdapterLoadState.MyViewHolder {
        val binding =
            LoadStateAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
}