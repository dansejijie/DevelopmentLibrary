package com.dansejijie.library.widget.activity.pdfReader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dansejijie.library.widget.R;

import java.util.List;

/**
 * Created by tygzx on 17/4/27.
 */

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> {

    private LayoutInflater mInflater;

    private List<Chapter>mDatas;

    private OnRecyclerItemClickListener onRecyclerItemClickListener;


    public ChapterAdapter(Context context, List<Chapter>mDatas, OnRecyclerItemClickListener onRecyclerItemClickListener){
        mInflater=LayoutInflater.from(context);
        this.mDatas=mDatas;
        this.onRecyclerItemClickListener=onRecyclerItemClickListener;
    }

    @Override
    public ChapterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view=mInflater.inflate(R.layout.item_pdf_reader,parent,false);
        ChapterAdapter.ViewHolder viewHolder=new ChapterAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tv.setText(mDatas.get(position).title);
        holder.tv.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv;

        public ViewHolder(View itemView) {
            super(itemView);
            tv= (TextView) itemView.findViewById(R.id.item_pdf_reader_tv);
        }
    }

    public interface OnRecyclerItemClickListener{
        void onItemClick(View view, int position);
    }
}
