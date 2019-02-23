package com.yutasuz.photo.screen.photolist

import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import com.yutasuz.photo.api.response.FlickrPhotosResultResponse
import io.reactivex.Single
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

    interface RequestState {
        var requested: Boolean
        fun request(page: Int)
        fun setResult(itemList: List<PhotoListAdapter.Item>)
        fun addResult(itemList: List<PhotoListAdapter.Item>)
    }

    inner class SearchRequest(private val text: String) : RequestState {
        override var requested = false


        override fun request(page: Int) = requestFlickrSearch(text, page)
        override fun setResult(itemList: List<PhotoListAdapter.Item>) {}
        override fun addResult(itemList: List<PhotoListAdapter.Item>) {}
    }

    inner class RecentRequest() : RequestState {
        override var requested = false

        val result = arrayListOf<PhotoListAdapter.Item>()

        override fun request(page: Int) = requestFlickrRecent(page)
        override fun setResult(itemList: List<PhotoListAdapter.Item>) {
            result.clear()
            result.addAll(itemList)
        }
        override fun addResult(itemList: List<PhotoListAdapter.Item>) {
            result.addAll(itemList)
        }
    }

    private val compositeDisposable = CompositeDisposable()


    private val itemList = arrayListOf<PhotoListAdapter.Item>()

    private var pagination: Pagination? = null

    private val recentRequest = RecentRequest()
    private var searchRequest: SearchRequest? = null
    private val currentRequest: RequestState
        get() {
            return searchRequest ?: recentRequest
        }

    init {
        val searchItem = PhotoListAdapter.Item.SearchItem(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        searchRequest = SearchRequest(query)
                        requestFirstPage()
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?) : Boolean {
                    if(newText != null && newText.isEmpty()){
                        searchRequest = null
                        requestFirstPageIfNotRequested()
                        removeNotFixItems()
                        itemList.addAll(recentRequest.result)
                        view.notifyDataSetChanged()
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
        if (currentRequest.requested) return
        requestFirstPage()
    }

    private fun requestFirstPage() {
        currentRequest.request(1)
    }

    private fun requestNextPage() {
        val pagination = pagination
        if (pagination == null || !pagination.hasNextPage()) return
        currentRequest.request(pagination.nextPage())
    }

    private fun requestFlickrSearch(text: String, page: Int) {
        val disposable = repository.getFlickrSearch(text, page)
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

    private fun requestFlickrRecent(page: Int) {
        val disposable = repository.getFlickrRecent(page)
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
                currentRequest.addResult(items)
                view.notifyItemRangeChanged(startPosition, itemCount)
            }
            receivedPage == 1 -> {
                removeNotFixItems()
                itemList.addAll(items)
                currentRequest.setResult(items)
                view.notifyDataSetChanged()
            }
            else -> {

            }
        }

        currentRequest.requested = true
    }

    private fun removeNotFixItems() {
        itemList.removeAll { !it.isFix }
    }
}