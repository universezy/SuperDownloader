package com.example.agentzengyu.superdownloader.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.agentzengyu.superdownloader.R;
import com.example.agentzengyu.superdownloader.entity.CurrentDownloadItem;
import com.example.agentzengyu.superdownloader.view.CircleProgressView;

import java.util.ArrayList;

/**
 * Created by ZengYu on 2017/6/6.
 */

public class CurrentItemAdpter extends RecyclerView.Adapter<CurrentItemAdpter.ItemViewHolder> implements View.OnClickListener {
    private ArrayList<CurrentDownloadItem> currentDownloadItems = null;
    private OnRecyclerViewItemClickListener listener = null;

    public CurrentItemAdpter(ArrayList<CurrentDownloadItem> arrayList) {
        this.currentDownloadItems = arrayList;
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
        itemViewHolder.getMtv1().setText(this.currentDownloadItems.get(position).getMsg1());
        itemViewHolder.getMtv2().setText(this.currentDownloadItems.get(position).getMsg2());
        itemViewHolder.getMtv3().setText(this.currentDownloadItems.get(position).getMsg3());
        itemViewHolder.getMtv4().setText(this.currentDownloadItems.get(position).getMsg4());
        itemViewHolder.getMtv5().setText(this.currentDownloadItems.get(position).getName());
        itemViewHolder.getCircleProgressView().setProgressNotInUiThread(this.currentDownloadItems.get(position).getProgress());
        itemViewHolder.itemView.setTag(this.currentDownloadItems.get(position));
    }

    @Override
    public int getItemCount() {
        return currentDownloadItems.size();
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onItemClick(v, (CurrentDownloadItem) v.getTag());
        }
    }

    public void setItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public void addItem(int position, CurrentDownloadItem currentDownloadItem) {
        currentDownloadItems.add(position, currentDownloadItem);
        notifyItemInserted(position);
    }

    public void removeItem(CurrentDownloadItem currentDownloadItem) {
        if (currentDownloadItems.contains(currentDownloadItem)) {
            currentDownloadItems.remove(currentDownloadItem);
            notifyDataSetChanged();
        }
    }

    public void updateProgress(CurrentDownloadItem currentDownloadItem, int progress) {
        if (currentDownloadItems.contains(currentDownloadItem)) {
            currentDownloadItems.get(currentDownloadItems.indexOf(currentDownloadItem)).setProgress(progress);
            notifyDataSetChanged();
        }
    }

    public void setDownloadStatus(CurrentDownloadItem currentDownloadItem, boolean downloading){
        if (currentDownloadItems.contains(currentDownloadItem)) {
            currentDownloadItems.get(currentDownloadItems.indexOf(currentDownloadItem)).setDownloading(downloading);
            notifyDataSetChanged();
        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, CurrentDownloadItem currentDownloadItem);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView mtv1, mtv2, mtv3, mtv4, mtv5;
        private CircleProgressView circleProgressView;

        public ItemViewHolder(View view) {
            super(view);
            mtv1 = (TextView) view.findViewById(R.id.tvName1);
            mtv2 = (TextView) view.findViewById(R.id.tvName2);
            mtv3 = (TextView) view.findViewById(R.id.tvName3);
            mtv4 = (TextView) view.findViewById(R.id.tvName4);
            mtv5 = (TextView) view.findViewById(R.id.tvName5);
            circleProgressView = (CircleProgressView) view.findViewById(R.id.cp);
        }

        public TextView getMtv1() {
            return mtv1;
        }

        public TextView getMtv2() {
            return mtv2;
        }

        public TextView getMtv3() {
            return mtv3;
        }

        public TextView getMtv4() {
            return mtv4;
        }

        public TextView getMtv5() {
            return mtv5;
        }

        public CircleProgressView getCircleProgressView() {
            return circleProgressView;
        }
    }
}
