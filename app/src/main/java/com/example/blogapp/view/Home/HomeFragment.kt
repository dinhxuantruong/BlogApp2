package com.example.blogapp.view.Home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.models.SlideModel
import com.example.blogapp.adapter.customRecyclerView
import com.example.blogapp.databinding.FragmentHomeBinding
import com.example.blogapp.model.response.Blog
import com.example.blogapp.utils.Resources
import com.example.blogapp.utils.UserPreferences
import com.example.blogapp.view.details.DetailsActivity
import com.example.blogapp.viewmodel.HomeViewModel

class HomeFragment : Fragment(){


    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var imageList: MutableList<SlideModel>
    private lateinit var listBlogs : MutableList<Blog>
    private lateinit var listBlogsAdapter: customRecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        listBlogs = mutableListOf()
        imageList = mutableListOf()
        binding.rycView.layoutManager = LinearLayoutManager(requireActivity()!!)
        binding.rycView.setHasFixedSize(true)
        val userPreferences = UserPreferences(requireContext())
        userPreferences.authToken.asLiveData().observe(viewLifecycleOwner, Observer {
            viewModel.blogs(it.toString())
        })

        viewModel.slide()

        viewModel.getSlide.observe(viewLifecycleOwner) {
            when (it) {
                is Resources.Success -> {
                    val dataResult = it.data.data_result
                    imageList.clear()
                    dataResult.forEach { item ->
                        imageList.add(SlideModel(item.image_url))
                    }
                    binding.imageSlider.setImageList(imageList)
                }

                is Resources.Error -> {

                }
            }
        }

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
//                            Toast.makeText(requireContext(), itemBlog.human_readable_createAt, Toast.LENGTH_SHORT).show()
                            val intent = Intent(requireActivity(),DetailsActivity::class.java)
                            val bundle = Bundle()
                            bundle.putString("id",itemBlog.id.toString())
                            bundle.putBoolean("check",true)
                            intent.putExtras(bundle)
                            startActivity(intent)
                        }
                    },
                        requireActivity(),listBlogs!!)
                    binding.rycView.adapter = listBlogsAdapter
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
        binding.imageSlider.stopSliding()
        _binding = null
    }



}
