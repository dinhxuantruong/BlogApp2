package com.example.blogapp.view.Home

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.example.blogapp.databinding.FragmentSettingBinding
import com.example.blogapp.utils.Resources
import com.example.blogapp.utils.UserPreferences
import com.example.blogapp.utils.getFileName
import com.example.blogapp.utils.uploadRequestBody
import com.example.blogapp.view.Auth.AuthActivity
import com.example.blogapp.viewmodel.HomeViewModel
import com.squareup.picasso.Picasso
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()
    private var selectImage: Uri? = null
    private lateinit var imagePickerLaucher: ActivityResultLauncher<Intent>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)

        requestDataUser()
        setImageView()
        binding.userAvatar2.setOnClickListener {
            chooseImage()
        }
        binding.btnSave.setOnClickListener {
            uploadImages()
        }
        binding.btnLogout.setOnClickListener {
            val userPreferences = UserPreferences(requireContext())
            userPreferences.authToken.asLiveData().observe(viewLifecycleOwner, Observer {
                val token = it.toString()
                viewModel.authLogout(token)
            })
        }
        observeData()
        return binding.root
    }

    private fun setImageView() {
        imagePickerLaucher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    selectImage = data?.data
                    binding.userAvatar2.setImageURI(selectImage)
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

    private fun uploadImages() {
        if (selectImage == null) {
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
        val body = uploadRequestBody(file, "profile_photo")

        val userPreferences = UserPreferences(requireContext())
        userPreferences.authToken.asLiveData().observe(viewLifecycleOwner, Observer {
            val token = it.toString()
            val name = binding.etName.text.toString()
                .toRequestBody("multipart/from-data".toMediaTypeOrNull())
            val profession = binding.etProfession.text.toString()
                .toRequestBody("multipart/from-data".toMediaTypeOrNull())
            val profile_photo =   MultipartBody.Part.createFormData("profile_photo",file.name,body)
            viewModel.updateProfile(token,name,profession,profile_photo)
        })

    }

    private fun observeData() {
        viewModel.resultUpdateProfile.observe(viewLifecycleOwner) {
            when (it) {
                is Resources.Success -> {
                    Toast.makeText(requireContext(), it.data.message, Toast.LENGTH_SHORT).show()
                }

                is Resources.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
                //
            }
        }
        viewModel.resultProfile.observe(viewLifecycleOwner) {
            when (it) {
                is Resources.Success -> {
                    Picasso.get().load(it.data.profile_photo).into(binding.userAvatar2)
                    binding.etName.setText(it.data.name)
                    binding.etEmail.setText(it.data.email)
                    binding.etProfession.setText(it.data.profession)
                }

                is Resources.Error -> {
                    //
                }
            }
        }

        viewModel.resultLogout.observe(viewLifecycleOwner){
            when(it) {
                is Resources.Success -> {
                    Toast.makeText(requireContext(), it.data.message, Toast.LENGTH_SHORT).show()
                    startActivity(Intent(requireActivity(),AuthActivity::class.java))
                    requireActivity().finish()
                }

                is Resources.Error -> {

                }
            }
        }
    }


    private fun requestDataUser() {
        val userPreferences = UserPreferences(requireContext())
        userPreferences.authToken.asLiveData().observe(viewLifecycleOwner, Observer {
            viewModel.getProfile(it.toString())
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}