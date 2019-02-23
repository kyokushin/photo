package com.yutasuz.photo.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.yutasuz.photo.R
import kotlinx.android.synthetic.main.viewholder_search.view.*

class SearchViewHolder(layoutInflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(layoutInflater.inflate(R.layout.viewholder_search, parent, false)) {

    fun onBind(onQueryTextListener: SearchView.OnQueryTextListener) {

        itemView.search.setOnQueryTextListener(onQueryTextListener)

    }
}

