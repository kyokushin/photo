package com.yutasuz.photo.screen.photolist

import android.util.Log
import android.view.View
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PhotoListPresenter(
    override val activityView: PhotoListContract.ActivityView,
    override val view: PhotoListContract.View,
    override val repository: PhotoListContract.Repository
) : PhotoListContract.Presenter {

    data class Pagination(
        val page: Int,
        val pages: Int
    ) {
        fun nextPage() = (page) + 1

        fun hasNextPage(): Boolean {
            return page < pages
        }
    }

    private val compositeDisposable = CompositeDisposable()

    private val itemList = arrayListOf<PhotoListAdapter.Item>()
    private var requested = false

    private var pagination: Pagination? = null

    override fun onCreateView() {
    }

    override fun onViewCreated() {
        view.initAdapter(itemList)
    }

    override fun onResume() {
        if (requested) return
        requestFirstPage()
    }

    override fun onPause() {
        compositeDisposable.clear()
    }

    override fun onDestroyView() {
    }

    override fun onRefresh() {
        requestFirstPage()
    }

    private fun requestFirstPage(){
        requestFlickrRecent(1)
    }

    private fun requestNextPage(){
        val pagination = pagination
        if(pagination == null || !pagination.hasNextPage()) return
        requestFlickrRecent(pagination.nextPage())
    }

    private fun requestFlickrRecent(page: Int) {
        val disposable = repository.getFlickrRecent(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { photoResult ->

                    val photos = photoResult.photos ?: return@subscribe

                    val items = photos.photo
                        ?.filterNotNull()
                        ?.map {
                            val title = it.title ?: ""
                            val imageUrl = it.imageUrl ?: ""
                            val listener = if (imageUrl.isNotEmpty()) {
                                View.OnClickListener {
                                    activityView.showPhotoViewerFragment(imageUrl)
                                }
                            } else {
                                View.OnClickListener { }
                            }

                            PhotoListAdapter.Item.PhotoGridItem(
                                title,
                                imageUrl,
                                listener
                            )
                        } ?: arrayListOf()

                    val receivedPage = photos.page ?: return@subscribe
                    val totalPage = photos.pages ?: return@subscribe

                    pagination = Pagination(receivedPage, totalPage)

                    when {
                        receivedPage > 1 -> {
                            val startPosition = itemList.size
                            val itemCount = items.size
                            itemList.addAll(items)
                            view.notifyItemRangeChanged(startPosition, itemCount)
                        }
                        receivedPage == 1 -> {
                            itemList.clear()
                            itemList.addAll(items)
                            view.notifyDataSetChanged()
                        }
                        else -> {

                        }
                    }

                    requested = true
                },
                {
                    Log.e("flickr_api", "exception", it)
                }
            )

        compositeDisposable.add(disposable)
    }
}