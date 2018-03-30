package com.juslt.common.rv;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * Created by phelps on 2015/7/9.
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder implements HolderInterface {

    protected BaseRvFooterAdapter mAdapter;

    public BaseViewHolder(View itemView, BaseRvFooterAdapter adapter) {
        super(itemView);
        mAdapter = adapter;
    }

    public static View inflateItemView(int resId, ViewGroup parent){
        return LayoutInflater.from(parent.getContext()).inflate(resId,parent,false);
    }

}
