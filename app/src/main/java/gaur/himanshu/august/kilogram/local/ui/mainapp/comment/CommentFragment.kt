package gaur.himanshu.august.kilogram.local.ui.mainapp.comment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import gaur.himanshu.august.kilogram.databinding.FragmentCommentBinding
import gaur.himanshu.august.kilogram.local.ui.mainapp.ContainerActivity
import gaur.himanshu.august.kilogram.local.ui.mainapp.comment.paging.CommentPagingAdapter
import gaur.himanshu.august.kilogram.util.Status
import gaur.himanshu.august.kilogram.util.hideKeyboard
import gaur.himanshu.august.kilogram.util.toast

class CommentFragment(private val preferences: SharedPreferences) : Fragment() {

    lateinit var viewModel: CommentViewModel
    lateinit var binding: FragmentCommentBinding


    val args by navArgs<CommentFragmentArgs>()

    var adapter: CommentPagingAdapter = CommentPagingAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCommentBinding.inflate(inflater, container, false)
        initView()

        return binding.root

    }

    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity()).get(CommentViewModel::class.java)

        args.postId?.let {
            viewModel.postCommentsPostId(it)
        }

        binding.commentToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
            deInitializeView()
        }

        setRecyclerView()



        viewModel.data.observe(viewLifecycleOwner) {
            adapter.submitData(lifecycle, it)
        }



        adapter.onDeleteClickListener {
            viewModel.deleteComments(it._id)
        }


        adapter.showAllReply {
            findNavController().navigate(
                CommentFragmentDirections.actionCommentFragmentToReplyFragment(
                    it._id
                )
            )
        }

        binding.postComment.setOnClickListener {
            binding.root.hideKeyboard()
            val msg = binding.postCommentTextEditText.text.toString().trim()
            if (msg.isEmpty()) {
                return@setOnClickListener
            }
            viewModel.postComments(args.postId!!, msg)
        }


        adapter.addLoadStateListener { it ->
            when (it.refresh) {
                is LoadState.Loading -> {
                    binding.commmentFragmentToolbarProgress.visibility = View.VISIBLE
                }
                is LoadState.Error -> {
                    binding.commmentFragmentToolbarProgress.visibility = View.GONE
                }
                is LoadState.NotLoading -> {
                    binding.commmentFragmentToolbarProgress.visibility = View.GONE
                }
            }

        }



        viewModel.postComments.observe(viewLifecycleOwner) { it ->
            when (it.getContentIfNotHandled()?.status) {

                Status.LOADING -> {
                    binding.apply {
                        commmentFragmentToolbarProgress.visibility = View.VISIBLE
                        postComment.isEnabled = false
                    }

                }
                Status.ERROR -> {
                    binding.apply {
                        requireContext().toast("error occured")
                        commmentFragmentToolbarProgress.visibility = View.GONE
                        postComment.isEnabled = true
                    }

                }
                Status.SUCCESS -> {
                    args.homeBlog?.let {
                        it.comment = it.comment + 1
                        viewModel.updateHomeBlog(it)
                    }

                    binding.apply {
                        requireContext().toast("Comment posted")
                        commmentFragmentToolbarProgress.visibility = View.GONE
                        postCommentTextEditText.setText("")

                        it.peekContent().data?.let {
                            viewModel.insertComment(comment = it.comment)
                            viewModel.postCommentsPostId("")
                            commentRecyclerView.scrollToPosition(adapter.itemCount)
                            postComment.isEnabled = true
                        }

                    }

                }

            }
        }

        viewModel.deleteComments.observe(viewLifecycleOwner) {
            when (it?.getContentIfNotHandled()?.status) {
                Status.ERROR -> {
                    binding.apply {
                        requireContext().toast("error")
                        commmentFragmentToolbarProgress.visibility = View.GONE
                    }
                }
                Status.LOADING -> {
                    binding.apply {
                        commmentFragmentToolbarProgress.visibility = View.VISIBLE
                    }
                }
                Status.SUCCESS -> {
                    binding.apply {
                        requireContext().toast("Comment deleted")
                        commmentFragmentToolbarProgress.visibility = View.GONE
                        viewModel.deleteSingleComment(it.peekContent().data?.msg!!)
                        viewModel.postCommentsPostId("")
                    }
                }
            }
        }


        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
                deInitializeView()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

    }

    private fun setRecyclerView() {

        binding.commentRecyclerView.apply {

            adapter = this@CommentFragment.adapter
        }


    }

    fun initView() {
        (requireActivity() as ContainerActivity).binding.bottomNavigationView.visibility = View.GONE
    }

    fun deInitializeView() {
        (requireActivity() as ContainerActivity).binding.bottomNavigationView.visibility =
            View.VISIBLE
    }


}