package com.yutasuz.photo.screen.photolist

import androidx.fragment.app.testing.launchFragment
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifySequence
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

            single<PhotoListContract.Repository>(override = true) {
                spyk(PhotoListRepository(), recordPrivateCalls = true)
            }
        }))

        val scenario = launchFragment<PhotoListFragment>()

        scenario.onFragment {
            val mockPresenter = it.presenter
            verify(exactly = 1) {
                mockPresenter["requestFirstPageIfNotRequested"]()
                mockPresenter["request"](1)
                mockPresenter["requestFirstPage"]()
            }
        }
    }
}