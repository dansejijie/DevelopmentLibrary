package com.dansejijie.library;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tygzx on 17/2/27.
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private LayoutInflater mInflater;

    private List<Action>mDatas;

    private OnRecyclerItemClickListener onRecyclerItemClickListener;

    public MainAdapter(Context context,List<Action>mDatas,OnRecyclerItemClickListener onRecyclerItemClickListener){
        mInflater=LayoutInflater.from(context);
        this.mDatas=mDatas;
        this.onRecyclerItemClickListener=onRecyclerItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view=mInflater.inflate(R.layout.main_recycle_view_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv.setText(mDatas.get(position).title);
        holder.tv.setTag(position);
        holder.tv.setOnClickListener(mDatas.get(position).onClickListener);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tv;
        public ViewHolder(View view){
            super(view);
            tv= (TextView) view.findViewById(R.id.app_recycle_view_text);
        }
    }

    public interface OnRecyclerItemClickListener{
        void onItemClick(View view,int position);
    }
}
