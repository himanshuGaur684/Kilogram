package gaur.himanshu.august.kilogram.local.ui.mainapp.serach

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import gaur.himanshu.august.kilogram.R
import gaur.himanshu.august.kilogram.databinding.FragmentSearchBinding
import gaur.himanshu.august.kilogram.local.ui.mainapp.AdapterLoadState
import gaur.himanshu.august.kilogram.local.ui.mainapp.serach.paging.SearchPagingAdapter
import gaur.himanshu.august.kilogram.util.toast


class SearchFragment : Fragment(R.layout.fragment_search) {

    lateinit var binding: FragmentSearchBinding
    lateinit var viewModel: SearchViewModel

    val searchAdapter = SearchPagingAdapter()


    val args:SearchFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)
        setRecyclerView()



        searchAdapter.addLoadStateListener {
            when (it.refresh) {
                is LoadState.Loading -> {
                    binding.apply {
                        searchUsers.isEnabled = false
                        searchProgress.visibility = View.VISIBLE
                    }

                }
                is LoadState.NotLoading -> {
                    binding.apply {
                        searchUsers.isEnabled = true
                        searchProgress.visibility = View.GONE
                    }
                }
                is LoadState.Error -> {
                    binding.apply {
                        searchUsers.isEnabled = true
                        searchProgress.visibility = View.GONE
                        requireContext().toast("Error occurred")
                    }

                }
            }
        }



        searchAdapter.onClickListenerOfListItem {
            if(args.userFragment==null){}else{


            }
            findNavController().navigate(
                SearchFragmentDirections.actionSearchFragmentToProfileFragment(
                    it._id
                )
            )
        }

        binding.searchUsers.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.query(it.toString())
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })


//


        viewModel.queryResult.observe(viewLifecycleOwner) {
            it?.let {
                searchAdapter.submitData(lifecycle, it)
            }

        }

    }

    private fun setRecyclerView() {
        binding.searchRecyclerView.apply {
            adapter = searchAdapter.withLoadStateHeaderAndFooter(
                header = AdapterLoadState { searchAdapter.retry() },
                footer = AdapterLoadState { searchAdapter.retry() }
            )
        }
    }


}