package com.thanh.deeplinkplus.screen.home.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thanh.deeplinkplus.R
import com.thanh.deeplinkplus.common.adapter.item.RecycleViewItem
import com.thanh.deeplinkplus.extension.onClick
import com.thanh.deeplinkplus.helper.DateTimeHelper
import com.thanh.deeplinkplus.model.UrlModel
import kotlinx.android.synthetic.main.item_url.view.*

open class UrlRecyclerViewItem(val dataItem: UrlModel, val mListener: IUrlRecycleViewListener): RecycleViewItem<UrlRecyclerViewHolder>() {

    override fun bind(holder: UrlRecyclerViewHolder?) {
        holder?.tvUrl?.text = dataItem.url
        holder?.itemView?.onClick {
            mListener.onItemClick(dataItem)
        }

        holder?.imgRemove?.onClick {
            mListener.onImgRemoveClick(dataItem)
        }

        holder?.tvDate?.text = DateTimeHelper.getSimpleDate(dataItem.createdAt.toLong())
    }

    override fun createViewHolder(context: Context?): RecyclerView.ViewHolder {
        return UrlRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.item_url, null))
    }
}

open class UrlRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var tvUrl = itemView.tv_url
    var imgRemove = itemView.imgRemove
    var tvDate = itemView.tv_date
}

interface IUrlRecycleViewListener{
    fun onItemClick(url: UrlModel)
    fun onImgRemoveClick(url: UrlModel)
}