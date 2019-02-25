package com.yutasuz.photo.screen.photolist

import androidx.fragment.app.testing.FragmentScenario
import org.junit.After
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.get
import org.koin.test.KoinTest
import org.mockito.Mockito.spy
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
        loadKoinModules(listOf(module(override = true) {
            factory<PhotoListContract.Presenter>(override = true) {(view: PhotoListContract.View) ->
                spy(PhotoListPresenter(view, get()))
            }

            single<PhotoListContract.Repository>(override = true) {
                spy(PhotoListRepository())
            }
        }))
    }

    @After
    fun after() {
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