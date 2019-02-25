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
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.startKoin
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.standalone.get
import org.koin.test.KoinTest
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
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
        val mockPresenter = mock(PhotoListContract.Presenter::class.java)
        val mockRepository = mock(PhotoListContract.Repository::class.java)
        loadKoinModules(listOf(module (override = true){
            factory<PhotoListContract.Presenter> {
                mockPresenter
            }

            single<PhotoListContract.Repository> {
                mockRepository
            }
        }))
    }

    @After
    fun after(){
//        stopKoin()
    }

    @Test
    fun test_onCreateView_call_presenter_onCreateView() {

        val scenario = FragmentScenario.launch(PhotoListFragment::class.java)

        val mockPresenter: PhotoListContract.Presenter = get()
        val mockRepository: PhotoListContract.Repository = get()


        verify(mockPresenter).onViewCreated()
    }
}