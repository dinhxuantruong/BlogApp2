package com.example.blogapp.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.blogapp.R
import com.example.blogapp.model.response.Blog
import com.squareup.picasso.Picasso


class customRecyclerView(
    private var clickListener: ClickListener,
    private val activity: Activity, private val listBlogs: MutableList<Blog>) : RecyclerView.Adapter<customRecyclerView.BlogViewHolder>() {

    class BlogViewHolder(view : View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        return BlogViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.blog_item,parent,false))
    }

    override fun getItemCount(): Int {
        return listBlogs.size
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        val image = holder.itemView.findViewById<ImageView>(R.id.imageViewItem)
        val title = holder.itemView.findViewById<TextView>(R.id.txtTitle)
        val time = holder.itemView.findViewById<TextView>(R.id.txtTime)

        holder.itemView.setOnClickListener {
            clickListener.onClickedItem(listBlogs[position])
        }
        Picasso.get().load(listBlogs[position].image_url).into(image)
        title.text = listBlogs[position].title
        time.text = listBlogs[position].human_readable_createAt
    }

    interface ClickListener{
        fun onClickedItem(itemBlog : Blog)
    }
}