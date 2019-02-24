package com.yutasuz.photo.screen.viewer

import android.graphics.Bitmap

class ViewerPresenter(
    override val view: ViewerContract.View,
    override val repository: ViewerContract.Repository
) : ViewerContract.Presenter {

    var scale: Float = 1.0f

    override fun onViewCreatedStart() {
    }

    override fun onViewCreatedEnd() {
        val imageUrl = view.photoResponse?.imageUrlLarge ?: return
        view.setImageUrl(imageUrl)
    }

    override fun onResume() {
    }

    override fun onPause() {
    }

    override fun onDestroyView() {
    }

    override fun onScaleChanged(scaleFactor: Float) {
        scale *= scaleFactor
        view.setImageScale(scale)
    }

    override fun onBitmapLoaded(bitmap: Bitmap?) {

        bitmap ?: return

        val size = view.getImageViewSize

        val scaleWidth = size.width.toFloat() / bitmap.width
        val scaleHeight = size.height.toFloat() / bitmap.height

        scale = if(scaleWidth < scaleHeight) scaleWidth else scaleHeight

        view.setImageScale(scale)
        view.setImageBitmap(bitmap)
    }
}