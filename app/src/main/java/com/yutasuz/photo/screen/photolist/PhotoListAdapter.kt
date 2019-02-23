package com.yutasuz.photo.screen.photolist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.yutasuz.photo.viewholders.PhotoGridViewHolder
import com.yutasuz.photo.viewholders.SearchViewHolder

class PhotoListAdapter(context: Context, private val itemList: List<Item>) :
    RecyclerView.Adapter<PhotoListAdapter.CleanableViewHolder>() {

    abstract class CleanableViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        abstract fun onDetachedFromRecyclerView()
        abstract fun onViewDetachedFromWindow()

        abstract fun onViewRecycled()
    }

    enum class Type {

        Search {
            override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup) =
                SearchViewHolder(inflater, parent)
        },
        PhotoGrid {
            override fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup) =
                PhotoGridViewHolder(inflater, parent)
        };

        abstract fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup): CleanableViewHolder
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CleanableViewHolder {
        return Type.values()[viewType].createViewHolder(layoutInflater, parent)
    }

    override fun getItemCount() = itemList.size

    override fun getItemViewType(position: Int) = itemList[position].type.ordinal

    override fun onBindViewHolder(holder: CleanableViewHolder, position: Int) {

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

    override fun onViewDetachedFromWindow(holder: CleanableViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.onViewDetachedFromWindow()
    }

    override fun onViewRecycled(holder: CleanableViewHolder) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)

        val size = itemCount
        0.until(size).forEach { index ->
            val viewHolder = recyclerView.findViewHolderForAdapterPosition(index) as CleanableViewHolder? ?: return
            viewHolder.onDetachedFromRecyclerView()
        }
    }
}