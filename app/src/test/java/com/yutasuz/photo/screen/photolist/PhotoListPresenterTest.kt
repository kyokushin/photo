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
import org.junit.Before
import org.junit.BeforeClass
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

    companion object {

        @BeforeClass
        @JvmStatic
        fun before() {
            loadKoinModules(module {

                single {
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
        }
    }

    lateinit var view: PhotoListContract.View

    @Before
    fun setup(){
        val flickrService: FlickrAPI.FlickrService = get()
        every { flickrService.getRecent(1) } returns Single.just(
            FlickrPhotosResultResponse(
                FlickrPhotosResponse(1, 1, 100, 0, arrayListOf())
            )
        )

        val requestState: PhotoRequestState = get()
        every { requestState.requested } returns false

        view = mockk<PhotoListContract.View>()
        every { view.notifyDataSetChanged() } answers { nothing }
        every { view.hideRefresh() } answers { nothing }
    }

    @Test
    fun onResume実行時に未リクエストだとrequestFirstPageが呼ばれる() {

        val presenter = spyk(PhotoListPresenter(view), recordPrivateCalls = true)
        presenter.onResume()

        verifyOrder {
            presenter["requestFirstPageIfNotRequested"]()
            presenter["requestFirstPage"]()
            presenter["request"](1)
        }

    }

    @Test
    fun onResume実行時にリクエスト済みだとrequestFirstPageが呼ばれない() {

        val requestState: PhotoRequestState = get()
        every { requestState.requested } returns true

        val presenter = spyk(PhotoListPresenter(view), recordPrivateCalls = true)
        presenter.onResume()

        verify(exactly = 1) {
            presenter["requestFirstPageIfNotRequested"]()
        }

        verify(exactly = 0) {
            presenter["requestFirstPage"]()
            presenter["request"](any() as Int)
        }

    }
}