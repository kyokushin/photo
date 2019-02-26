package com.yutasuz.photo.screen.photolist

import com.yutasuz.photo.BaseSchedulers
import com.yutasuz.photo.api.FlickrAPI
import com.yutasuz.photo.api.response.FlickrPhotosResponse
import com.yutasuz.photo.api.response.FlickrPhotosResultResponse
import io.mockk.*
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.*
import org.junit.Test
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module.module
import org.koin.test.KoinTest
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.get
import org.koin.standalone.inject
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class PhotoListPresenterTest : KoinTest {

    @Test
    fun onResume実行時に未リクエストだとrequestFirstPageが呼ばれる() {

        loadKoinModules(module {

            factory {
                spyk(PhotoRequestState(get()))
            }

            single<PhotoListContract.Repository> {
                spyk(PhotoListRepository())
            }

            single<FlickrAPI.FlickrService> {
                mockk()
            }

            single<BaseSchedulers> {
                object : BaseSchedulers {
                    override val io: Scheduler = Schedulers.trampoline()
                    override val main: Scheduler = Schedulers.trampoline()
                }
            }
        })

        val flickrService: FlickrAPI.FlickrService = get()
        every { flickrService.getRecent(1) } returns Single.just(
            FlickrPhotosResultResponse(
                FlickrPhotosResponse(1, 1, 100, 0, arrayListOf())
            )
        )

        val view = mockk<PhotoListContract.View>()
        every { view.notifyDataSetChanged() } answers { nothing }
        every { view.hideRefresh() } answers { nothing }
        val presenter = spyk(PhotoListPresenter(view), recordPrivateCalls = true)
        presenter.onResume()

        verifyOrder{
            presenter["requestFirstPageIfNotRequested"]()
            presenter["requestFirstPage"]()
            presenter["request"](1)
        }

    }
}