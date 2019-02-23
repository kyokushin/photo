package com.yutasuz.photo.screen.viewer

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Size
import com.yutasuz.photo.api.response.FlickrPhotoResponse

interface ViewerContract {

    interface View {
        val presenter: Presenter
        val photoResponse: FlickrPhotoResponse?
        val getImageViewSize: Size

        fun setImageUrl(imageUrl:String)
        fun setImageBitmap(bitmap: Bitmap?)

        fun setImageScale(scale: Float)
    }

    interface Presenter {
        val view: View
        val repository: Repository

        fun onViewCreated()

        fun onResume()

        fun onPause()

        fun onDestroyView()

        fun onScaleChanged(scaleFactor: Float)

        fun onBitmapLoaded(bitmap: Bitmap?)
    }

    interface Repository {

    }

}