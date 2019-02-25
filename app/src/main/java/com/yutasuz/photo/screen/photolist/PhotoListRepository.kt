package com.yutasuz.photo.screen.photolist

import com.yutasuz.photo.api.FlickrAPI
import com.yutasuz.photo.api.response.FlickrPhotosResultResponse
import io.reactivex.Single

/**
 * データの入出力を担当する
 * ロジックは含まず、単純な入出力のみを担当する
 */
open class PhotoListRepository: PhotoListContract.Repository {

    override fun getFlickrSearch(text: String, page: Int): Single<FlickrPhotosResultResponse> {
        FlickrAPI.service?.let{
            return it.getSearch(text, page)
        }

        return Single.just(FlickrPhotosResultResponse(null))
    }

    override fun getFlickrRecent(page: Int): Single<FlickrPhotosResultResponse> {
        FlickrAPI.service?.let{
            return it.getRecent(page)
        }

        return Single.just(FlickrPhotosResultResponse(null))
    }

}