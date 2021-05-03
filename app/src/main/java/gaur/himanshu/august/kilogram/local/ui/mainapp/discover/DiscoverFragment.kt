package gaur.himanshu.august.kilogram.local.ui.mainapp.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import gaur.himanshu.august.kilogram.R
import gaur.himanshu.august.kilogram.databinding.FragmentDiscoverBinding
import gaur.himanshu.august.kilogram.local.ui.mainapp.AdapterLoadState
import gaur.himanshu.august.kilogram.local.ui.mainapp.discover.paging.DiscoverPagingAdapter
import gaur.himanshu.august.kilogram.local.ui.mainapp.follow.repository.IFollowRepository
import gaur.himanshu.august.kilogram.util.toast

class DiscoverFragment(private val followRepository: IFollowRepository) :
    Fragment(R.layout.fragment_discover) {


    lateinit var binding: FragmentDiscoverBinding
    lateinit var viewModel: DiscoverViewModel

    private val discoverAdapter = DiscoverPagingAdapter(followRepository)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        return binding.root
    }


    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity()).get(DiscoverViewModel::class.java)
        setRecyclerView()

        binding.discoverSwipeRefreshLayout.setOnRefreshListener {
            binding.discoverSwipeRefreshLayout.isRefreshing = false

        }

        discoverAdapter.onClickListener {
            findNavController().navigate(
                DiscoverFragmentDirections.actionDiscoverFragmentToProfileFragment(
                    it._id
                )
            )
        }

        discoverAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.Error -> {
                    binding.discoverFragmentProgressBar.visibility = View.GONE
                }
                is LoadState.NotLoading -> {
                    binding.discoverFragmentProgressBar.visibility = View.GONE
                }
                is LoadState.Loading -> {
                    binding.discoverFragmentProgressBar.visibility = View.VISIBLE
                }
            }
        }
        viewModel.discover.observe(viewLifecycleOwner) {
            discoverAdapter.submitData(lifecycle, it)
        }

    }

    private fun setRecyclerView() {
        binding.discoverRecycler.apply {
            adapter = discoverAdapter.withLoadStateHeaderAndFooter(
                header = AdapterLoadState { discoverAdapter.retry() },
                footer = AdapterLoadState { discoverAdapter.retry() }
            )
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

}