package com.example.blogapp.adapter

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.blogapp.R
import com.example.blogapp.model.response.Data
import com.squareup.picasso.Picasso

class commentAdapter(
    private val activitty: Activity,
    private val listComment: MutableList<Data>
) : ArrayAdapter<Data>(
    activitty,
    R.layout.layout_comment
) {
    override fun getCount(): Int {
        return listComment.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val rowView = activitty.layoutInflater.inflate(R.layout.layout_comment, parent, false)

        val image = rowView.findViewById<ImageView>(R.id.userAvatar)
        val nameUser = rowView.findViewById<TextView>(R.id.username)
        val comment = rowView.findViewById<TextView>(R.id.commentText)
        val time = rowView.findViewById<TextView>(R.id.commentTime)

        nameUser.text = listComment[position].name
        comment.text = listComment[position].message
        time.text = listComment[position].human_readable_createAt
        Picasso.get().load(listComment[position].profile_photo).into(image)
        return rowView
    }
}