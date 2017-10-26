package com.fsecure.homework.searchapp.adapter;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class GenericViewHolder<T> extends RecyclerView.ViewHolder {

    protected final ViewDataBinding binding;

    public GenericViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public View getView() {
        return binding.getRoot();
    }

    public abstract void bind(T data);
}
