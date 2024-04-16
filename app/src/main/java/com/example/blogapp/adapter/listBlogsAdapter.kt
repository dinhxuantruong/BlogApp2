package com.example.blogapp.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.blogapp.R
import com.example.blogapp.model.response.Blog
import com.example.blogapp.model.response.Blogs
import com.squareup.picasso.Picasso

class listBlogsAdapter(private val activity: Activity, private val listBlogs: MutableList<Blog>) :
    ArrayAdapter<Blog>(
        activity,
        R.layout.blog_item
    ) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val context = activity.layoutInflater
        val rowView = context.inflate(R.layout.blog_item,parent,false)
        val image = rowView.findViewById<ImageView>(R.id.imageViewItem)
        val title = rowView.findViewById<TextView>(R.id.txtTitle)
        val time = rowView.findViewById<TextView>(R.id.txtTime)

        title.text = listBlogs[position].title
        time.text = listBlogs[position].human_readable_createAt
        Picasso.get().load(listBlogs[position].image_url).into(image)
        return rowView
    }

    override fun getCount(): Int {
        return listBlogs.size
    }
}