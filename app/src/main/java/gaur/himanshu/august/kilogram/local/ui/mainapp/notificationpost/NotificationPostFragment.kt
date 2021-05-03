package gaur.himanshu.august.kilogram.local.ui.mainapp.notificationpost

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import gaur.himanshu.august.kilogram.databinding.FragmentNotificationPostBinding
import gaur.himanshu.august.kilogram.util.Status
import gaur.himanshu.august.kilogram.util.snack
import gaur.himanshu.august.kilogram.util.toast


class NotificationPostFragment : Fragment() {


    lateinit var binding: FragmentNotificationPostBinding
    lateinit var viewModel: NotificationPostViewModel

    val args: NotificationPostFragmentArgs by navArgs()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity()).get(NotificationPostViewModel::class.java)
        binding = FragmentNotificationPostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val postId = args.postId

        postId?.let {
            viewModel.getSingleBlog(it)
        }

        binding.notificationPostToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

    binding.navigationPostSwipeRefresh.setOnRefreshListener {
        binding.navigationPostSwipeRefresh.isRefreshing=false

        viewModel.getSingleBlog(postId!!)
    }


        viewModel.blog.observe(viewLifecycleOwner) {
            when (it.peekContent().status) {

                Status.LOADING -> {
                  binding.navigationPostProgress.visibility=View.VISIBLE
                }
                Status.SUCCESS -> {
                    binding.navigationPostProgress.visibility=View.GONE
                    val data=it.peekContent().data

                    binding.blog = data
                    binding.apply {
                        blogComment.setOnClickListener {
                            val action= NotificationPostFragmentDirections.actionNotificationPostFragmentToCommentFragment(postId = data!!._id)
                            findNavController().navigate(action)
                        }
                    }

                }
                Status.ERROR -> {
                    binding.navigationPostProgress.visibility=View.GONE
                    requireContext().toast("Error occurred")
                }
                else -> {

                }
            }
        }


    }

}