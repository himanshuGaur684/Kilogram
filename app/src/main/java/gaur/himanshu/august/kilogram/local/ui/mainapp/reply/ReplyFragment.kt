package gaur.himanshu.august.kilogram.local.ui.mainapp.reply

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
import gaur.himanshu.august.kilogram.R
import gaur.himanshu.august.kilogram.databinding.FragmentReolyBinding
import gaur.himanshu.august.kilogram.local.ui.mainapp.reply.paging.ReplyPagingAdapter
import gaur.himanshu.august.kilogram.remote.response.reply.postreply.PostReply
import gaur.himanshu.august.kilogram.util.Status

class ReplyFragment(private val preferences: SharedPreferences) :
    Fragment(R.layout.fragment_reoly) {

    lateinit var viewModel: ReplyViewModel
    lateinit var binding: FragmentReolyBinding

    val args by navArgs<ReplyFragmentArgs>()

    val replyAdapter = ReplyPagingAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentReolyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(requireActivity()).get(ReplyViewModel::class.java)
        args.commentId?.let {
            viewModel.postCommentId(it)
        }


        setRecyclerView()

        replyAdapter.onDeleteReplyListener {
            viewModel.deleteReply(it.reply_id)
        }

        viewModel.reply.observe(viewLifecycleOwner) {
            replyAdapter.submitData(lifecycle, it)
        }


        binding.replyToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.postReply.setOnClickListener {
            binding.root.hideKeyboard()
            val reply = binding.postReplyTextEditText.text.toString().trim()
            if (reply.isEmpty()) {
                return@setOnClickListener
            }
            viewModel.postReply(args.commentId!!, PostReply(reply))
        }

        replyAdapter.addLoadStateListener { it->
            when(it.refresh){
                is LoadState.Loading->{
                    binding.replyFragmentToolbarProgress.visibility = View.VISIBLE
                }
               is LoadState.Error->{
                   binding.replyFragmentToolbarProgress.visibility = View.GONE
               }
                is LoadState.NotLoading->{
                    binding.replyFragmentToolbarProgress.visibility = View.GONE
                }
            }

        }


    }

    private fun setRecyclerView() {
        binding.replyRecyclerView.apply {
            adapter = replyAdapter
        }

        viewModel.replyDelete.observe(viewLifecycleOwner) {
            when (it.getContentIfNotHandled()?.status) {
                Status.LOADING -> {
                    binding.replyFragmentToolbarProgress.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    binding.root.snack("Error")
                    binding.replyFragmentToolbarProgress.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    binding.root.snack("success")
                    binding.postReplyTextEditText.setText("")
                    binding.replyFragmentToolbarProgress.visibility = View.GONE
                    viewModel.postCommentId(args.commentId!!)

                }
            }
        }

        viewModel.replyPost.observe(viewLifecycleOwner) {
            when (it.getContentIfNotHandled()?.status) {
                Status.LOADING -> {
                    binding.replyFragmentToolbarProgress.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    binding.root.snack("Error")
                    binding.replyFragmentToolbarProgress.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    binding.root.snack("success")
                    binding.replyFragmentToolbarProgress.visibility = View.GONE
                    viewModel.postCommentId(args.commentId!!)

                }
            }
        }

    }

}