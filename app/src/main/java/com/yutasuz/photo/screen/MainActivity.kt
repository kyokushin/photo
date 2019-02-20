package com.yutasuz.photo

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import com.yutasuz.photo.screen.top.TopFragment

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initFragment()
    }

    private fun initView(){
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    }

    private fun initFragment(){
        val fragment = supportFragmentManager.findFragmentByTag(TopFragment.TAG)
        if(fragment != null) return

        supportFragmentManager.beginTransaction()
            .add(TopFragment.create(), TopFragment.TAG)
            .commitNow()
    }
}
