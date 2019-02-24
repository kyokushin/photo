package com.yutasuz.photo.screen.photolist

import com.yutasuz.photo.api.response.FlickrPhotosResultResponse
import io.reactivex.Single

/**
 * 写真のリスト表示画面のリクエストロジック
 * Presenterからリクエストロジックを分離した
 *
 * 同じFragmentから初期状態ではRecentのAPIを、検索キーワードが指定された場合はSearchのAPIを
 * リクエストするので抽象化してまとめた
 *
 * インスタンスに保持するだけの簡単なキャッシュを備えている
 */
class PhotoRequestState(val repository: PhotoListContract.Repository) {

    /**
     * リクエスト処理を抽象化したinterface
     */
    interface RequestState {
        //リクエスト済みか否か
        var requested: Boolean
        // リクエスト結果を保持する簡単なキャッシュ。再表示時に使う
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
        get() = currentRequest.result // get()実行時にcurrentRequestのresultを使いたいので代入していない

    var requested
        get() = currentRequest.requested
        set(value) {
            currentRequest.requested = value
        }

    fun request(page: Int) = currentRequest.request(page)


    fun hasResult() = currentRequest.result.isNotEmpty()

    fun clearSearch() {
        searchRequest = null
    }

    fun setSearchText(text: String) {
        searchRequest = SearchRequest(text)
    }
}
