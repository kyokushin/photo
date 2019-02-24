package com.yutasuz.photo.screen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.yutasuz.photo.R
import com.yutasuz.photo.api.response.FlickrPhotoResponse
import com.yutasuz.photo.screen.photolist.PhotoListFragment
import com.yutasuz.photo.screen.viewer.ViewerFragment
import kotlinx.android.synthetic.main.activity_main.view.*

object FragmentNavigator {

    fun showPhotoViewerFragment(fragmentManager: FragmentManager, photoResponse: FlickrPhotoResponse) {
        fragmentManager.beginTransaction().apply {
            val prevFragment = fragmentManager.findFragmentByTag(ViewerFragment.TAG)
            if (prevFragment != null) {
                remove(prevFragment)
            }
            replace(R.id.fragment_container, ViewerFragment.create(photoResponse), ViewerFragment.TAG)
            addToBackStack(ViewerFragment.TAG)
            commitAllowingStateLoss()
        }
    }

    fun showPhotoListFragment(fragmentManager: FragmentManager) {
        fragmentManager.findFragmentByTag(PhotoListFragment.TAG) ?: run {
            fragmentManager.beginTransaction()
                .add(R.id.fragment_container, PhotoListFragment.create(), PhotoListFragment.TAG)
                .commitAllowingStateLoss()
        }
    }
}