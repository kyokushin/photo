package com.yutasuz.photo.screen.viewer

import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.*
import android.widget.ImageView
import androidx.core.view.MotionEventCompat
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import com.yutasuz.photo.R
import com.yutasuz.photo.api.response.FlickrPhotoResponse
import kotlinx.android.synthetic.main.fragment_viewer.*
import java.lang.Exception

/**
 * 画像表示のViewを担当するクラス
 *
 * Presenterへのイベント伝達、Presenterから操作されることを主とする
 * ロジックはPresenterへ集約している
 */
class ViewerFragment : Fragment(), ViewerContract.View {

    companion object {

        const val TAG = "viewer"

        private const val ARG_PHOTO = "photo"

        fun create(photoResponse: FlickrPhotoResponse) = ViewerFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_PHOTO, photoResponse)
            }
        }
    }

    override lateinit var presenter: ViewerContract.Presenter

    override val getImageViewSize: Size
        get() = Size(image.width, image.height)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = ViewerPresenter(this, ViewerRepository())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_viewer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onViewCreatedStart()
        initView()
        presenter.onViewCreatedEnd()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroyView()
        Picasso.get()
            .cancelRequest(target)
    }

    override val photoResponse
        get() = arguments?.getParcelable<FlickrPhotoResponse>(ARG_PHOTO)

    /**
     * 画像データ取得後に表示するスケールを計算するためにTargetを使って間接的に画像表示をしている
     */
    private val target = object : Target {
        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            image.scaleType = ImageView.ScaleType.CENTER_INSIDE
            image.setImageDrawable(placeHolderDrawable)
        }

        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
            image.scaleType = ImageView.ScaleType.CENTER_INSIDE
            image.setImageDrawable(errorDrawable)
        }

        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
            image.scaleType = ImageView.ScaleType.MATRIX
            //BitmapはPresenterへ渡し、表示サイズの計算を行う
            presenter.onBitmapLoaded(bitmap)
        }
    }

    override fun setImageUrl(imageUrl: String) {
        Picasso.get()
            .load(imageUrl)
            .resize(3000, 0)
            .error(R.drawable.ic_report_problem_black_24dp)
            .onlyScaleDown()
            .into(target)
    }

    override fun setImageBitmap(bitmap: Bitmap?) {
        image.setImageBitmap(bitmap)
    }

    override fun setImageScaleAndPosition(scale: Float, positionX: Float, positionY: Float) {
        val matrix = image.imageMatrix
        matrix.setScale(scale, scale)
        matrix.postTranslate(positionX, positionY)
        Log.d("scale", matrix.toString())
        image.imageMatrix = matrix
        image.invalidate()
    }

    private fun initView() {

        //ジェスチャーの設定
        val gestureListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                presenter.onScaleChanged(detector?.scaleFactor ?: 1.0f)
                return true
            }
        }

        val gestureListener2 = object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent?): Boolean {
                presenter.onDoubleTaped()
                return true
            }

            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                presenter.onScrolled(distanceX, distanceY)
                return true
            }

            override fun onDown(e: MotionEvent?): Boolean {
                return true
            }
        }

        val gestureDetector = ScaleGestureDetector(context, gestureListener)
        val gestureDetector2 = GestureDetector(context, gestureListener2)
            .apply {
                setOnDoubleTapListener(gestureListener2)
            }
        image.setOnTouchListener { v, event ->
            if(event.pointerCount > 1) {
                gestureDetector.onTouchEvent(event)
            }
            else {
                gestureDetector2.onTouchEvent(event)
            }
        }
    }
}