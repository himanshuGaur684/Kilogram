package gaur.himanshu.august.kilogram.local.ui.mainapp.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import gaur.himanshu.august.kilogram.R
import gaur.himanshu.august.kilogram.databinding.FragmentHomeBinding
import gaur.himanshu.august.kilogram.local.ui.mainapp.AdapterLoadState
import gaur.himanshu.august.kilogram.local.ui.mainapp.home.paging.HomeMediaterPagingAdapter
import gaur.himanshu.august.kilogram.util.toResultMapper
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HomeFragment : Fragment(R.layout.fragment_home) {


    lateinit var viewModel: HomeViewModel
    lateinit var binding: FragmentHomeBinding

    var position = 0
    lateinit var homeMediaterAdapter: HomeMediaterPagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        setRecyclerView()


//        homeMediaterAdapter.addLoadStateListener {
//            when(it.refresh){
//              is   LoadState.NotLoading->{
//                    homeMediaterAdapter.refresh()
//                }
//            }
//        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.blog.collectLatest {
                homeMediaterAdapter.submitData(it)
            }
        }


        binding.apply {

            mainFragmentSwipeRefreshLayout.setOnRefreshListener {
                binding.mainFragmentSwipeRefreshLayout.isRefreshing = false
                homeMediaterAdapter.refresh()
            }

            homeAddBlogCamera.setOnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToAddBlogFragment(
                        true
                    )
                )
            }

            homeAddBlog.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_addBlogFragment)
            }

            homeMessenger.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_userFragment)
            }
        }


    }


    private fun setRecyclerView() {
        homeMediaterAdapter =
            HomeMediaterPagingAdapter(requireContext(), HomeFragment::class.java.name)

        binding.homeRecycler.apply {
            scrollToPosition(0)
            adapter = homeMediaterAdapter.withLoadStateHeaderAndFooter(
                header = AdapterLoadState { homeMediaterAdapter.retry() },
                footer = AdapterLoadState { homeMediaterAdapter.retry() }
            )
            layoutManager = LinearLayoutManager(requireContext())
        }

        homeMediaterAdapter.onLikeClickListener {
            if (it.like_by_me) {
                viewModel.postLike(it._id)
            } else {
                viewModel.deleteLike(it._id)
            }
        }

        homeMediaterAdapter.onCommentClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToCommentFragment(
                    it._id, it
                )
            )
        }

        homeMediaterAdapter.saveBlogOffline {
            viewModel.saveBlogOffline(it.toResultMapper())
        }

        homeMediaterAdapter.onSeeUserDetails {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToProfileFragment(
                    it.user
                )
            )
        }
    }


    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()

    }


}