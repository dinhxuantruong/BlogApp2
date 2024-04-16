package com.example.blogapp.view.Home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.example.blogapp.R
import com.example.blogapp.adapter.listBlogsAdapter
import com.example.blogapp.databinding.FragmentSearchBinding
import com.example.blogapp.model.response.Blog
import com.example.blogapp.utils.Resources
import com.example.blogapp.utils.UserPreferences
import com.example.blogapp.view.details.DetailsActivity
import com.example.blogapp.viewmodel.HomeViewModel

class SearchFragment : Fragment() {
    private var _binding : FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()
    private  var _listSearchBlogs : MutableList<Blog>? = null
    private  val listSearchBlogs get() = _listSearchBlogs!!
    private lateinit var adapterSearchBlogs : listBlogsAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater,container,false)

        _listSearchBlogs = mutableListOf()
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.clearSearchBlogs()
                if (query != null) {
                    val userPreferences = UserPreferences(requireContext())
                    userPreferences.authToken.asLiveData().observe(viewLifecycleOwner, Observer {
                        viewModel.searchBlogs(it.toString(), query.toString())
                    })
                }else{
                    Toast.makeText(requireContext(), "Blog not found", Toast.LENGTH_SHORT).show()
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        viewModel.searchBlogs.observe(viewLifecycleOwner){
            when(it){
                is Resources.Success -> {
                    listSearchBlogs.clear()
                    val blogs = it.data.blogs
                    blogs.forEach {blog->
                        listSearchBlogs.add(blog)
                    }
                    adapterSearchBlogs = listBlogsAdapter(requireActivity()!!,listSearchBlogs)
                    binding.listview.adapter = adapterSearchBlogs
                }

                is Resources.Error -> {
                    //
                }

                else -> {}
            }
        }

        binding.listview.setOnItemClickListener { _, _, position, _ ->
            val itemBlog = listSearchBlogs[position]
            val intent = Intent(requireActivity(), DetailsActivity::class.java)
            intent.putExtra("id",itemBlog.id.toString())
            startActivity(intent)
        }

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _listSearchBlogs = null
        _binding = null
    }

}