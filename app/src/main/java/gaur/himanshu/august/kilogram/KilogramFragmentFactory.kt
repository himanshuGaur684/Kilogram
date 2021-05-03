package gaur.himanshu.august.kilogram

import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import gaur.himanshu.august.kilogram.local.ui.auth.LoginFragment
import gaur.himanshu.august.kilogram.local.ui.auth.RegisterFragment
import gaur.himanshu.august.kilogram.local.ui.intro.FirstFragment
import gaur.himanshu.august.kilogram.local.ui.intro.SecondFragment
import gaur.himanshu.august.kilogram.local.ui.intro.ThirdFragment
import gaur.himanshu.august.kilogram.local.ui.mainapp.follow.FollowFragment
import gaur.himanshu.august.kilogram.local.ui.mainapp.addblog.AddBlogFragment
import gaur.himanshu.august.kilogram.local.ui.mainapp.chat.ChatFragment
import gaur.himanshu.august.kilogram.local.ui.mainapp.comment.CommentFragment
import gaur.himanshu.august.kilogram.local.ui.mainapp.discover.DiscoverFragment
import gaur.himanshu.august.kilogram.local.ui.mainapp.follow.repository.IFollowRepository
import gaur.himanshu.august.kilogram.local.ui.mainapp.home.HomeFragment
import gaur.himanshu.august.kilogram.local.ui.mainapp.notification.NotificationFragment
import gaur.himanshu.august.kilogram.local.ui.mainapp.notificationpost.NotificationPostFragment
import gaur.himanshu.august.kilogram.local.ui.mainapp.profile.ProfileFragment
import gaur.himanshu.august.kilogram.local.ui.mainapp.reply.ReplyFragment
import javax.inject.Inject

class KilogramFragmentFactory @Inject constructor(private val sharedPreferences: SharedPreferences,private val followRepository:IFollowRepository) :
    FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {

        return when (className) {
            FirstFragment::class.java.name -> {
                FirstFragment()
            }
            SecondFragment::class.java.name -> {
                SecondFragment()
            }
            ThirdFragment::class.java.name -> {
                ThirdFragment()
            }
            LoginFragment::class.java.name -> {
                LoginFragment()
            }
            RegisterFragment::class.java.name -> {
                RegisterFragment()
            }
            HomeFragment::class.java.name -> {
                HomeFragment()
            }
            AddBlogFragment::class.java.name -> {
                AddBlogFragment()
            }
            ProfileFragment::class.java.name -> {
                ProfileFragment(sharedPreferences)
            }
            CommentFragment::class.java.name -> {
                CommentFragment(sharedPreferences)
            }
            ReplyFragment::class.java.name -> {
                ReplyFragment(sharedPreferences)
            }
            FollowFragment::class.java.name -> {
                FollowFragment(sharedPreferences,followRepository)
            }
            DiscoverFragment::class.java.name->{
                DiscoverFragment(followRepository)
            }
            NotificationFragment::class.java.name->{
                NotificationFragment()
            }
            NotificationPostFragment::class.java.name->{
                NotificationPostFragment()
            }
            ChatFragment::class.java.name->{
                ChatFragment(sharedPreferences)
            }
            else -> {
                super.instantiate(classLoader, className)
            }
        }


    }
}