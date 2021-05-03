package gaur.himanshu.august.kilogram.local.ui.mainapp.notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import gaur.himanshu.august.kilogram.databinding.FragmentNotificationBinding
import gaur.himanshu.august.kilogram.local.ui.mainapp.notification.paging.NotificationPagingAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class NotificationFragment : Fragment() {


    lateinit var binding: FragmentNotificationBinding
    lateinit var viewModel: NotificationViewModel

    val notificationAdapter = NotificationPagingAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity()).get(NotificationViewModel::class.java)
        return binding.root
    }

    @ExperimentalPagingApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setRecyclerView()
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.notification.collectLatest {
                notificationAdapter.submitData(it)
            }
        }

        binding.apply {
            notificationToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            notificationSwipeRefresh.setOnRefreshListener {
                notificationSwipeRefresh.isRefreshing = false
                notificationAdapter.refresh()
            }
        }

        notificationAdapter.onNotificationClick {
            val action =
                NotificationFragmentDirections.actionNotificationFragmentToNotificationPostFragment(
                    it.post_id
                )
            findNavController().navigate(action)
        }


    }

    private fun setRecyclerView() {
        binding.apply {
            notificationRecycler.adapter = notificationAdapter
        }
    }


}