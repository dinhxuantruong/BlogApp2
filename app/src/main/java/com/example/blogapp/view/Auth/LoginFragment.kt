package com.example.blogapp.view.Auth

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.app.SharedPreference.PrefManager
import com.example.blogapp.R
import com.example.blogapp.databinding.FragmentLoginBinding
import com.example.blogapp.model.Auth
import com.example.blogapp.utils.Resources
import com.example.blogapp.view.Home.HomeActivity
import com.example.blogapp.view.admin.AdminActivity
import com.example.blogapp.viewmodel.AuthViewModel

class LoginFragment : Fragment(), View.OnClickListener, View.OnKeyListener,
    View.OnFocusChangeListener {
    private val viewModel: AuthViewModel by activityViewModels()
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var _prefManager: PrefManager? = null
    private val prefManager get() = _prefManager!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)


        init()
        // checkLogin()


        binding.emailEdt.onFocusChangeListener = this
        binding.passwordEdt.onFocusChangeListener = this
        binding.emailEdt.onFocusChangeListener = this
        binding.passwordEdt.onFocusChangeListener = this

        binding.emailEdt.addTextChangedListener(emailWatcher)
        binding.passwordEdt.addTextChangedListener(passwordWatcher)
        binding.btnForgotPass.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotFragment)
        }

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        // Khởi tạo ViewModel Factory

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading == true) View.VISIBLE else View.GONE
        }

//        prefManager.getPassword()?.let { Auth(prefManager.getUserName()!!, it) }
//            ?.let { viewModel.login(it) }

        binding.loginButton.setOnClickListener {
            val email = binding.emailEdt.text.toString()
            val password = binding.passwordEdt.text.toString()

            // Kiểm tra dữ liệu đầu vào
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Gọi hàm đăng nhập từ ViewModel
                viewModel.login(Auth(email, password))
            } else {
                Toast.makeText(
                    requireContext(),
                    "Vui lòng nhập đủ thông tin đăng nhập",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

// Quan sát LiveData để nhận kết quả đăng nhập từ ViewModel
        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Resources.Success -> {
                    // Xử lý khi đăng nhập thành công
                    prefManager.setLogin(true)
                    prefManager.saveCredentials(
                        binding.emailEdt.text.toString(),
                        binding.passwordEdt.text.toString()
                    )
//                    if (result.data.user.role == 1) {
//                        startActivity(Intent(requireActivity(), AdminActivity::class.java))
//                    } else {
                        startActivity(Intent(requireActivity(), HomeActivity::class.java))
                 //   }
                    val intent = Intent(requireActivity(), HomeActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }

                is Resources.Error -> {
                    // Xử lý khi đăng nhập thất bại
                    val errorMessage = result.message
                    prefManager.setLogin(false)
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }


        return binding.root
    }

    private val emailWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            val email = s.toString()

            if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                // Email đúng định dạng, thực hiện các xử lý tương ứng
                binding.emailTil.apply {
                    isErrorEnabled = false
                }
            } else {
                // Email không đúng định dạng, hiển thị lỗi và xóa icon check
                binding.emailTil.apply {
                    isErrorEnabled = true
                    error = "Invalid email format"
                    startIconDrawable = null
                }
            }
        }
    }

    private val passwordWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            val password = s.toString()
            if (password.length in 6..15) {
                // Độ dài mật khẩu hợp lệ, thực hiện các xử lý tương ứng
                binding.passwordTil.apply {
                    isErrorEnabled = false
                    setStartIconDrawable(R.drawable.baseline_check_24)
                    setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                }
            } else {
                // Độ dài mật khẩu không hợp lệ, hiển thị lỗi và xóa icon check
                binding.passwordTil.apply {
                    isErrorEnabled = true
                    error = "Password must be between 6 and 15 characters long"
                    startIconDrawable = null
                }
            }
        }
    }

    private fun init() {
        _prefManager = PrefManager(requireContext())
    }

    private fun checkLogin() {
        if (prefManager.isLogin()!!) {
            startActivity(Intent(requireActivity(), HomeActivity::class.java))
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.emailEdt.removeTextChangedListener(emailWatcher)
        binding.passwordEdt.removeTextChangedListener(passwordWatcher)
        // Đảm bảo rằng activity không giữ tham chiếu đến ViewModel khi không cần thiết nữa
        _binding = null
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {

    }

    override fun onClick(v: View?) {

    }

    override fun onKey(view: View?, keyCode: Int, event: KeyEvent?): Boolean {
        return false
    }


}