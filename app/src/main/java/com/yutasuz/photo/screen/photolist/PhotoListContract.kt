package com.yutasuz.photo.screen.photolist

import com.yutasuz.photo.api.response.FlickrPhotosResultResponse
import com.yutasuz.photo.screen.MainActivityView
import io.reactivex.Single
import org.koin.dsl.module.applicationContext
import org.koin.dsl.module.module

/**
 * Flickrから取得した画像を一覧表示するためのクラス間でやり取りする内容を定義
 */

interface PhotoListContract {

    interface View {
        val presenter: Presenter

        fun initAdapter(items: List<PhotoListAdapter.Item>)

        fun notifyDataSetChanged()

        fun notifyItemRangeChanged(positionStart: Int, itemCount: Int)

        fun hideRefresh()
    }

    interface Presenter {
        val activityView: MainActivityView
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