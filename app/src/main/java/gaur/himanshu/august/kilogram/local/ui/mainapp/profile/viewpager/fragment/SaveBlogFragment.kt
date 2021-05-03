package gaur.himanshu.august.kilogram.local.ui.mainapp.profile.viewpager.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import gaur.himanshu.august.kilogram.databinding.FragmentBlogSaveBinding
import gaur.himanshu.august.kilogram.local.ui.mainapp.home.paging.HomeMediaterPagingAdapter
import gaur.himanshu.august.kilogram.local.ui.mainapp.home.paging.HomePagingAdapter
import gaur.himanshu.august.kilogram.local.ui.mainapp.profile.ProfileViewModel


class SaveBlogFragment : Fragment() {

    lateinit var viewModel: ProfileViewModel
    lateinit var binding: FragmentBlogSaveBinding

    lateinit var blogAdapter : HomePagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)
        binding = FragmentBlogSaveBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setRecyclerView()
        viewModel.saveBlog.observe(viewLifecycleOwner) {
            blogAdapter.submitData(lifecycle, it)
        }

        binding.swipeRefreshLayoutSaveBlogs.setOnRefreshListener {
            binding.swipeRefreshLayoutSaveBlogs.isRefreshing=false
            blogAdapter.refresh()
        }

        blogAdapter.onDeleteOfflineBlog {
            viewModel.delteteSaveBlog(it)
            blogAdapter.refresh()
        }


    }

    private fun setRecyclerView() {
        blogAdapter=  HomePagingAdapter(requireContext(),SaveBlogFragment::class.java.name)
        binding.postFragmentRecycler.apply {
            adapter = blogAdapter
        }
    }
}