package com.example.agentzengyu.superdownloader.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.agentzengyu.superdownloader.R;
import com.example.agentzengyu.superdownloader.entity.DownloadItem;

import java.util.ArrayList;

/**
 * Created by ZengYu on 2017/6/6.
 */

public class ItemAdpter  extends RecyclerView.Adapter<ItemAdpter.ItemViewHolder> implements View.OnClickListener{
    private ArrayList<DownloadItem> downloadItems = null;
    private OnRecyclerViewItemClickListener listener = null;

    public ItemAdpter(ArrayList<DownloadItem> arrayList){
        this.downloadItems = arrayList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail,parent,false);
        ItemViewHolder viewHolder = new ItemViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder itemViewHolder, int position) {
        itemViewHolder.getMtv1().setText(this.downloadItems.get(position).getMsg1());
        itemViewHolder.getMtv2().setText(this.downloadItems.get(position).getMsg2());
        itemViewHolder.getMtv3().setText(this.downloadItems.get(position).getMsg3());
        itemViewHolder.getMtv4().setText(this.downloadItems.get(position).getMsg4());
        itemViewHolder.getMtv5().setText(this.downloadItems.get(position).getName());
        itemViewHolder.itemView.setTag(this.downloadItems.get(position));
    }

    @Override
    public int getItemCount() {
        return downloadItems.size();
    }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onItemClick(v,(DownloadItem)v.getTag());
        }
    }

    public void setItemClickListener(OnRecyclerViewItemClickListener listener){
        this.listener = listener;
    }

    public void addItem(int position,DownloadItem downloadItem){
        downloadItems.add(position,downloadItem);
        notifyItemInserted(position);
    }

    public void removeItem(DownloadItem downloadItem){
        if (downloadItems.contains(downloadItem)){
            downloadItems.remove(downloadItem);
            notifyDataSetChanged();
        }
    }

    public static interface OnRecyclerViewItemClickListener{
        void onItemClick(View view, DownloadItem downloadItem);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder{
        private TextView mtv1,mtv2,mtv3,mtv4,mtv5;

        public ItemViewHolder(View view){
            super(view);
            mtv1 = (TextView)view.findViewById(R.id.tvName1);
            mtv2 = (TextView)view.findViewById(R.id.tvName2);
            mtv3 = (TextView)view.findViewById(R.id.tvName3);
            mtv4 = (TextView)view.findViewById(R.id.tvName4);
            mtv5 = (TextView)view.findViewById(R.id.tvName5);
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

    }
}
