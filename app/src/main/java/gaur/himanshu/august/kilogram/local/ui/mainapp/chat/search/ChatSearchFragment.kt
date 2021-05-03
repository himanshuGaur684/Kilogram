package gaur.himanshu.august.kilogram.local.ui.mainapp.chat.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import gaur.himanshu.august.kilogram.R
import gaur.himanshu.august.kilogram.databinding.FragmentSearchChatBinding
import gaur.himanshu.august.kilogram.local.ui.mainapp.chat.search.paging.ChatSearchPagingAdapter

class ChatSearchFragment : Fragment(R.layout.fragment_search_chat) {


    lateinit var viewModel: ChatSearchViewModel
    lateinit var binding: FragmentSearchChatBinding

    private val searchChatAdapter = ChatSearchPagingAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchChatBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity()).get(ChatSearchViewModel::class.java)
        setRecyclerView()
        viewModel.list.observe(viewLifecycleOwner) {
            it?.let {
                searchChatAdapter.submitData(lifecycle, it)
            }
        }


        binding.chatUserSearchToolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        searchChatAdapter.onClickListenerOfListItem {
            viewModel.insertChatUser(it)
            findNavController().popBackStack()
        }
        binding.chatUserSearchSearch.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.postQuery(query.toString())
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })


    }

    private fun setRecyclerView() {
        binding.chatSearchRecycler.apply {
            adapter = searchChatAdapter
        }
    }
}