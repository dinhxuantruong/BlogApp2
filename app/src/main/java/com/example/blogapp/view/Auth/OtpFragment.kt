package com.example.blogapp.view.Auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.blogapp.R
import com.example.blogapp.databinding.FragmentOtpBinding
import com.example.blogapp.model.AcceptOTP
import com.example.blogapp.model.ForgetPass
import com.example.blogapp.model.sendOTP
import com.example.blogapp.utils.EmailType
import com.example.blogapp.utils.Resources
import com.example.blogapp.viewmodel.AuthViewModel

class OtpFragment : Fragment() {
   private var _binding : FragmentOtpBinding? = null
    private val binding get() = _binding!!
    private  var _editTextList: List<EditText>? = null
    private  val editTextList get() = _editTextList!!
    private val viewModel: AuthViewModel by activityViewModels()
    var email: String? = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOtpBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment

         email = arguments?.getString("email")
        val pass = arguments?.getString("pass")
        val confPass = arguments?.getString("conf pass")


        binding.btnSubmit.setOnClickListener {
            val otp = getOtpFromEditText()
            if (otp.length == 6) {
                when(email){
                    EmailType.REGISTER -> {
                        viewModel.authAcceptRegister(AcceptOTP(EmailType.REGISTER,otp))
                        binding.toolRegisterOTP.setNavigationOnClickListener {
                            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
                        }
                    }

                    EmailType.FORGOT -> {
                        viewModel.authForgetPassword(ForgetPass(EmailType.FORGOT,otp,pass!!,confPass!!))
                        binding.toolRegisterOTP.setNavigationOnClickListener {
                            findNavController().navigate(R.id.action_forgotFragment_to_loginFragment)
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Nhập đủ 6 số OTP", Toast.LENGTH_SHORT).show()
            }
        }


        val modifiedEmail = modifyString(email!!)
        binding.emailView.text = modifiedEmail
        //Khoi tao view model
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }

        binding.tvSendOTP.setOnClickListener {
            viewModel.authSendOTP(sendOTP(email!!))
        }

        viewModel.resultForget.observe(viewLifecycleOwner){
            when(it){
                is Resources.Success -> {
                    clearEmail()
                    val message = it.data.message
                    Toast.makeText(requireContext(),message , Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_otpFragment_to_loginFragment)
                }

                is Resources.Error -> {
                    val message = it.message
                    Toast.makeText(requireContext(),message, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
        viewModel.resultOTP.observe(viewLifecycleOwner) {
            when (it) {
                is Resources.Success -> {
                    Toast.makeText(requireContext(), it.data.message, Toast.LENGTH_SHORT).show()
                }

                is Resources.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }


        // Initialize the list of EditTexts
        _editTextList = listOf(
            binding.editTextDigit1,
            binding.editTextDigit2,
            binding.editTextDigit3,
            binding.editTextDigit4,
            binding.editTextDigit5,
            binding.editTextDigit6
        )

        // Add TextWatcher and OnKeyListener to handle backspace/delete key press
        setupEditTextListeners()

        viewModel.acceptResult.observe(viewLifecycleOwner) {
            when (it) {
                is Resources.Success -> {
                    clearEmail()
                    Toast.makeText(requireContext(), it.data.message, Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack(R.id.otpFragment, false)
                    findNavController().navigate(R.id.action_otpFragment_to_loginFragment)
                }

                is Resources.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }

                else -> {
                    Toast.makeText(requireContext(), "Đăng ký tài khoản không thành công", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }


        return binding.root
    }

    private fun clearEmail(){
        EmailType.REGISTER = ""
        EmailType.FORGOT = ""
    }

    private fun modifyString(originalString: String): String {
        val length = originalString.length
        val atIndex = originalString.indexOf('@')
        if (atIndex != -1 && atIndex >= 5) { // Đảm bảo có ký tự '@' và có đủ ký tự trước và sau '@'
            val stars =
                "*".repeat((length - length / 1.40).toInt()) // Số dấu sao tương ứng với số ký tự giữ nguyên trước '@' (ở đây là 5)
            return originalString.take(5) + stars + originalString.substring(atIndex)
        }
        return originalString // Trả về chuỗi ban đầu nếu không có ký tự '@' hoặc không đủ ký tự trước '@'
    }


    private fun getOtpFromEditText(): String {
        val stringBuilder = StringBuilder()
        for (editText in editTextList) {
            stringBuilder.append(editText.text.toString())
        }
        return stringBuilder.toString()
    }

    private fun setupEditTextListeners() {
        for (i in editTextList.indices) {
            val currentEditText = editTextList[i]
            val nextEditText = if (i < editTextList.size - 1) editTextList[i + 1] else null
            val prevEditText = if (i > 0) editTextList[i - 1] else null

            currentEditText.addTextChangedListener(createTextWatcher(currentEditText, nextEditText))
            setOnBackspaceListener(currentEditText, prevEditText)
        }
    }

    private fun createTextWatcher(currentEditText: EditText, nextEditText: EditText?): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 1 && nextEditText != null) {
                    nextEditText.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }
    }


    private fun setOnBackspaceListener(editText: EditText, prevEditText: EditText?) {
        editText.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN && editText.text.isEmpty() && prevEditText != null) {
                prevEditText.requestFocus()
                prevEditText.text.clear()  // Xóa nội dung của ô trước đó
                true
            } else {
                false
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _editTextList = null
        _binding = null
    }


}