package com.thanh.deeplinkplus.screen.home.item.empty_item

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import com.thanh.deeplinkplus.R
import com.thanh.deeplinkplus.common.adapter.item.RecycleViewItem

class EmptyListRecycleViewItem: RecycleViewItem<EmptyListRecycleVH>() {
    override fun bind(viewHolder: EmptyListRecycleVH?) {

    }

    override fun createViewHolder(context: Context?): RecyclerView.ViewHolder {
        return EmptyListRecycleVH(LayoutInflater.from(context).inflate(R.layout.item_empty, null))
    }

}