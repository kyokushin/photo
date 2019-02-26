package com.yutasuz.photo.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yutasuz.photo.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){

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
