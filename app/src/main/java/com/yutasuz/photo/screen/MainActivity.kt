package com.yutasuz.photo.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity;
import com.yutasuz.photo.R
import com.yutasuz.photo.screen.photolist.PhotoListFragment

import kotlinx.android.synthetic.main.activity_main.*
import org.koin.dsl.module.Module

class MainActivity : AppCompatActivity(), MainActivityView {

    private val module: Module = org.koin.dsl.module.applicationContext {
        factory {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initFragment()
    }

    private fun initView() {
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    }

    private fun initFragment() {
        supportFragmentManager.findFragmentByTag(PhotoListFragment.TAG) ?: run {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, PhotoListFragment.create(), PhotoListFragment.TAG)
                .commitNow()
        }

    }

    override fun showPhotoViewerFragment(imageUrl: String) {
        FragmentNavigator.showPhotoViewerFragment(supportFragmentManager, imageUrl)
    }

    override fun showPhotoSearchResultFragment(keyword: String) {
        FragmentNavigator.showPhotoSearchResultFragment(supportFragmentManager, keyword)
    }
}
