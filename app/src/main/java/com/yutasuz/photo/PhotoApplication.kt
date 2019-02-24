package com.yutasuz.photo

import android.app.Application
import com.yutasuz.photo.api.FlickrAPI
import com.yutasuz.photo.screen.MainActivityView
import com.yutasuz.photo.screen.photolist.PhotoListContract
import com.yutasuz.photo.screen.photolist.PhotoListPresenter
import com.yutasuz.photo.screen.photolist.PhotoListRepository
import com.yutasuz.photo.screen.viewer.ViewerContract
import com.yutasuz.photo.screen.viewer.ViewerPresenter
import com.yutasuz.photo.screen.viewer.ViewerRepository
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

class PhotoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        FlickrAPI.init(getString(R.string.flickr_api_key))

        startKoin(
            this, listOf(
                photoListModule,
                viewerModule
            )
        )
    }

    val photoListModule = module {
        factory<PhotoListContract.Presenter> { (acivityView: MainActivityView,
                                                  view: PhotoListContract.View) ->
            PhotoListPresenter(acivityView, view, get())
        }

        single<PhotoListContract.Repository> {
            PhotoListRepository()
        }
    }

    val viewerModule = module {
        factory<ViewerContract.Presenter> { (view: ViewerContract.View) ->
            ViewerPresenter(view, get())
        }

        single<ViewerContract.Repository> {
            ViewerRepository()
        }
    }


}