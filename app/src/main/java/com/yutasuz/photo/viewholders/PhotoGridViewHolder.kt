package com.yutasuz.photo.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.yutasuz.photo.R
import kotlinx.android.synthetic.main.viewholder_photogrid.view.*

class PhotoGridViewHolder(layoutInflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(layoutInflater.inflate(R.layout.viewholder_photogrid, parent, false)) {

    fun onBind(title: String, imageUrl: String, onClickListener: View.OnClickListener) {
        itemView.title.text = title
        itemView.setOnClickListener(onClickListener)

        Picasso.get()
            .load(imageUrl)
            .resize(1000,0)
            .onlyScaleDown()
            .into(itemView.thumbnail)
    }
}
