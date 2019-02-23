package com.yutasuz.photo.screen.photolist

import com.yutasuz.photo.api.response.FlickrPhotosResultResponse
import io.reactivex.Single

interface PhotoListContract {

    interface ActivityView {
        fun showPhotoViewerFragment(imageUrl: String)
        fun showPhotoSearchResultFragment(keyword: String)
    }

    interface View {
        var presenter: Presenter?

        fun initAdapter(items: List<PhotoListAdapter.Item>)

        fun notifyDataSetChanged()

        fun notifyItemRangeChanged(positionStart: Int, itemCount: Int)

        fun hideRefresh()
    }

    interface Presenter {
        val activityView: ActivityView
        val view: View
        val repository: Repository

        fun onCreateView()
        fun onViewCreated()

        fun onResume()

        fun onPause()

        fun onDestroyView()

        fun onRefresh()

    }

    interface Repository {

        fun getFlickrSearch(text: String, page: Int): Single<FlickrPhotosResultResponse>

        fun getFlickrRecent(page: Int): Single<FlickrPhotosResultResponse>
    }
}