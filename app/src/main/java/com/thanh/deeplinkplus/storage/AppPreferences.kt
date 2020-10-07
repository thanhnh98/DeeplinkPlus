package com.thanh.deeplinkplus.storage

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thanh.deeplinkplus.model.UrlModel
import java.lang.Exception
import kotlin.collections.ArrayList
import kotlin.collections.List

class AppPreferences(context: Context?) {

    private var mPreferences: SharedPreferences
    private var PREFERENCES_NAME:String = "DeepLink_Preferences"
    private var LIST_URL:String = "LIST_URL"

    init {
        mPreferences = context?.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)!!
    }

    companion object{
        private lateinit var mInstance: AppPreferences

        @Synchronized
        fun getInstance(): AppPreferences {
            if (mInstance == null)
                throw NullPointerException("LoshipPreferences is null!")
            return mInstance
        }

        @Synchronized
        fun init(context: Context?) {
            mInstance = AppPreferences(context)
        }
    }

    fun saveUrl(url: UrlModel){

        val lst: MutableList<UrlModel>? = if (getListUrls() != null) getListUrls()?.toMutableList() else ArrayList()

        lst?.add(0, url)

        mPreferences.edit().putString(LIST_URL, Gson().toJson(lst?:return)).commit()
    }


    fun getListUrls(): List<UrlModel>?{
        return try {
            var json:String = mPreferences.getString(LIST_URL, "")?:""
            val type = object : TypeToken<List<UrlModel>>() {}.type

            val lst: List<UrlModel> = Gson().fromJson(json, type)

            Log.e("data", lst.size.toString())

            lst
        }catch (e: Exception){
            e.printStackTrace()
            null

        }
    }
    
    fun removeLocalById(id: Int): Int{
        var listUrl = getListUrls()?.toMutableList()
        for (indx in listUrl?.indices?:return -1){
            if (listUrl[indx].id == id){
                listUrl.removeAt(indx)
                mPreferences.edit().putString(LIST_URL, Gson().toJson(listUrl)).commit()
                return indx
            }
        }

        return -1
    }

    fun removeLocalUrls(){
        mPreferences.edit().remove(LIST_URL).commit()
    }

}