package com.yutasuz.photo.screen.photolist

import com.yutasuz.photo.api.FlickrAPI
import com.yutasuz.photo.api.response.FlickrPhotosResultResponse
import io.reactivex.Single

class PhotoListRepository: PhotoListContract.Repository {


    override fun getFlickrSearch(keyword: String, page: Int): Single<FlickrPhotosResultResponse> {
        FlickrAPI.service?.let{
            return it.getSearch(keyword, page)
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