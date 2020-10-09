package com.thanh.deeplinkplus.screen.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.thanh.deeplinkplus.R
import com.thanh.deeplinkplus.common.adapter.RecyclerManager
import com.thanh.deeplinkplus.common.base.BaseActivity
import com.thanh.deeplinkplus.common.resources.Resources
import com.thanh.deeplinkplus.dialog.FactoryDialog
import com.thanh.deeplinkplus.extension.onClick
import com.thanh.deeplinkplus.extension.showMessage
import com.thanh.deeplinkplus.extension.text
import com.thanh.deeplinkplus.model.UpdateModel
import com.thanh.deeplinkplus.model.UrlModel
import com.thanh.deeplinkplus.screen.home.item.IUrlRecycleViewListener
import com.thanh.deeplinkplus.screen.home.item.UrlRecyclerViewItem
import com.thanh.deeplinkplus.screen.home.item.empty_item.EmptyListRecycleViewItem
import com.thanh.deeplinkplus.storage.AppPreferences
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.reflect.KClass

class HomeActivity: BaseActivity<HomeContact.Presenter>(), HomeContact.View, IUrlRecycleViewListener{

    private lateinit var listDataUrl: MutableList<UrlModel>
    private lateinit var mRecyclerManager: RecyclerManager<KClass<*>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //testThreadling()

        mRecyclerManager = RecyclerManager()

        addCluster()

        recyclerview.adapter = mRecyclerManager.adapter
        recyclerview.layoutManager = GridLayoutManager(this, 1)

        //go
        btn_go?.onClick {
            mPresenter.requestHandleIntent(edt_deeplink?.text() ?: "")
        }

        //clear
        btn_clear?.onClick {
            mPresenter.requestCleanUrls()
        }

        //copy
        btnCopy?.onClick {
            mPresenter.copyText(edt_deeplink?.text())
        }

        //version
        tvVersion?.text = mPresenter.requestVersionName()

//        //hint
//        tvHint?.apply {
//            onClick {
//                edt_deeplink?.setText(text)
//                visibility = View.GONE
//            }
//        }

        mPresenter.requestShowListUrl()

        mPresenter.getClipboard(this).apply {
            if (!isNullOrEmpty())
                tvHint?.text = this
        }

        mPresenter.requestCheckingUpdate()

    }

    private fun testThreadling() {
        showMessage("THANH")

        runBlocking {
            Log.e("thread current 2", Thread.currentThread().name)
            delay(1000)
            showMessage("THANH")

        }
        GlobalScope.launch {
            Log.e("thread current 1", Thread.currentThread().name)
        }

    }

    private fun addCluster() {
        mRecyclerManager.addCluster(UrlRecyclerViewItem::class)
    }


    override fun getPresenter(): HomeContact.Presenter {
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
        if (listData.isNotEmpty())
            btn_clear?.visibility = View.VISIBLE
        else
            btn_clear?.visibility = View.GONE
    }

    override fun removeUrlFromList(position: Int) {
        mRecyclerManager.remove(UrlRecyclerViewItem::class, position)
    }

    override fun insertUrlIntoList(url: UrlModel) {
        mRecyclerManager.append(UrlRecyclerViewItem::class, 0, UrlRecyclerViewItem(url, this))
    }

    override fun showEmpty() {
        mRecyclerManager.replace(UrlRecyclerViewItem::class, EmptyListRecycleViewItem())
        btn_clear?.visibility = View.GONE
    }

    override fun showWebView(url: String) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(if (!url.contains("http")) "https://$url" else url)
            )
        )
    }

    override fun copySuccess(msg: String) {
       showMessage(msg)
    }

    override fun showDialogCheckingUpdate(update: UpdateModel) {
        FactoryDialog.getInstance().apply {
                setContent(Resources.getString(R.string.dialog_update_des).replace("%old", mPresenter.requestVersionName()).replace("%new", update.version))
                setTitle(Resources.getString(R.string.dialog_update_title))
                setNegativeClick(Resources.getString(R.string.dialog_update_btn_close)){
                        it.dismiss()
                    }
                setPositiveClick(Resources.getString(R.string.dialog_update_btn_ok)){
                    startActivity(Intent().apply {
                        action = Intent.ACTION_VIEW
                        data = Uri.parse(update.url)
                    })
                    it.dismiss()
                    AppPreferences.getInstance().setValueShouldShowDialog(false)
                }
                setOnCheckBoxListener {
                    mPresenter.onStatusShowDialogUpdateChanged(it)
                }
            AppPreferences.getInstance().setLastVersionShowPopup(update.version)
        }.show(supportFragmentManager)
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

    override fun onResume() {
        super.onResume()
    }
}