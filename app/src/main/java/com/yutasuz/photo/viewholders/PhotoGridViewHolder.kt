package com.yutasuz.photo.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.yutasuz.photo.R
import com.yutasuz.photo.screen.photolist.PhotoListAdapter
import kotlinx.android.synthetic.main.viewholder_photogrid.view.*

class PhotoGridViewHolder(layoutInflater: LayoutInflater, parent: ViewGroup) :
    PhotoListAdapter.CleanableViewHolder(layoutInflater.inflate(R.layout.viewholder_photogrid, parent, false)) {

    override fun onDetachedFromRecyclerView() = cancelRequest()
    override fun onViewDetachedFromWindow() = cancelRequest()
    override fun onViewRecycled() = cancelRequest()

    private fun cancelRequest() =
        Picasso.get()
            .cancelRequest(itemView.thumbnail)

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
