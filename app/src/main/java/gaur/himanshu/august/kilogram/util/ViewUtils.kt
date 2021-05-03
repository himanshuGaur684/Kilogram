package gaur.himanshu.august.kilogram.util

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import gaur.himanshu.august.kilogram.R

@BindingAdapter("glide")
fun glideImage(view: ImageView, str: String?) {
    if (str == null || str.isEmpty()) {
        view.setImageResource(R.drawable.ic_account)
        return
    }
    Picasso.get().load(str).placeholder(R.drawable.ic_account).error(R.drawable.ic_account)
        .into(view)
}

@BindingAdapter("image_glide")
fun imageGlideImage(view: ImageView, str: String?) {
   
    if (str == null || str.isEmpty()) {
        view.setImageResource(R.drawable.warning)
        return
    }
    Picasso.get().load(str)
        .into(view)

}

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun View.snack(msg: String) {
    Snackbar.make(this, msg, Snackbar.LENGTH_SHORT).show()
}

//here edittext is the focused edittext

fun View.hideKeyboard() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.showKeyBoard() {
    this.requestFocus()
    this.post {
        val keyboard: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        keyboard.showSoftInput(this, 0)
    }
}

private fun Context.isInternetAvailable() : Boolean {
    val connectivityManager =
        applicationContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    connectivityManager.activeNetworkInfo.also {
        return it != null && it.isConnected
    }
}


