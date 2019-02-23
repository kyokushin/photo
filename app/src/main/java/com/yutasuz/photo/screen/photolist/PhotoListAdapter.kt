package com.yutasuz.photo.screen.photolist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.yutasuz.photo.viewholders.PhotoGridViewHolder
import com.yutasuz.photo.viewholders.SearchViewHolder

class PhotoListAdapter(context: Context, val itemList: List<Item>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class Type {

        Search {
            override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup) =
                SearchViewHolder(inflater, parent)
        },
        PhotoGrid {
            override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup) =
                PhotoGridViewHolder(inflater, parent)
        };

        abstract fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder
    }

    interface Item {
        val type: Type
        val isFix: Boolean

        class SearchItem(
            val onQueryTextListener: SearchView.OnQueryTextListener
        ) : Item {
            override val type = Type.Search
            override val isFix = true
        }

        data class PhotoGridItem(
            val title: String,
            val imageUrl: String,
            val onClickListener: View.OnClickListener
        ) : Item {
            override val type = Type.PhotoGrid
            override val isFix = false
        }
    }

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return Type.values()[viewType].createViewHolder(layoutInflater, parent)
    }

    override fun getItemCount() = itemList.size

    override fun getItemViewType(position: Int) = itemList[position].type.ordinal

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = itemList[position]
        val viewType = item.type
        when (viewType) {
            Type.Search -> {
                holder as SearchViewHolder
                item as Item.SearchItem

                holder.onBind(item.onQueryTextListener)
            }
            Type.PhotoGrid -> {
                holder as PhotoGridViewHolder
                item as Item.PhotoGridItem

                holder.onBind(item.title, item.imageUrl, item.onClickListener)
            }
        }
    }
}