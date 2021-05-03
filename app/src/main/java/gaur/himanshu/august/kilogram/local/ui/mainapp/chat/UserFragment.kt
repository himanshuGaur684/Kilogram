package gaur.himanshu.august.kilogram.local.ui.mainapp.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import gaur.himanshu.august.kilogram.R
import gaur.himanshu.august.kilogram.databinding.FragmentUserBinding
import gaur.himanshu.august.kilogram.local.ui.mainapp.chat.search.ChatSearchViewModel
import gaur.himanshu.august.kilogram.local.ui.mainapp.chat.search.paging.ChatSearchPagingAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserFragment : Fragment(R.layout.fragment_user) {


    lateinit var binding: FragmentUserBinding

    lateinit var viewModel: UserViewModel
    //lateinit var viewModel:ChatSearchViewModel
    val adapters = ChatSearchPagingAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
        setRecyclerView()
        binding.userSearch.setOnClickListener {
            findNavController().navigate(R.id.action_userFragment_to_chatSearchFragment)
        }


        viewLifecycleOwner.lifecycleScope.launch{
            viewModel.list.collectLatest {
                adapters.submitData(it)
            }
        }


        adapters.onClickListenerOfListItem {
            val action = UserFragmentDirections.actionUserFragmentToChatFragment(it)
            findNavController().navigate(action)
        }


    }

    private fun setRecyclerView() {
        binding.userRecycler.apply {
            adapter = adapters
        }
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onStart() {
        super.onStart()

    }
}