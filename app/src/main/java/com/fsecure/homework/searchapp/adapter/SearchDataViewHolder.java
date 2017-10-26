package com.fsecure.homework.searchapp.adapter;

import android.databinding.ViewDataBinding;

import com.fsecure.homework.searchapp.BR;
import com.fsecure.homework.searchapp.data.SearchQueryData;

public class SearchDataViewHolder extends GenericViewHolder<SearchQueryData> {

    public SearchDataViewHolder(ViewDataBinding binding) {
        super(binding);
    }

    @Override
    public void bind(SearchQueryData data) {
        binding.setVariable(BR.data, data);
        binding.executePendingBindings();
    }
}
