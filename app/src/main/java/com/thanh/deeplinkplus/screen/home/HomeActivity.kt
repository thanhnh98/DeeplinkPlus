package com.thanh.deeplinkplus.screen.home

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.thanh.deeplinkplus.R
import com.thanh.deeplinkplus.common.adapter.RecyclerManager
import com.thanh.deeplinkplus.common.adapter.item.spacing.SpacingRecyclerItem
import com.thanh.deeplinkplus.common.base.BaseActivity
import com.thanh.deeplinkplus.common.resources.Resources
import com.thanh.deeplinkplus.dialog.FactoryDialog
import com.thanh.deeplinkplus.extension.onClick
import com.thanh.deeplinkplus.extension.showMessage
import com.thanh.deeplinkplus.extension.text
import com.thanh.deeplinkplus.extension.toSafeUrl
import com.thanh.deeplinkplus.model.ActionDataChanged
import com.thanh.deeplinkplus.model.UpdateModel
import com.thanh.deeplinkplus.model.UrlModel
import com.thanh.deeplinkplus.model.converter.TypeUrl
import com.thanh.deeplinkplus.screen.home.item.IUrlRecycleViewListener
import com.thanh.deeplinkplus.screen.home.item.UrlRecyclerViewItem
import com.thanh.deeplinkplus.screen.home.item.empty_item.EmptyListRecycleViewItem
import com.thanh.deeplinkplus.screen.home.viewmodel.HomeViewModel
import com.thanh.deeplinkplus.storage.AppPreferences
import kotlinx.android.synthetic.main.activity_home.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.Exception
import kotlin.reflect.KClass

class HomeActivity: BaseActivity(), IUrlRecycleViewListener{

    private lateinit var mRecyclerManager: RecyclerManager<KClass<*>>
    private val viewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initViewModel(viewModel)
        initCluster()
        initListener()
        initObservers()
        initUI()
    }

    private fun initUI() {
        tvVersion?.text = viewModel.requestVersionName()
    }

    private fun initObservers() {
        viewModel.onListUrlUpdated().observe(this){
                showListUrl(it?:return@observe)
        }

        viewModel.onCurrentUrlSelectedChanged().observe(this){
            if (it == null)
                return@observe

            edt_deeplink?.setText(it.url)
            tv_mode?.text = when (it.typeUrl) {
                TypeUrl.DEEP_LINK -> Resources.getString(R.string.mode_deep_link)
                TypeUrl.UNIVERSAL_LINK -> Resources.getString(R.string.mode_universal_link)
                else -> Resources.getString(R.string.mode_other)
            }
        }

        viewModel.getErrorMessageNotifier().observe(this){
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.getIntentHandler().observe(this){
            try {
                startActivity(Intent().apply {
                    action = Intent.ACTION_VIEW
                    data = when (it.typeUrl) {
                        TypeUrl.DEEP_LINK -> Uri.parse(it.url)
                        else -> Uri.parse(it.url.toSafeUrl())
                    }
                })
            }
            catch (exp: ActivityNotFoundException){
                viewModel.getErrorMessageNotifier().postValue(Resources.getString(R.string.err_msg_not_found))
            }
            catch (exp2: Exception){
                viewModel.getErrorMessageNotifier().postValue(Resources.getString(R.string.err_msg_something_wrong))
            }
        }

        viewModel.onListUrlItemChanged().observe(this){
            when(it.action){
                ActionDataChanged.INSERT -> {
                    insertUrlItem(it.url)
                }
                ActionDataChanged.DELETE -> {
                    removeUrlItem(it.pos)
                }
                ActionDataChanged.DELETE_ALL -> {}
            }
        }

        viewModel.onListEmptyNotifier().observe(this){
            val isCurrentListEmpty = it

            if (isCurrentListEmpty){
                showEmpty()
                btn_clear.visibility = View.GONE
            }
            else {
                hideEmpty()
                btn_clear.visibility = View.VISIBLE
            }
        }
    }

    private fun insertUrlItem(url: UrlModel){
        mRecyclerManager.append(UrlRecyclerViewItem::class, 0, UrlRecyclerViewItem(url, this))
        recyclerview.smoothScrollToPosition(0)
    }

    private fun removeUrlItem(pos: Int){
        mRecyclerManager.remove(UrlRecyclerViewItem::class, pos)
    }

    private fun initListener() {
        btn_go?.onClick {
            viewModel.handleUrl(edt_deeplink?.text()?:return@onClick)
        }

        //clear
        btn_clear?.onClick {
            viewModel.removeAll()
        }

        //copy
        btnCopy?.onClick {
            edt_deeplink?.text?.apply {
                if (this.toString().isEmpty()){
                    showMessage("Nothing...")
                }
                else{
                    Resources.copyToClipboard(this.toString())
                    showMessage(Resources.getString(R.string.copy_success))
                }
            }
        }
    }

    private fun initCluster() {
        mRecyclerManager = RecyclerManager()
        addCluster()
        recyclerview.adapter = mRecyclerManager.adapter
        recyclerview.layoutManager = GridLayoutManager(this, 1)
    }

    private fun addCluster() {
        mRecyclerManager.addCluster(UrlRecyclerViewItem::class)
        mRecyclerManager.addCluster(EmptyListRecycleViewItem::class)
    }


    fun showListUrl(listData: List<UrlModel>) {
        mRecyclerManager.replace(UrlRecyclerViewItem::class, buildListItem(listData))
    }

    fun showEmpty() {
        mRecyclerManager.replace(EmptyListRecycleViewItem::class, EmptyListRecycleViewItem())
    }

    fun hideEmpty(){
        mRecyclerManager.replace(EmptyListRecycleViewItem::class, SpacingRecyclerItem(0, 0))
    }

    private fun showDialogCheckingUpdate(update: UpdateModel) {
        FactoryDialog.getInstance().apply {
                setContent(Resources.getString(R.string.dialog_update_des).replace("%old", viewModel.requestVersionName()).replace("%new", update.version))
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
                    //TODO: Save current state to local
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
        viewModel.updateCurrentUrlSelected(url)
    }

    override fun onImgRemoveClick(url: UrlModel) {
        viewModel.removeUrlById(url)
    }


}