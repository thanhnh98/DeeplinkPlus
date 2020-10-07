package com.thanh.deeplinkplus.screen.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.thanh.deeplinkplus.R
import com.thanh.deeplinkplus.common.adapter.RecyclerManager
import com.thanh.deeplinkplus.common.adapter.item.RecycleViewItem
import com.thanh.deeplinkplus.storage.AppPreferences
import com.thanh.deeplinkplus.common.base.BaseActivity
import com.thanh.deeplinkplus.extension.onClick
import com.thanh.deeplinkplus.extension.showMessage
import com.thanh.deeplinkplus.extension.text
import com.thanh.deeplinkplus.model.PinModel
import com.thanh.deeplinkplus.model.UrlModel
import com.thanh.deeplinkplus.model.UserModel
import com.thanh.deeplinkplus.network.AppClient
import com.thanh.deeplinkplus.screen.home.item.IUrlRecycleViewListener
import com.thanh.deeplinkplus.screen.home.item.UrlRecyclerViewHolder
import com.thanh.deeplinkplus.screen.home.item.UrlRecyclerViewItem
import com.thanh.deeplinkplus.screen.home.presenter.HomePresenter
import com.thanh.deeplinkplus.screen.home.presenter.IHomePresenter
import com.thanh.deeplinkplus.service.UpdateService
import com.thanh.deeplinkplus.storage.local_db.database.AppDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.http.Url
import kotlin.reflect.KClass

class HomeActivity: BaseActivity<IHomePresenter>(), IHomeView, IUrlRecycleViewListener{

    private lateinit var listDataUrl: MutableList<UrlModel>
    private lateinit var mRecyclerManager: RecyclerManager<KClass<*>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mRecyclerManager = RecyclerManager()

        addCluster()

        recyclerview.adapter = mRecyclerManager.adapter
        recyclerview.layoutManager = GridLayoutManager(this, 1)

        btn_go?.onClick {
            mPresenter.requestHandleIntent(edt_deeplink?.text()?:"")
        }

        btn_clear?.onClick {
            mPresenter.requestCleanUrls()
        }
        mPresenter.requestShowListUrl()
    }

    private fun addCluster() {
        mRecyclerManager.addCluster(UrlRecyclerViewItem::class)
    }


    override fun getPresenter(): IHomePresenter {
        return HomePresenter(this)
    }

    override fun onError(msg: String) {
        showMessage(msg)
    }

    override fun handleIntent(intent: Intent) {
       startActivity(intent)
    }

    override fun showListUrl(listData: List<UrlModel>) {
        mRecyclerManager.replace(UrlRecyclerViewItem::class, buildListItem(listData))
        if (listData.size > 0)
            btn_clear?.visibility = View.VISIBLE
        else
            btn_clear?.visibility = View.GONE

    }

    private fun buildListItem(listData: List<UrlModel>): List<UrlRecyclerViewItem> {

        var listResult: MutableList<UrlRecyclerViewItem> = ArrayList()
        for (index in listData)
            listResult.add(UrlRecyclerViewItem(index, this))

        return listResult
    }

    override fun onItemClick(url: UrlModel) {
        edt_deeplink.setText(url.url)
    }

    override fun onImgRemoveClick(url: UrlModel) {
        mPresenter.requestRemoveUrl(url)
    }

}