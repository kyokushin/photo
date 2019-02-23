package com.yutasuz.photo.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.yutasuz.photo.R
import com.yutasuz.photo.screen.photolist.PhotoListAdapter
import kotlinx.android.synthetic.main.viewholder_search.view.*

class SearchViewHolder(layoutInflater: LayoutInflater, parent: ViewGroup) :
    PhotoListAdapter.CleanableViewHolder(layoutInflater.inflate(R.layout.viewholder_search, parent, false)) {

    override fun onDetachedFromRecyclerView() {}
    override fun onViewDetachedFromWindow() {}
    override fun onViewRecycled() {}

    fun onBind(onQueryTextListener: SearchView.OnQueryTextListener) {

        itemView.search.setOnQueryTextListener(onQueryTextListener)

    }
}

