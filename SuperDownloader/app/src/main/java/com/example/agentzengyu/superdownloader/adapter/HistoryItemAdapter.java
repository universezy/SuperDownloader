package com.example.agentzengyu.superdownloader.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.agentzengyu.superdownloader.R;
import com.example.agentzengyu.superdownloader.entity.HistoryDownloadItem;

import java.util.ArrayList;

/**
 * Created by ZengYu on 2017/6/6.
 */

/**
 * 历史任务适配器
 */
public class HistoryItemAdapter extends RecyclerView.Adapter<HistoryItemAdapter.ItemViewHolder> implements View.OnClickListener {
    private ArrayList<HistoryDownloadItem> historyDownloadItems = null;
    private OnRecyclerViewItemClickListener listener = null;

    public HistoryItemAdapter(ArrayList<HistoryDownloadItem> arrayList) {
        this.historyDownloadItems = arrayList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_current, parent, false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder itemViewHolder, int position) {
        itemViewHolder.getName().setText(this.historyDownloadItems.get(position).getName());
        itemViewHolder.getSize().setText("" + this.historyDownloadItems.get(position).getSize());
        itemViewHolder.itemView.setTag(this.historyDownloadItems.get(position));
    }

    @Override
    public int getItemCount() {
        return historyDownloadItems.size();
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onItemClick(v, (HistoryDownloadItem) v.getTag());
        }
    }

    /**
     * 设置点击监听
     *
     * @param listener
     */
    public void setItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * RecyclerView点击监听的接口
     */
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, HistoryDownloadItem historyDownloadItem);
    }

    /**
     * 自定义任务容器
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mtvName, mtvSize;

        public ItemViewHolder(View view) {
            super(view);
            mtvName = (TextView) view.findViewById(R.id.tvName);
            mtvSize = (TextView) view.findViewById(R.id.tvSize);
        }

        public TextView getName() {
            return mtvName;
        }

        public TextView getSize() {
            return mtvSize;
        }
    }
}

