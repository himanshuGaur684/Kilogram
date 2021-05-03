package gaur.himanshu.august.kilogram.local.ui.mainapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import gaur.himanshu.august.kilogram.KilogramFragmentFactory
import gaur.himanshu.august.kilogram.R
import gaur.himanshu.august.kilogram.databinding.ActivityContainerBinding
import gaur.himanshu.august.kilogram.local.ui.mainapp.chat.service.ChatService
import javax.inject.Inject

@AndroidEntryPoint
class ContainerActivity : AppCompatActivity() {

    @Inject
    lateinit var fragmentFactory: KilogramFragmentFactory


    lateinit var startService: Intent
    lateinit var navController: NavController

    lateinit var binding: ActivityContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.fragmentFactory = fragmentFactory

        binding = DataBindingUtil.setContentView(this, R.layout.activity_container)


        navController = findNavController(R.id.container_container)

        binding.bottomNavigationView.setupWithNavController(navController)

        startService = Intent(this, ChatService::class.java)


        startService(startService)

    }

    override fun onPause() {
        super.onPause()
        Log.d("TAG", "onDestroy: activity destroyed")
        // this.stopService(startService)
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(startService)
    }
}