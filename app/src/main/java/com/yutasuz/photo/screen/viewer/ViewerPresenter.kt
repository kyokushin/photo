package com.yutasuz.photo.screen.viewer

import android.graphics.Bitmap
import android.util.Log

class ViewerPresenter(
    override val view: ViewerContract.View,
    override val repository: ViewerContract.Repository
) : ViewerContract.Presenter {

    var scale: Float = 1.0f
    var defaultScale: Float = 1.0f

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
        Log.d("onScaleChanged", "$scale $scaleFactor")
        view.setImageScale(scale)
    }

    override fun onBitmapLoaded(bitmap: Bitmap?) {
        bitmap ?: return
        view.setImageBitmap(bitmap)
        calcDefaultScale(bitmap)
        setDefaultImageScale()
    }

    override fun onDoubleTaped() {
        setDefaultImageScale()
    }

    private fun calcDefaultScale(bitmap: Bitmap) {
        val size = view.getImageViewSize

        val scaleWidth = size.width.toFloat() / bitmap.width
        val scaleHeight = size.height.toFloat() / bitmap.height

        defaultScale = if (scaleWidth < scaleHeight) scaleWidth else scaleHeight
    }

    private fun setDefaultImageScale() {
        scale = defaultScale
        view.setImageScale(scale)
    }
}