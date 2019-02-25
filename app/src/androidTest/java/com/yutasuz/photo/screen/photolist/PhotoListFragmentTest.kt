package com.yutasuz.photo.screen.photolist

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.FragmentScenario.launchInContainer
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.yutasuz.photo.screen.MainActivity
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.standalone.get
import org.koin.test.KoinTest
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PhotoListFragmentTest : KoinTest {

    companion object {
        @JvmStatic
        @BeforeClass
        fun start() {
            MockitoAnnotations.initMocks(this)
        }
    }

    @Before
    fun before() {
        loadKoinModules(listOf(module(override = true) {
            factory<PhotoListContract.Presenter>(override = true) {(view: PhotoListContract.View) ->
//                mock(PhotoListContract.Presenter::class.java)
                spy(PhotoListPresenter(view, get()))
            }

            single<PhotoListContract.Repository>(override = true) {
//                mock(PhotoListContract.Repository::class.java)
                spy(PhotoListRepository())
            }
        }))
    }

    @After
    fun after() {
//        stopKoin()
    }

    @Test
    fun test_onCreateView_call_presenter_onCreateView() {

        val scenario = FragmentScenario.launch(PhotoListFragment::class.java)

        scenario.onFragment {
            val mockPresenter = it.presenter
            verify(mockPresenter).onViewCreated()
        }

        val mockRepository: PhotoListContract.Repository = get()


    }
}