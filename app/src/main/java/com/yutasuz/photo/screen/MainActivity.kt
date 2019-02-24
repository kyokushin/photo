package com.yutasuz.photo.screen

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.yutasuz.photo.R
import com.yutasuz.photo.api.response.FlickrPhotoResponse
import com.yutasuz.photo.screen.photolist.PhotoListFragment

import kotlinx.android.synthetic.main.activity_main.*
import org.koin.dsl.module.Module

class MainActivity : AppCompatActivity(){

    private val module: Module = org.koin.dsl.module.applicationContext {
        factory {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addOnBackPressedCallback {
            if(supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStackImmediate()
                return@addOnBackPressedCallback true
            }
            false
        }

        initView()
        initFragment()
    }


    private fun initView() {
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    }

    private fun initFragment() {
        FragmentNavigator.showPhotoListFragment(supportFragmentManager)
    }


}
