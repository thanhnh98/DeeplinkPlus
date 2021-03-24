package com.thanh.deeplinkplus.screen.home.viewmodel

import com.thanh.deeplinkplus.R
import com.thanh.deeplinkplus.common.SingleLiveEvent
import com.thanh.deeplinkplus.common.base.BaseViewModel
import com.thanh.deeplinkplus.common.resources.Resources
import com.thanh.deeplinkplus.extension.isSafe
import com.thanh.deeplinkplus.model.*
import com.thanh.deeplinkplus.model.converter.TypeUrl
import com.thanh.deeplinkplus.usecase.UrlUseCase
import kotlinx.coroutines.launch

class HomeViewModel(private val urlUseCase: UrlUseCase): BaseViewModel() {

    private var currentUrlSelected = SingleLiveEvent<UrlModel>()
    private var errorMessage = SingleLiveEvent<String>()
    private var intentHandler = SingleLiveEvent<UrlModel>()
    private var listEmptyNotifier = SingleLiveEvent<Boolean>()

    private val regexUniversal: Regex = Regex("^((https?|ftp|smtp):\\/\\/)?(www.)?[a-z0-9]+\\.[a-z]+(\\/[a-zA-Z0-9\\p{Punct}]+\\/?)*\$")
    private val regexDeeplink: Regex = Regex("^(\\w*:\\/\\/).+\$")

    private var currentListUrl: MutableList<UrlModel> = ArrayList()

    override fun init() {
        super.init()
        loadListUrls()
        checkUpdate()
    }

    private fun checkUpdate() {
        uiScope.launch {
            urlUseCase.requestDataUpdate().apply {
                shouldShowDialogUpdate(this)
            }
        }
    }

    fun onCurrentUrlSelectedChanged(): SingleLiveEvent<UrlModel>{
        return currentUrlSelected
    }

    fun onListEmptyNotifier(): SingleLiveEvent<Boolean>{
        return listEmptyNotifier
    }

    fun onListUrlItemChanged(): SingleLiveEvent<ActionModel>{
        return urlUseCase.onListUrlItemChanged()
    }

    fun getErrorMessageNotifier(): SingleLiveEvent<String>{
        return errorMessage
    }

    fun getIntentHandler(): SingleLiveEvent<UrlModel>{
        return intentHandler
    }

    fun updateCurrentUrlSelected(urlModel: UrlModel){
        currentUrlSelected.value = urlModel
    }

    fun onListUrlUpdated(): SingleLiveEvent<List<UrlModel>>{
        return urlUseCase.onListUrlUpdated()
    }

    fun loadListUrls(){
        uiScope.launch {
            urlUseCase.getListUrlFromLocal().apply {
                currentListUrl.clear()
                currentListUrl.addAll(this)
                onListUrlUpdated().postValue(currentListUrl)
                checkCurrentListState()
            }
        }
    }

    fun removeUrlById(urlModel: UrlModel){
        uiScope.launch {
            urlUseCase
                .removeUrlById(urlModel).apply {
                    onListUrlItemChanged().postValue(ActionModel(ActionDataChanged.DELETE, urlModel, getPosUrlInList(urlModel)))
                    currentListUrl.remove(urlModel)
                    checkCurrentListState()
                }
        }
    }

    fun saveUrl(urlModel: UrlModel){
        uiScope.launch {
            urlUseCase.saveUrl(urlModel).apply {
                onListUrlItemChanged().postValue(ActionModel(ActionDataChanged.INSERT, urlModel, 0))
                currentListUrl.add(0, urlModel)
                checkCurrentListState()
            }
        }
    }

    fun handleUrl(link: String){
        if (!link.isSafe()){
            errorMessage.postValue(Resources.getString(R.string.err_msg_something_wrong))
            return
        }
        val urlModel = generateUrlModel(link)
        saveUrl(urlModel)
        intentHandler.postValue(urlModel)
        currentUrlSelected.value = urlModel
    }

    fun removeAll(){
        uiScope.launch {
            urlUseCase.clearAllUrl().apply {
                loadListUrls()
            }
        }
    }

    private fun generateUrlModel(url: String): UrlModel{
        val url = url
        val createdAt: Long = System.currentTimeMillis()
        val createdBy = UserModel("Thanh")
        val pin = PinModel(System.currentTimeMillis().toString())
        val isEnable = true
        val typeUrl: Int = when{
            url.matches(regexUniversal) -> TypeUrl.UNIVERSAL_LINK
            url.matches(regexDeeplink) -> TypeUrl.DEEP_LINK
            else -> TypeUrl.OTHER
        }
        return UrlModel(url, createdAt.toString(), createdBy, typeUrl, isEnable, pin)
    }

    private fun getPosUrlInList(urlModel: UrlModel): Int{
        return currentListUrl.indexOf(urlModel)
    }

    private fun checkCurrentListState(){
        listEmptyNotifier.postValue(currentListUrl.isNullOrEmpty())
    }

    fun shouldShowDialogUpdate(update: UpdateModel){
        //TODO: check choi cho vui
//        if (update == null)
//            return
//
//        if (update.isForceUpdate ||
//            (AppPreferences.getInstance().getLastVersionShowPopup().isNotEmpty() && update.version != AppPreferences.getInstance().getLastVersionShowPopup()) ||
//            (Resources.getString(R.string.version_name).replace("%s", update.version) != requestVersionName() && AppPreferences.getInstance().shouldShowUpdateDialog()))

    }

    fun requestVersionName(): String {
        try {
            val versionName = Resources.getContext().packageManager
                .getPackageInfo(Resources.getContext().packageName, 0).versionName
            return Resources.getString(R.string.version_name).replace("%s", versionName)
        }catch (e: Exception){
            e.printStackTrace()
        }

        return ""
    }
}