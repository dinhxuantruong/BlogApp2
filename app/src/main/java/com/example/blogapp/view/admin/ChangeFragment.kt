package com.example.blogapp.view.admin

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blogapp.R
import com.example.blogapp.adapter.customRecyclerView
import com.example.blogapp.databinding.FragmentChangeBinding
import com.example.blogapp.model.response.Blog
import com.example.blogapp.repository.authRepository.AuthRepository
import com.example.blogapp.utils.MyButton
import com.example.blogapp.utils.MyButtonClickListener
import com.example.blogapp.utils.MySwipeHelper
import com.example.blogapp.utils.Resources
import com.example.blogapp.utils.UserPreferences
import com.example.blogapp.view.details.DetailsActivity
import com.example.blogapp.viewmodel.AddViewModel
import com.example.blogapp.viewmodel.AddViewModelFactory


class ChangeFragment : Fragment() {
    private var _binding : FragmentChangeBinding? = null
    private val binding get() = _binding!!
    private lateinit var listBlogs : MutableList<Blog>
    private lateinit var listBlogsAdapter: customRecyclerView
    private lateinit var viewModel : AddViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangeBinding.inflate(inflater,container,false)
        val repository = AuthRepository(requireContext())
        val vmFactory = AddViewModelFactory(repository)
        viewModel = ViewModelProvider(this, vmFactory)[AddViewModel::class.java]

        listBlogs = mutableListOf()
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity()!!)

        val swipe = object  : MySwipeHelper(requireContext(),binding.recyclerView,200){
            override fun instantiateButton(viewHolder: RecyclerView.ViewHolder, buffer: MutableList<MyButton>) {
                buffer.add(
                    MyButton(requireContext(),"Delete",30,0,Color.parseColor("#FF3C30"),
                    object : MyButtonClickListener{
                        override fun onClickedItem(pos: Int) {
                            Toast.makeText(requireContext(), pos.toString(), Toast.LENGTH_SHORT).show()
                        }
                    })
                )

                buffer.add(
                    MyButton(requireContext(),"Update",30, R.drawable.update,Color.parseColor("#FF9520"),
                        object : MyButtonClickListener{
                            override fun onClickedItem(pos: Int) {
                                Toast.makeText(requireContext(), pos.toString(), Toast.LENGTH_SHORT).show()
                            }
                        })
                )

            }
        }
        val userPreferences = UserPreferences(requireContext())
        userPreferences.authToken.asLiveData().observe(viewLifecycleOwner, Observer {
           viewModel.blogs(it.toString())
        })


        viewModel.getBlogs.observe(viewLifecycleOwner) {
            when (it) {
                is Resources.Success -> {
                    listBlogs!!.clear()
                    val blogs = it.data.blogs
                    Log.e("MAIN",blogs.toString())
                    blogs.forEach {blog ->
                        listBlogs.add(blog)
                    }
                    listBlogsAdapter = customRecyclerView(
                        object : customRecyclerView.ClickListener {
                        override fun onClickedItem(itemBlog: Blog) {

                        }
                    },
                        requireActivity(),listBlogs!!)
                    binding.recyclerView.adapter = listBlogsAdapter
                }

                is Resources.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }



        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}