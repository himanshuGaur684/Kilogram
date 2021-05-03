package gaur.himanshu.august.kilogram.local.ui.mainapp.addblog

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.dhaval2404.imagepicker.ImagePicker
import gaur.himanshu.august.kilogram.Constants
import gaur.himanshu.august.kilogram.R
import gaur.himanshu.august.kilogram.databinding.FragmentAddBlogBinding
import gaur.himanshu.august.kilogram.local.ui.mainapp.ContainerActivity
import gaur.himanshu.august.kilogram.util.Status
import gaur.himanshu.august.kilogram.util.snack
import gaur.himanshu.august.kilogram.util.toast
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class AddBlogFragment : Fragment(R.layout.fragment_add_blog) {

    lateinit var viewModel: AddBlogViewModel
    lateinit var binding: FragmentAddBlogBinding

    lateinit var file: File

    val args by navArgs<AddBlogFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBlogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()

        viewModel = ViewModelProvider(requireActivity()).get(AddBlogViewModel::class.java)


        if(args.openCamera){
            ImagePicker.with(this).cameraOnly()
                .galleryMimeTypes(  //Exclude gif images
                    mimeTypes = arrayOf(
                        "image/png",
                        "image/jpg",
                        "image/jpeg"
                    )
                )
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }

        binding.addBlogClose.setOnClickListener {
          deInitializeView()
        }

        binding.postBlog.setOnClickListener {

            val request = RequestBody.create(MediaType.parse("image/*"), file)
            val imagePart =
                MultipartBody.Part.createFormData(Constants.BLOG_IMAGE, file.name, request)
            val blogTitle = RequestBody.create(
                MediaType.parse("text/plain"),
                binding.addBlogTitle.text.toString().trim()
            )

            val blogDiscription = RequestBody.create(
                MediaType.parse("text/plain"),
                binding.addBlogDiscription.text.toString().trim()
            )

            viewModel.postBlog(imagePart, blogTitle, blogDiscription)


        }


        binding.addBlogImageFab.setOnClickListener {
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
                    .maxResultSize(1080, 1080)
                    .start()
            }


        viewModel.addBlog.observe(viewLifecycleOwner) {
            when (it.getContentIfNotHandled()?.status) {
                Status.LOADING -> {
                    binding.addBlogProgress.visibility=View.VISIBLE
                }
                Status.ERROR -> {
                    requireContext().toast("Not Posted! Error Occured")
                    binding.addBlogProgress.visibility=View.GONE
                }
                Status.SUCCESS -> {
                    binding.addBlogProgress.visibility=View.GONE
                 requireContext().toast("Successfully Shared")
                    deInitializeView()

                }
            }
        }

        val callback= object:OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                deInitializeView()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callback)


    }

    fun initView(){
        (requireActivity() as ContainerActivity).binding.bottomNavigationView.visibility=View.GONE
    }
    fun deInitializeView(){
        (requireActivity() as ContainerActivity).binding.bottomNavigationView.visibility=View.VISIBLE
        findNavController().popBackStack()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == ImagePicker.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            file = File(ImagePicker.getFilePath(data)!!)

            binding.addBlogImage.setImageURI(data?.data!!)
        }
    }

}