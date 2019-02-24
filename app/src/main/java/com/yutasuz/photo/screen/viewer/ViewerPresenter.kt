package com.yutasuz.photo.screen.viewer

import android.graphics.Bitmap
import android.util.Log
import android.util.Size

class ViewerPresenter(
    override val view: ViewerContract.View,
    override val repository: ViewerContract.Repository
) : ViewerContract.Presenter {

    var scale: Float = 1.0f
    var defaultScale: Float = 1.0f
    var movedX = 0f
    var movedY = 0f
    var imagePositionX = 0f
    var imagePositionY = 0f
    var imageSize = Size(0, 0)

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
        setImageScaleAndPosition()
    }

    override fun onBitmapLoaded(bitmap: Bitmap?) {
        bitmap ?: return
        view.setImageBitmap(bitmap)
        calcDefaultScale(bitmap)
        setDefaultImageScaleAndPosition()
        setImageScaleAndPosition()
    }

    override fun onDoubleTaped() {
        setDefaultImageScaleAndPosition()
        setImageScaleAndPosition()
    }

    override fun onScrolled(moveX: Float, moveY: Float) {
        moveImagePosition(-moveX, -moveY)
        setImageScaleAndPosition()
    }

    private fun calcDefaultScale(bitmap: Bitmap) {
        val size = view.getImageViewSize

        val scaleWidth = size.width.toFloat() / bitmap.width
        val scaleHeight = size.height.toFloat() / bitmap.height

        defaultScale = if (scaleWidth < scaleHeight) scaleWidth else scaleHeight
        imageSize = Size(bitmap.width, bitmap.height)
    }

    private fun setDefaultImageScaleAndPosition() {
        scale = defaultScale
        resetImagePosition()
    }

    private fun calcImagePosition() {
        val size = view.getImageViewSize
        val imageX = (size.width - imageSize.width * scale) / 2 + movedX
        val imageY = (size.height - imageSize.height * scale) / 2 + movedY
        imagePositionX = imageX
        imagePositionY = imageY
    }

    private fun moveImagePosition(moveX: Float, moveY: Float) {
        movedX += moveX
        movedY += moveY
    }

    private fun resetImagePosition() {
        movedX = 0f
        movedY = 0f
    }

    private fun setImageScaleAndPosition(){
        calcImagePosition()
        view.setImageScaleAndPosition(scale, imagePositionX, imagePositionY)
    }
}