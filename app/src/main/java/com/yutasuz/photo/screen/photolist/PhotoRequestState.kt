package com.yutasuz.photo.screen.photolist

import com.yutasuz.photo.api.response.FlickrPhotosResultResponse
import io.reactivex.Single

class PhotoRequestState(val repository: PhotoListContract.Repository) {

    interface RequestState {
        var requested: Boolean
        val result: ArrayList<PhotoListAdapter.Item>

        fun request(page: Int): Single<FlickrPhotosResultResponse>
        fun setResult(itemList: List<PhotoListAdapter.Item>)
        fun addResult(itemList: List<PhotoListAdapter.Item>)
    }

    inner class SearchRequest(private val text: String) : RequestState {
        override var requested = false

        override val result = arrayListOf<PhotoListAdapter.Item>()

        override fun request(page: Int) = repository.getFlickrSearch(text, page)
        override fun setResult(itemList: List<PhotoListAdapter.Item>) {}
        override fun addResult(itemList: List<PhotoListAdapter.Item>) {}
    }

    inner class RecentRequest() : RequestState {
        override var requested = false

        override val result = arrayListOf<PhotoListAdapter.Item>()

        override fun request(page: Int) = repository.getFlickrRecent(page)
        override fun setResult(itemList: List<PhotoListAdapter.Item>) {
            result.clear()
            result.addAll(itemList)
        }

        override fun addResult(itemList: List<PhotoListAdapter.Item>) {
            result.addAll(itemList)
        }
    }

    private val recentRequest = RecentRequest()
    private var searchRequest: SearchRequest? = null
    private val currentRequest: RequestState
        get() {
            return searchRequest ?: recentRequest
        }

    val result
        get() = currentRequest.result

    var requested = currentRequest.requested

    fun request(page: Int) = currentRequest.request(page)


    fun hasResult() = currentRequest.result.isNotEmpty()

    fun clearSearch() {
        searchRequest = null
    }

    fun setSearchText(text: String) {
        searchRequest = SearchRequest(text)
    }
}
