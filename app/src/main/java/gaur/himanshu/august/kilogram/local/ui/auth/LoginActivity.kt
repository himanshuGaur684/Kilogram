package gaur.himanshu.august.kilogram.local.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import gaur.himanshu.august.kilogram.KilogramFragmentFactory
import gaur.himanshu.august.kilogram.R
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    @Inject
    lateinit var fragmentFactory: KilogramFragmentFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.fragmentFactory = fragmentFactory



        setContentView(R.layout.activity_login)
    }
}