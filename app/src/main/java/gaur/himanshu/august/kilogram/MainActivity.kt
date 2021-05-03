package gaur.himanshu.august.kilogram

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import gaur.himanshu.august.kilogram.databinding.ActivityMainBinding
import gaur.himanshu.august.kilogram.local.ui.auth.LoginActivity
import gaur.himanshu.august.kilogram.local.ui.mainapp.ContainerActivity
import gaur.himanshu.august.kilogram.local.ui.intro.ViewPagerAdapter
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var fragmentFactory: KilogramFragmentFactory
    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.fragmentFactory = fragmentFactory
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.introViewpager.adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)



    if(sharedPreferences.getString(Constants.AUTH_TOKEN,"")!!.isNotEmpty()){
        startActivity(Intent(this,ContainerActivity::class.java))
        finish()
    }


      binding.getStarted.setOnClickListener {
          startActivity(Intent( this,LoginActivity::class.java))
          finish()
      }

        Timer().schedule(object : TimerTask() {
            // task to be scheduled
            override fun run() {
                when(binding.introViewpager.currentItem){
                    0->{
                        MainScope().launch {
                            binding.introViewpager.setCurrentItem(1,true)
                        }
                        return
                    }
                    1->{
                        MainScope().launch {
                            binding.introViewpager.setCurrentItem(2,true)
                        }
                        return
                    }
                    2->{
                        MainScope().launch {
                            binding.introViewpager.setCurrentItem(0,true)
                            binding.introViewpager.currentItem=0
                        }
                         return
                    }
                }

            }
        }, 3500, 3500)



    }

}