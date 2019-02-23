package com.yutasuz.photo.screen.photolist

import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import com.yutasuz.photo.api.response.FlickrPhotosResultResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PhotoListPresenter(
    override val activityView: PhotoListContract.ActivityView,
    override val view: PhotoListContract.View,
    override val repository: PhotoListContract.Repository
) : PhotoListContract.Presenter {

    data class Pagination(
        val page: Int,
        val pages: Int
    ) {
        fun nextPage() = (page) + 1

        fun hasNextPage(): Boolean {
            return page < pages
        }
    }

    private val compositeDisposable = CompositeDisposable()

    private val itemList = arrayListOf<PhotoListAdapter.Item>()

    private var pagination: Pagination? = null

    private val photoRequestState = PhotoRequestState(repository)

    init {
        val searchItem = PhotoListAdapter.Item.SearchItem(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        photoRequestState.setSearchText(query)
                        requestFirstPage()
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?) : Boolean {
                    if(newText != null && newText.isEmpty()){
                        photoRequestState.clearSearch()
                        requestFirstPageIfNotRequested()
                    }
                    return false
                }

            }
        )
        itemList.add(searchItem)
    }

    override fun onCreateView() {
    }

    override fun onViewCreated() {
        view.initAdapter(itemList)
    }

    override fun onResume() {
        requestFirstPageIfNotRequested()
    }

    override fun onPause() {
        compositeDisposable.clear()
    }

    override fun onDestroyView() {
    }

    override fun onRefresh() {
        requestFirstPage()
    }

    private fun requestFirstPageIfNotRequested() {
        if (photoRequestState.requested){
            removeNotFixItems()
            if(photoRequestState.hasResult()){
                itemList.addAll(photoRequestState.result)
            }
            view.notifyDataSetChanged()

            return
        }

        requestFirstPage()
    }

    private fun requestFirstPage() {
        request(1)
    }

    private fun requestNextPage() {
        val pagination = pagination
        if (pagination == null || !pagination.hasNextPage()) return
        request(pagination.nextPage())
    }

    private fun request(page: Int){
        val single = photoRequestState.request(page) ?: return
        val disposable = single
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    receiveResponse(it)
                    view.hideRefresh()
                },
                {
                    Log.e("flickr_api", "exception", it)
                }
            )

        compositeDisposable.add(disposable)
    }


    private fun receiveResponse(photosResult: FlickrPhotosResultResponse) {
        val photos = photosResult.photos ?: return

        val items = photos.photo
            ?.filterNotNull()
            ?.map {
                val title = it.title ?: ""
                val imageUrl = it.imageUrl ?: ""
                val listener = if (imageUrl.isNotEmpty()) {
                    View.OnClickListener {
                        activityView.showPhotoViewerFragment(imageUrl)
                    }
                } else {
                    View.OnClickListener { }
                }

                PhotoListAdapter.Item.PhotoGridItem(
                    title,
                    imageUrl,
                    listener
                )
            } ?: arrayListOf()

        val receivedPage = photos.page ?: return
        val totalPage = photos.pages ?: return

        pagination = Pagination(receivedPage, totalPage)

        when {
            receivedPage > 1 -> {
                val startPosition = itemList.size
                val itemCount = items.size
                itemList.addAll(items)
                photoRequestState.result.addAll(items)
                view.notifyItemRangeChanged(startPosition, itemCount)
            }
            receivedPage == 1 -> {
                removeNotFixItems()
                itemList.addAll(items)
                photoRequestState.result.clear()
                photoRequestState.result.addAll(items)
                view.notifyDataSetChanged()
            }
            else -> {

            }
        }

        photoRequestState.requested = true
    }

    private fun removeNotFixItems() {
        itemList.removeAll { !it.isFix }
    }
}