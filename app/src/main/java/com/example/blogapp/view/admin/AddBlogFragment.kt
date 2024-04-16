package com.example.blogapp.view.admin

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.navGraphViewModels
import com.example.blogapp.R
import com.example.blogapp.databinding.FragmentAddBlogBinding
import com.example.blogapp.repository.authRepository.AuthRepository
import com.example.blogapp.utils.Resources
import com.example.blogapp.utils.SharedPreference.SharedPreferencesHelper
import com.example.blogapp.utils.UserPreferences
import com.example.blogapp.utils.getFileName
import com.example.blogapp.utils.uploadRequestBody
import com.example.blogapp.view.NoteActivity
import com.example.blogapp.viewmodel.AddViewModel
import com.example.blogapp.viewmodel.AddViewModelFactory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class AddBlogFragment : Fragment() {
    private var _binding: FragmentAddBlogBinding? = null
    private val binding get() = _binding!!
    private var _itemList: MutableList<String>? = null
    private val itemList get() = _itemList!!
    private var selectImage: Uri? = null
    private lateinit var viewModel: AddViewModel
    private lateinit var imagePickerLaucher: ActivityResultLauncher<Intent>
    private lateinit var sharedPreferencesHelper: SharedPreferencesHelper
    private var autoComple = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddBlogBinding.inflate(inflater, container, false)
        sharedPreferencesHelper = SharedPreferencesHelper(requireContext())
        val repository = AuthRepository(requireContext())
        val vmFactory = AddViewModelFactory(repository)
        viewModel = ViewModelProvider(this, vmFactory)[AddViewModel::class.java]

        _itemList =
            mutableListOf<String>("Sport", "Food", "Education", "Travel", "Fashion", "Another Type")
        val compleAdapter = ArrayAdapter(requireActivity(), R.layout.layout_list, itemList)
        binding.autoComplete.setAdapter(compleAdapter)
        binding.autoComplete.setOnItemClickListener { _, _, position, _ ->
            autoComple = position + 1
            binding.autoComplete.dismissDropDown()
        }
        binding.longDescriptionEditText.setOnClickListener {
            sharedPreferencesHelper.saveTitle(binding.titleEditText.text.toString())
            sharedPreferencesHelper.saveShortDescription(binding.shortDescriptionEditText.text.toString())
            sharedPreferencesHelper.saveImage(selectImage)
            val intent = Intent(requireActivity(), NoteActivity::class.java)
            intent.putExtra("text", binding.longDescriptionEditText.text.toString())
            startActivity(intent)
        }

        val savedTitle = sharedPreferencesHelper.getTitle()
        binding.titleEditText.setText(savedTitle)
        val savedShortDes = sharedPreferencesHelper.getShortDescription()
        binding.shortDescriptionEditText.setText(savedShortDes)
        val saveLongDess = sharedPreferencesHelper.getLongDescription()
        binding.longDescriptionEditText.setText(saveLongDess)
        val saveUri = sharedPreferencesHelper.getImage()
        binding.imageView.setImageURI(saveUri)
        selectImage = saveUri

        setImageView()
        binding.imageView.setOnClickListener {
            chooseImage()
        }

        binding.button.setOnClickListener {
            uploadImages()
        }


        viewObserve()

        return binding.root
    }

    private fun uploadImages() {
        if (selectImage == null || binding.titleEditText.text.isNullOrEmpty()
            || binding.shortDescriptionEditText.text.isNullOrEmpty()
            || binding.longDescriptionEditText.text.isNullOrEmpty()
        ) {
            //binding.addFragmetn.snackbar("Select Image!")
            Toast.makeText(requireContext(), "null", Toast.LENGTH_SHORT).show()
            return
        }

        val contentProvider: ContentResolver? = requireActivity().contentResolver

        val parcelFileDescriptor =
            contentProvider?.openFileDescriptor(selectImage!!, "r", null) ?: return

        val file = File(requireContext().cacheDir, contentProvider.getFileName(selectImage!!))
        val fileInputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val outputStream = FileOutputStream(file)
        fileInputStream.copyTo(outputStream)
        // binding.progressBar.progress = 0
        val body = uploadRequestBody(file, "image")

        val userPreferences = UserPreferences(requireContext())
        userPreferences.authToken.asLiveData().observe(viewLifecycleOwner, Observer {
            val token = it.toString()
            val title = binding.titleEditText.text.toString()
                .toRequestBody("multipart/from-data".toMediaTypeOrNull())
            val short_description = binding.shortDescriptionEditText.text.toString()
                .toRequestBody("multipart/from-data".toMediaTypeOrNull())
            val long_description = binding.longDescriptionEditText.text.toString()
                .toRequestBody("multipart/from-data".toMediaTypeOrNull())
            val category_id = autoComple.toString()
                .toRequestBody("multipart/from-data".toMediaTypeOrNull())
            val image = MultipartBody.Part.createFormData("image", file.name, body)
            viewModel.addBlog(token, title, short_description, long_description, category_id, image)
        })

    }
private fun setImageView() {
    imagePickerLaucher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                selectImage = data?.data
                binding.imageView.setImageURI(selectImage)
            }
        }
}

    private fun chooseImage() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            //Định dạng kiểu file
            type = "image/*"
            val mimeType = arrayOf("image/jpeg", "image/png")
            putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
        }
        imagePickerLaucher.launch(intent)
    }

    private fun viewObserve() {
        viewModel.resultAddBlog.observe(viewLifecycleOwner){
            when(it){
                is Resources.Success -> {
                    Toast.makeText(requireContext(), it.data.message, Toast.LENGTH_SHORT).show()
                    sharedPreferencesHelper.clearData()
                    binding.titleEditText.setText("")
                    binding.shortDescriptionEditText.setText("")
                    binding.longDescriptionEditText.setText("")
                    binding.imageView.setImageResource(R.drawable.ic_launcher_foreground)
                }

                is Resources.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _itemList = null
        _binding = null
    }


}
