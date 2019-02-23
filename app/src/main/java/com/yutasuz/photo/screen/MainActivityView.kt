package com.yutasuz.photo.screen

interface MainActivityView {
    fun showPhotoViewerFragment(imageUrl: String)
    fun showPhotoSearchResultFragment(keyword: String)
}