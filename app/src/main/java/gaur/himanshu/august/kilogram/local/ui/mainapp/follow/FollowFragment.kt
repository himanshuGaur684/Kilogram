package gaur.himanshu.august.kilogram.local.ui.mainapp.follow

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import gaur.himanshu.august.kilogram.Constants
import gaur.himanshu.august.kilogram.R
import gaur.himanshu.august.kilogram.databinding.FragmentFollowBinding
import gaur.himanshu.august.kilogram.local.ui.mainapp.follow.paging.FollowPagingAdapter
import gaur.himanshu.august.kilogram.local.ui.mainapp.follow.repository.IFollowRepository
import gaur.himanshu.august.kilogram.util.toast


class FollowFragment(
    private val sharedPreferences: SharedPreferences,
    private val repository: IFollowRepository
) : Fragment(
    R.layout.fragment_follow
) {

    lateinit var viewModel: FollowViewModel
    lateinit var binding: FragmentFollowBinding
    val args by navArgs<FollowFragmentArgs>()
    private lateinit var followAdapter: FollowPagingAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity()).get(FollowViewModel::class.java)



        binding.followToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        if (args.indicators == FollowIndicators.FOLLOWING.name) {
            viewModel.postInicators(FollowIndicators.FOLLOWING)
            binding.followToolbar.title = "Following"
        } else {
            viewModel.postInicators(FollowIndicators.FOLLOWER)
            binding.followToolbar.title = "Follower"
        }
        followAdapter = FollowPagingAdapter(args.indicators!!, repository)

        followAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.Loading -> {
                    binding.followProgressBar.visibility = View.VISIBLE
                }
                is LoadState.Error -> {
                    binding.followProgressBar.visibility = View.GONE
                    requireContext().toast("Error occured")
                }
                is LoadState.NotLoading -> {
                    binding.followProgressBar.visibility = View.GONE
                }
            }
        }

        sharedPreferences.getString(Constants.USER_ID, "")?.let { viewModel.postUserId(args.userId!!) }



        setRecyclerView()

        viewModel.follow.observe(viewLifecycleOwner) {
            followAdapter.submitData(lifecycle, it)
        }
    }

    fun setRecyclerView() {
        binding.followRecyclerView.apply {
            adapter = followAdapter
        }
    }

}