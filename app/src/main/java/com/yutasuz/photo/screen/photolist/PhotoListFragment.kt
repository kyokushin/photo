package com.yutasuz.photo.screen.photolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yutasuz.photo.R
import com.yutasuz.photo.screen.MainActivityView
import kotlinx.android.synthetic.main.fragment_photo_list.*

class PhotoListFragment : Fragment(), PhotoListContract.View {

    companion object {
        const val TAG = "top"

        fun create() = PhotoListFragment()
    }

    override var presenter: PhotoListContract.Presenter? = null

    private lateinit var photoListAdapter: PhotoListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activity = activity as MainActivityView?

        if (activity != null) {
            presenter = PhotoListPresenter(activity, this, PhotoListRepository())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        presenter?.onCreateView()
        return inflater.inflate(R.layout.fragment_photo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        presenter?.onViewCreated()

        initView()
    }

    private fun initView() {
        fragment_top_recycler_view.apply {
            adapter = photoListAdapter
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        }

        refresh.setOnRefreshListener {
            presenter?.onRefresh()
        }

    }

    override fun initAdapter(items: List<PhotoListAdapter.Item>) {
        val context = context ?: return
        photoListAdapter = PhotoListAdapter(context, items)
    }

    override fun onResume() {
        super.onResume()

        presenter?.onResume()
    }

    override fun onPause() {
        super.onPause()

        presenter?.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        presenter?.onDestroyView()
    }

    override fun notifyDataSetChanged() {
        photoListAdapter.notifyDataSetChanged()
    }

    override fun notifyItemRangeChanged(positionStart: Int, itemCount: Int) {
        photoListAdapter.notifyItemRangeChanged(positionStart, itemCount)
    }

    override fun hideRefresh() {
        if (refresh.isRefreshing) refresh.isRefreshing = false
    }
}
