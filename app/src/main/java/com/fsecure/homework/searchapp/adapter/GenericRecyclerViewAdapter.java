package com.fsecure.homework.searchapp.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

abstract class GenericRecyclerViewAdapter<T> extends RecyclerView.Adapter<GenericViewHolder<T>> {

    private LayoutInflater layoutInflater;

    @Override
    public GenericViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }

        ViewDataBinding binding = DataBindingUtil.inflate(layoutInflater, getViewLayoutId(viewType),
                parent, false);
        return createViewHolder(binding, viewType);
    }

    @Override
    public void onBindViewHolder(final GenericViewHolder<T> holder, final int position) {
        holder.bind(getItem(position));
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(holder.getView(), getItem(position), position);
            }
        });
    }

    @Override
    public void onViewDetachedFromWindow(GenericViewHolder<T> holder) {
        holder.getView().setOnClickListener(null);
    }

    protected void onItemClick(View view, T data, int position) {

    }

    protected abstract GenericViewHolder<T> createViewHolder(ViewDataBinding binding, int viewType);

    protected abstract T getItem(int position);

    protected abstract int getViewLayoutId(int viewType);

}
