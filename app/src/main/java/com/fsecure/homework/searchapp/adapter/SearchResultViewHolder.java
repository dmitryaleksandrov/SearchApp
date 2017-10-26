package com.fsecure.homework.searchapp.adapter;

import android.databinding.ViewDataBinding;

import com.fsecure.homework.searchapp.BR;
import com.fsecure.homework.searchapp.data.ApplicationData;

public class SearchResultViewHolder extends GenericViewHolder<ApplicationData> {

    public SearchResultViewHolder(ViewDataBinding binding) {
        super(binding);
    }

    @Override
    public void bind(ApplicationData data) {
        binding.setVariable(BR.appData, data);
        binding.getRoot().setTag(data);
    }
}
