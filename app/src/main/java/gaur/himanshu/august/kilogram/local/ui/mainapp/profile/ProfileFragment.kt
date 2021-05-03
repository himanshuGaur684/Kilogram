package gaur.himanshu.august.kilogram.local.ui.mainapp.profile

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import gaur.himanshu.august.kilogram.Constants
import gaur.himanshu.august.kilogram.R
import gaur.himanshu.august.kilogram.databinding.FragmentProfileBinding
import gaur.himanshu.august.kilogram.local.ui.mainapp.follow.FollowIndicators
import gaur.himanshu.august.kilogram.local.ui.mainapp.profile.viewpager.ProfileViewPagerAdapter
import gaur.himanshu.august.kilogram.local.ui.mainapp.profile.viewpager.fragment.SaveBlogFragment
import gaur.himanshu.august.kilogram.local.ui.mainapp.profile.viewpager.fragment.postfragment.PostFragment
import gaur.himanshu.august.kilogram.util.Status


class ProfileFragment(private val sharedPreferences: SharedPreferences) :
    Fragment(R.layout.fragment_profile) {

    lateinit var viewModel: ProfileViewModel
    lateinit var binding: FragmentProfileBinding

    val args: ProfileFragmentArgs by navArgs()


    val v by activityViewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)

        checkArgs()

        setViewPager()
        followOrUnFollow()
        binding.followingLinearLayout.setOnClickListener {
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToFollowFragment2(
                    FollowIndicators.FOLLOWING.name,
                    args.userId ?: sharedPreferences.getString(Constants.USER_ID, "")
                )
            )
        }

        binding.bindingFollowerLinearLayout.setOnClickListener {
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileFragmentToFollowFragment2(
                    FollowIndicators.FOLLOWER.name,
                    args.userId ?: sharedPreferences.getString(Constants.USER_ID, "")
                )
            )
        }



        viewModel.profile.observe(viewLifecycleOwner) {
            when (it.peekContent().status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    val data = it.peekContent().data

                    Log.d("TAG", "onViewCreated: ${data}")
                    if (it.peekContent().data?.user?._id == sharedPreferences.getString(
                            Constants.USER_ID,
                            ""
                        )
                    ) {
                        binding.profileFollowButton.visibility = View.GONE
                    } else {
                        if (data?.followed_by_me == true) {
                            binding.profileFollowButton.text = "Following"
                        }
                        binding.profileFollowButton.visibility = View.VISIBLE
                    }

                    binding.user = data?.user
                }
                Status.ERROR -> {
                }
            }
        }


    }


    private fun followOrUnFollow() {

        args.userId?.let { otherUserId ->


            binding.profileFollowButton.setOnClickListener {
                binding.profileFollowButton.apply {
                    if (text == "Following") {
                        viewModel.unFollowUser(otherUserId)
                    }
                    if (text == "Follow") {
                        viewModel.followUser(otherUserId)
                    }
                }
            }

            viewModel.unFollows.observe(viewLifecycleOwner) {
                when (it.peekContent().status) {
                    Status.ERROR -> {
                        binding.profileFollowButton.isEnabled = true
                    }
                    Status.LOADING -> {
                        binding.profileFollowButton.isEnabled = false
                    }
                    Status.SUCCESS -> {
                        binding.profileFollowButton.isEnabled = true
                        binding.profileFollowButton.text = "Follow"
                    }
                }
            }

            viewModel.follows.observe(viewLifecycleOwner) {
                when (it.peekContent().status) {
                    Status.ERROR -> {
                        binding.profileFollowButton.isEnabled = true
                    }
                    Status.LOADING -> {
                        binding.profileFollowButton.isEnabled = false
                    }
                    Status.SUCCESS -> {
                        binding.profileFollowButton.isEnabled = true
                        binding.profileFollowButton.text = "Following"
                    }
                }
            }

        }
    }

    private fun checkArgs() {
        if (args.userId == null) {
            val id = sharedPreferences.getString(Constants.USER_ID, "").toString()
            viewModel.postUserId(id)
            viewModel.getProfileDetails(id)
        } else {
            args.userId?.let {
                viewModel.postUserId(it)
                viewModel.getProfileDetails(it)
            }
        }

    }

    fun setViewPager() {
        val viewPagerAdapter =
            ProfileViewPagerAdapter(
                requireActivity().supportFragmentManager,
                lifecycle,
                args.userId,
                viewModel
            )
        binding.profileViewpager.adapter = viewPagerAdapter
        TabLayoutMediator(
            binding.profileTabLayout,
            binding.profileViewpager
        ) { tab, position ->
            when (position) {
                0 -> {
                    tab.setIcon(R.drawable.ic_my_blog)
                    PostFragment(args.userId, viewModel)
                }
                1 -> {
                    tab.setIcon(R.drawable.ic_save)
                    SaveBlogFragment()
                }
                else -> {
                    Fragment()
                }
            }


        }.attach()
    }


    override fun onResume() {
        super.onResume()


        viewModel.profile.observe(viewLifecycleOwner) {
            when (it.peekContent().status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    val data = it.peekContent().data

                    if (it.peekContent().data?.user?._id == sharedPreferences.getString(
                            Constants.USER_ID,
                            ""
                        )
                    ) {
                        binding.profileFollowButton.visibility = View.GONE
                    } else {
                        binding.profileFollowButton.visibility = View.VISIBLE
                    }

                    binding.user = data?.user
                }
                Status.ERROR -> {
                }
            }
        }
    }

}