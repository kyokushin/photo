package com.yutasuz.photo.screen.photolist

import androidx.fragment.app.testing.launchFragment
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4ClassRunner::class)
class PhotoListFragmentTest : KoinTest {

    companion object {
        @JvmStatic
        @BeforeClass
        fun start() {
        }
    }

    @Before
    fun before() {
    }

    @After
    fun after() {
    }

    @Test
    fun PhotoListFragment起動時にPresenterのonViewCreatedとonResumeが呼ばれる() {
        loadKoinModules(listOf(module(override = true) {
            factory<PhotoListContract.Presenter>(override = true) { (view: PhotoListContract.View) ->
                spyk(PhotoListPresenter(view, get()))
            }

            factory(override = true) {
                val photoRequestState = spyk(PhotoRequestState(get()), recordPrivateCalls = true)
                photoRequestState
            }

            single<PhotoListContract.Repository>(override = true) {
                spyk(PhotoListRepository())
            }
        }))

        val scenario = launchFragment<PhotoListFragment>()

        scenario.onFragment {
            val mockPresenter = it.presenter
            verifySequence {
                mockPresenter.onCreateView()
                mockPresenter.onViewCreated()
                mockPresenter.view
                mockPresenter.onResume()
            }
        }
    }

    @Test
    fun PhotoListFragment起動時にPresenterがrequestを呼ぶ() {
        loadKoinModules(listOf(module(override = true) {
            factory<PhotoListContract.Presenter>(override = true) { (view: PhotoListContract.View) ->
                spyk(PhotoListPresenter(view, get()), recordPrivateCalls = true)
            }

            factory(override = true) {
                val photoRequestState = spyk(PhotoRequestState(get()), recordPrivateCalls = true)
                photoRequestState
            }

            single<PhotoListContract.Repository>(override = true) {
                spyk(PhotoListRepository(), recordPrivateCalls = true)
            }
        }))

        val scenario = launchFragment<PhotoListFragment>()

        scenario.onFragment {
            val mockPresenter = it.presenter
            verifyOrder {
                mockPresenter["requestFirstPageIfNotRequested"]()
                mockPresenter["requestFirstPage"]()
                mockPresenter["request"](1)
            }
        }
    }

    @Test
    fun PhotoListFragment起動時にリクエスト済みならPresenterがrequestを呼ばない() {
        loadKoinModules(listOf(module(override = true) {
            factory<PhotoListContract.Presenter>(override = true) { (view: PhotoListContract.View) ->
                spyk(PhotoListPresenter(view, get()), recordPrivateCalls = true)
            }

            factory(override = true) {
                val photoRequestState = spyk(PhotoRequestState(get()), recordPrivateCalls = true)

                every{ photoRequestState.requested } returns true
                photoRequestState
            }

            single<PhotoListContract.Repository>(override = true) {
                spyk(PhotoListRepository(), recordPrivateCalls = true)
            }
        }))

        val scenario = launchFragment<PhotoListFragment>()

        scenario.onFragment {
            val mockPresenter = it.presenter
            verify(exactly = 1){
                mockPresenter["requestFirstPageIfNotRequested"]()
            }
            verify(exactly = 0){
                mockPresenter["request"](1)
                mockPresenter["requestFirstPage"]()
            }
        }
    }
}