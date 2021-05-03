package gaur.himanshu.august.kilogram.local.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import gaur.himanshu.august.kilogram.R
import gaur.himanshu.august.kilogram.databinding.FragmentLoginBinding
import gaur.himanshu.august.kilogram.local.ui.mainapp.ContainerActivity
import gaur.himanshu.august.kilogram.remote.body.LoginBody
import gaur.himanshu.august.kilogram.util.Status
import gaur.himanshu.august.kilogram.util.toast

class LoginFragment : Fragment(R.layout.fragment_login) {
    lateinit var binding: FragmentLoginBinding

    lateinit var viewModel: AuthViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)


        binding.loginButton.setOnClickListener {
            val email = binding.loginEmail.text.toString().trim()
            val password = binding.loginPassword.text.toString().trim()

            FirebaseMessaging.getInstance().token.addOnCompleteListener {
                if (it.isSuccessful) {
                    it.result?.let {result->
                        viewModel.login(LoginBody(email, password, result))
                    }

                } else {
                    requireContext().toast("Check your Connectivity")
                }
            }


        }


        viewModel.loginBody.observe(viewLifecycleOwner) {
            when (it.getContentIfNotHandled()?.status) {
                Status.LOADING -> {
                    binding.loginProgress.visibility = View.VISIBLE
                }
                Status.ERROR -> {
                    binding.loginProgress.visibility = View.GONE
                }
                Status.SUCCESS -> {
                    binding.loginProgress.visibility = View.GONE
                    Snackbar.make(binding.root, "Login Successful", Snackbar.LENGTH_SHORT).show()
                    startActivity(Intent(requireContext(), ContainerActivity::class.java))
                    requireActivity().finish()
                }
            }
        }

        binding.navigateToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

}