package com.yutasuz.photo.screen.viewer

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Size
import com.yutasuz.photo.api.response.FlickrPhotoResponse

/**
 * Flickrの画像を表示するためのクラス間でやり取りする内容を定義
 */
interface ViewerContract {

    interface View {
        val presenter: Presenter
        val photoResponse: FlickrPhotoResponse?
        val getImageViewSize: Size

        fun setImageUrl(imageUrl:String)
        fun setImageBitmap(bitmap: Bitmap?)

        fun setImageScaleAndPosition(scale: Float, positionX: Float, positionY: Float)

        fun showProgress()
        fun hideProgress()
    }

    interface Presenter {
        val view: View
        val repository: Repository

        fun onViewCreatedStart()
        fun onViewCreatedEnd()

        fun onResume()

        fun onPause()

        fun onDestroyView()

        fun onScaleChanged(scaleFactor: Float)

        fun onDoubleTaped()
        fun onScrolled(moveX: Float, moveY: Float)

        fun onBitmapLoaded(bitmap: Bitmap?)
        fun onBitmapLoadFailed()
    }

    interface Repository {

    }

}