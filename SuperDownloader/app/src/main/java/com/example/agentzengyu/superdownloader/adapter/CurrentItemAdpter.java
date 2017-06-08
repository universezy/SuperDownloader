package com.example.agentzengyu.superdownloader.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.agentzengyu.superdownloader.R;
import com.example.agentzengyu.superdownloader.entity.CurrentItem;
import com.example.agentzengyu.superdownloader.view.CircleProgressView;

import java.util.ArrayList;

/**
 * Created by ZengYu on 2017/6/6.
 */

/**
 * 当前任务适配器
 */
public class CurrentItemAdpter extends RecyclerView.Adapter<CurrentItemAdpter.ItemViewHolder> implements View.OnClickListener {
    private ArrayList<CurrentItem> itemArrayList = null;
    private OnRecyclerViewItemClickListener listener = null;

    public CurrentItemAdpter(ArrayList<CurrentItem> arrayList) {
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
        holder.getCircleProgressView().setProgressNotInUiThread(this.itemArrayList.get(position).getProgress());
        holder.itemView.setTag(this.itemArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onItemClick(v, (CurrentItem) v.getTag());
        }
    }

    /**
     * 设置任务点击监听
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
        void onItemClick(View view, CurrentItem currentItem);
    }

    /**
     * 自定义任务容器
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mtvName, mtvSize, mtvID;
        private CircleProgressView circleProgressView;

        public ItemViewHolder(View view) {
            super(view);
            mtvName = (TextView) view.findViewById(R.id.tvName);
            mtvSize = (TextView) view.findViewById(R.id.tvSize);
            mtvID = (TextView)view.findViewById(R.id.tvID);
            circleProgressView = (CircleProgressView) view.findViewById(R.id.cp);
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

        public CircleProgressView getCircleProgressView() {
            return circleProgressView;
        }
    }
}
