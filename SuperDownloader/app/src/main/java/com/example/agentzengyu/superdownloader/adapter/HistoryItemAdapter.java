package com.example.agentzengyu.superdownloader.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.agentzengyu.superdownloader.R;
import com.example.agentzengyu.superdownloader.entity.HistoryItem;

import java.util.ArrayList;

/**
 * Created by ZengYu on 2017/6/6.
 */

/**
 * 历史任务适配器
 */
public class HistoryItemAdapter extends RecyclerView.Adapter<HistoryItemAdapter.ItemViewHolder> implements View.OnClickListener {
    private ArrayList<HistoryItem> itemArrayList = null;
    private OnRecyclerViewItemClickListener listener = null;

    public HistoryItemAdapter(ArrayList<HistoryItem> arrayList) {
        this.itemArrayList = arrayList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_current, parent, false);
        ItemViewHolder holder = new ItemViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        String temp, text = "";
        if ((temp = this.itemArrayList.get(position).getName()) != null) {
            text += temp;
        }
        holder.getMtvName().setText(text);
        holder.getMtvSize().setText("" + this.itemArrayList.get(position).getSize());
        holder.getMtvID().setText("" + this.itemArrayList.get(position).getID());
        holder.itemView.setTag(this.itemArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onItemClick(v, (HistoryItem) v.getTag());
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
        void onItemClick(View view, HistoryItem historyItem);
    }

    /**
     * 自定义任务容器
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mtvName, mtvSize, mtvID;

        public ItemViewHolder(View view) {
            super(view);
            mtvName = (TextView) view.findViewById(R.id.tvName);
            mtvSize = (TextView) view.findViewById(R.id.tvSize);
            mtvID = (TextView)view.findViewById(R.id.tvID);
        }

        public TextView getMtvName() {
            return mtvName;
        }

        public TextView getMtvSize() {
            return mtvSize;
        }

        public TextView getMtvID() {
            return mtvID;
        }
    }
}

