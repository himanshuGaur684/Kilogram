package gaur.himanshu.august.kilogram.local.ui.mainapp.profile.viewpager.fragment.postfragment

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import gaur.himanshu.august.kilogram.Constants
import gaur.himanshu.august.kilogram.R
import gaur.himanshu.august.kilogram.databinding.FragmentPostBinding
import gaur.himanshu.august.kilogram.local.ui.mainapp.AdapterLoadState
import gaur.himanshu.august.kilogram.local.ui.mainapp.home.HomeViewModel
import gaur.himanshu.august.kilogram.local.ui.mainapp.home.paging.HomeMediaterPagingAdapter
import gaur.himanshu.august.kilogram.local.ui.mainapp.profile.ProfileViewModel
import gaur.himanshu.august.kilogram.local.ui.mainapp.profile.viewpager.fragment.postfragment.paging.PersonalBlogPagingAdapter
import gaur.himanshu.august.kilogram.util.toResult
import gaur.himanshu.august.kilogram.util.toResultMapper


class PostFragment(private val args: String?, private val viewModel: ProfileViewModel) :
    Fragment() {


    lateinit var binding: FragmentPostBinding


    private lateinit var homeViewModel: HomeViewModel

    lateinit var homeMediaterAdapter: PersonalBlogPagingAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostBinding.inflate(inflater, container, false)
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        return binding.root

    }

    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setRecyclerView()



        homeMediaterAdapter.saveBlogOffline {
            homeViewModel.saveBlogOffline(it.toResult())
        }

        viewModel.personalBlogs.observe(viewLifecycleOwner) {
              homeMediaterAdapter.submitData(lifecycle,it)
        }

        binding.postFragmentSwipeRefreshLayout.setOnRefreshListener {
            binding.postFragmentSwipeRefreshLayout.isRefreshing = false
            val pref = requireContext().getSharedPreferences(Constants.PREF_NAME, MODE_PRIVATE)
            homeMediaterAdapter.refresh()
        }

        // Comment details
        homeMediaterAdapter.onCommentClickListener {
            findNavController().navigate(R.id.commentFragment, bundleOf("post_id" to it._id))
        }
        // like manuplation
        homeMediaterAdapter.onLikeClickListener {
            if (it.like_by_me) {
                homeViewModel.postLike(it._id)
            } else {
                homeViewModel.deleteLike(it._id)
            }
        }

        homeMediaterAdapter.saveBlogOffline {
            homeViewModel.saveBlogOffline(it.toResult())
        }



    }

    override fun onResume() {
        super.onResume()

    }


    private fun setRecyclerView() {
        homeMediaterAdapter =
            PersonalBlogPagingAdapter(requireContext(), PostFragment::class.java.name)
        binding.postFragmentRecycler.apply {
            adapter = homeMediaterAdapter.withLoadStateHeaderAndFooter(
                header = AdapterLoadState { homeMediaterAdapter.retry() },
                footer = AdapterLoadState { homeMediaterAdapter.retry() }
            )
        }
    }

}