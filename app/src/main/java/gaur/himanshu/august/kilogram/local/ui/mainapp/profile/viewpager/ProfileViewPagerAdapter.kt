package gaur.himanshu.august.kilogram.local.ui.mainapp.profile.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import gaur.himanshu.august.kilogram.local.ui.mainapp.profile.ProfileViewModel
import gaur.himanshu.august.kilogram.local.ui.mainapp.profile.viewpager.fragment.SaveBlogFragment
import gaur.himanshu.august.kilogram.local.ui.mainapp.profile.viewpager.fragment.postfragment.PostFragment

class ProfileViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val args:String?,
    private val viewModel: ProfileViewModel
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                PostFragment(args,viewModel)
            }
            1 -> {
                SaveBlogFragment()
            }
            else -> {
                Fragment()
            }
        }
    }
}