package gaur.himanshu.august.kilogram.local.ui.auth

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.CursorLoader
import androidx.navigation.fragment.findNavController
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.messaging.FirebaseMessaging
import gaur.himanshu.august.kilogram.R
import gaur.himanshu.august.kilogram.databinding.FragmentRegisterBinding
import gaur.himanshu.august.kilogram.local.ui.mainapp.ContainerActivity
import gaur.himanshu.august.kilogram.util.Status
import gaur.himanshu.august.kilogram.util.toast
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class RegisterFragment : Fragment(R.layout.fragment_register) {


    private val RC_SIGN_IN: Int = 0
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var gso: GoogleSignInOptions
    lateinit var googleSignInClient: GoogleSignInClient

    lateinit var viewModel: AuthViewModel
    lateinit var binding: FragmentRegisterBinding

    private val PICK_IMAGE = 1

    lateinit var file: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configure Google Sign In
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()


        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)


        binding.profilePic.setOnClickListener {
            ImagePicker.with(this).galleryOnly()
                .galleryMimeTypes(  //Exclude gif images
                    mimeTypes = arrayOf(
                        "image/png",
                        "image/jpg",
                        "image/jpeg"
                    )
                )
                .crop()
                .compress(1024)
                .maxResultSize(512, 512)
                .start()
        }


        binding.registerButton.setOnClickListener {
            val request = RequestBody.create(MediaType.parse("image/*"), file)
            val imagePart = MultipartBody.Part.createFormData("profileimage", file.name, request)
            val username = RequestBody.create(
                MediaType.parse("text/plain"),
                binding.registerUsername.text.toString().trim()
            )
            val password = RequestBody.create(
                MediaType.parse("text/plain"),
                binding.loginPassword.text.toString().trim()
            )
            val email = RequestBody.create(
                MediaType.parse("text/plain"),
                binding.loginEmail.text.toString().trim()
            )
            val phone = RequestBody.create(
                MediaType.parse("text/plain"),
                binding.registerPhone.text.toString().trim()
            )

            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val fcmTokens = task.result
                    if (!fcmTokens.isNullOrEmpty()) {
                        Log.d("TAG", "onViewCreated: ${fcmTokens}")
                        val fcm =
                            RequestBody.create(MediaType.parse("text/plain"), fcmTokens)
                        viewModel.register(imagePart, username, email, phone, password, fcm)
                    }
                } else {
                    requireContext().toast("Error Occured! Please Retry")
                }
            }


        }

        binding.googleRegisterButton.setOnClickListener {
            signIn()
        }

        viewModel.registerBody.observe(viewLifecycleOwner) {
            when (it?.peekContent()?.status) {
                Status.LOADING -> {
                    binding.loginProgress.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    binding.loginProgress.visibility = View.GONE
                    requireContext().toast("Registration Successful ! Please Login")
                    findNavController().popBackStack()

                }
                Status.ERROR -> {
                    binding.loginProgress.visibility = View.GONE
                    Snackbar.make(
                        binding.root,
                        "Registration In Successfull ${it.peekContent().toString()}",
                        Snackbar.LENGTH_SHORT
                    ).show()

                }
            }
        }


    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent

        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                e.printStackTrace()
            }
        }

        if (resultCode == Activity.RESULT_OK && requestCode == ImagePicker.REQUEST_CODE) {
            file = File(ImagePicker.getFilePath(data)!!)

            binding.profilePic.setImageURI(data?.data!!)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {

                    Snackbar.make(binding.root, "Authentication Failed.", Snackbar.LENGTH_SHORT)
                        .show()
                    updateUI(null)
                }


            }
    }

    private fun updateUI(user: FirebaseUser?) {
        user?.let {
            binding.loginEmail.setText(it.email)
            binding.registerUsername.setText(it.displayName)
            binding.registerPhone.setText(it.phoneNumber)
        }

        auth.signOut()

    }


    private fun getRealPathFromURI(contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media._ID)
        val loader = CursorLoader(requireContext(), contentUri, proj, null, null, null)
        val cursor: Cursor = loader.loadInBackground()!!
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        cursor.moveToFirst()
        val result = cursor.getString(column_index)
        cursor.close()
        return result
    }

}