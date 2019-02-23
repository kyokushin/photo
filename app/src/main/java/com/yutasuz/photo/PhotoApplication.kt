package com.yutasuz.photo

import android.app.Application
import com.yutasuz.photo.api.FlickrAPI

class PhotoApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        FlickrAPI.init(getString(R.string.flickr_api_key))
    }
}