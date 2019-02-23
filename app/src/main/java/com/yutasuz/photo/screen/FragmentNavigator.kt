package com.yutasuz.photo.screen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.yutasuz.photo.R
import kotlinx.android.synthetic.main.activity_main.view.*

object FragmentNavigator {

    fun showPhotoViewerFragment(fragmentManager: FragmentManager, imageUrl: String){
    }

    fun showPhotoSearchResultFragment(fragmentManager: FragmentManager, keyword: String){
    }

    private fun replaceFragment(fragmentManager: FragmentManager, fragment: Fragment, tag: String){
        fragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
            .addToBackStack(null)
            .commitNow()
    }
}