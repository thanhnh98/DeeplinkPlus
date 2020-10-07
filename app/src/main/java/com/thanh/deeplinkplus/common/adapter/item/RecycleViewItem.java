package com.thanh.deeplinkplus.common.adapter.item;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

public abstract class RecycleViewItem<VH extends RecyclerView.ViewHolder> {
    public int getType() {
        return this.getClass().hashCode();
    }

    public abstract void bind(VH viewHolder);

    public abstract RecyclerView.ViewHolder createViewHolder(Context context);
}
