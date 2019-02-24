package com.yutasuz.photo.screen

import com.yutasuz.photo.api.response.FlickrPhotoResponse

interface MainActivityView {
    fun showPhotoViewerFragment(photoResponse: FlickrPhotoResponse)
}