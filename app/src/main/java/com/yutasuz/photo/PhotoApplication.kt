package com.yutasuz.photo

import android.app.Application
import com.yutasuz.photo.api.FlickrAPI
import com.yutasuz.photo.screen.photolist.PhotoListContract
import com.yutasuz.photo.screen.photolist.PhotoListPresenter
import com.yutasuz.photo.screen.photolist.PhotoListRepository
import com.yutasuz.photo.screen.photolist.PhotoRequestState
import com.yutasuz.photo.screen.viewer.ViewerContract
import com.yutasuz.photo.screen.viewer.ViewerPresenter
import com.yutasuz.photo.screen.viewer.ViewerRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class PhotoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        FlickrAPI.init(getString(R.string.flickr_api_key))

        startKoin(
            this, listOf(
                photoListModule,
                viewerModule,
                flickrAPIModule
            )
        )
    }

    val photoListModule = module {
        factory<PhotoListContract.Presenter> { (view: PhotoListContract.View) ->
            PhotoListPresenter(view, get())
        }

        factory {
            PhotoRequestState(get())
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

    val flickrAPIModule = module {
        single<FlickrAPI.FlickrService> { (apiKey: String) ->
            val httpClient = OkHttpClient.Builder()
                .addInterceptor(FlickrAPI.RequestInterceptor(apiKey))
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .addNetworkInterceptor(HttpLoggingInterceptor())
                .build()

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(FlickrAPI.BASE_URL)
                .client(httpClient)
                .build()

            retrofit.create(FlickrAPI.FlickrService::class.java)
        }
    }
}