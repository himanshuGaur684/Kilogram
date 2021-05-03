package gaur.himanshu.august.kilogram.local.ui.mainapp.chat

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.ExperimentalPagingApi
import gaur.himanshu.august.kilogram.Constants
import gaur.himanshu.august.kilogram.R
import gaur.himanshu.august.kilogram.databinding.FragmentChatBinding
import gaur.himanshu.august.kilogram.local.ui.mainapp.ContainerActivity
import gaur.himanshu.august.kilogram.local.ui.mainapp.chat.adapter.ChattingAdapter
import gaur.himanshu.august.kilogram.local.ui.mainapp.chat.paging.ChatPagingAdapter
import gaur.himanshu.august.kilogram.local.ui.mainapp.chat.service.ChatService
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class ChatFragment(private val sharedPreferences: SharedPreferences) :
    Fragment(R.layout.fragment_chat) {


    val args by navArgs<ChatFragmentArgs>()

    lateinit var binding: FragmentChatBinding

    lateinit var chatService: ChatService
    lateinit var viewModel: ChatViewModel


    private val chatAdapter = ChattingAdapter()

    private val userId = sharedPreferences.getString(Constants.USER_ID, "").toString()

    private val chatPagingAdapter =
        ChatPagingAdapter()

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as ChatService.MyBinder
            chatService = binder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addCallback()
        setRecyclerView()
        setToolbar()
        val unique = userId + args.chatUsesr?._id
        viewModel = ViewModelProvider(requireActivity()).get(ChatViewModel::class.java)

        binding.sendMessage.setOnClickListener {
            val data = binding.sendMessageTextEditText.text.toString().trim()
            if (data.isEmpty()) {
                return@setOnClickListener
            }
            args.chatUsesr?.let { user ->
                chatService.sendMessage(
                    sharedPreferences.getString(Constants.USER_NAME, "").toString(), data, user._id
                )
            }
            binding.sendMessageTextEditText.setText("")
        }

        ChatService.chats.observe(viewLifecycleOwner) {
            it?.let {
                chatAdapter.addItem(it)
                binding.chatRecycler.smoothScrollToPosition(chatAdapter.messages.size - 1)
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getAllChatFlows().collectLatest {
                chatAdapter.setChattingAdapterData(it.toMutableList())
                if (chatAdapter.messages.size != 0) {
                    binding.chatRecycler.smoothScrollToPosition(chatAdapter.messages.size - 1)
                }
            }
        }
//        viewLifecycleOwner.lifecycleScope.launch {
//            viewModel.getChatRepository(unique).collectLatest {
//                // chatPagingAdapter.submitData(it)
//                viewModel.getAllChatFlows()
//            }
//        }
    }


    private fun setBinder() {
        val args = args.chatUsesr!!
        val intent = Intent(requireContext(), ChatService::class.java)
        intent.putExtra("service", args._id)
        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onStart() {
        super.onStart()
        setBinder()
    }


    fun initView() {
        ChatService.checkIsForeground=true
        (requireActivity() as ContainerActivity).binding.bottomNavigationView.visibility = View.GONE
    }

    fun deInitializeView() {
        ChatService.checkIsForeground=false
        (requireActivity() as ContainerActivity).binding.bottomNavigationView.visibility =
            View.VISIBLE
        findNavController().popBackStack()
    }

    private fun addCallback() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                deInitializeView()

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun setRecyclerView() {
        binding.chatRecycler.apply {
            adapter = chatAdapter
        }
    }

    private fun setToolbar() {
        binding.toolbarTitle = args.chatUsesr?.username
        binding.chatFragmentToolbar.setNavigationOnClickListener {
            deInitializeView()
        }
    }

    override fun onPause() {
        super.onPause()
        ChatService.checkIsForeground=false
    }
    override fun onResume() {
        super.onResume()
        ChatService.checkIsForeground=true
    }

    override fun onDestroy() {
        super.onDestroy()
        ChatService.checkIsForeground=true
    }

}