package com.fsecure.homework.searchapp.adapter;

import android.databinding.ViewDataBinding;

import com.fsecure.homework.searchapp.R;
import com.fsecure.homework.searchapp.data.ApplicationData;

import java.util.List;

public class SearchResultListAdapter extends GenericRecyclerViewAdapter<ApplicationData> {

    private final List<ApplicationData> appDataList;

    public SearchResultListAdapter(List<ApplicationData> appDataList) {
        this.appDataList = appDataList;
    }

    @Override
    protected GenericViewHolder<ApplicationData> createViewHolder(ViewDataBinding binding, int viewType) {
        return new SearchResultViewHolder(binding);
    }

    @Override
    protected ApplicationData getItem(int position) {
        return appDataList.get(position);
    }

    @Override
    protected int getViewLayoutId(int viewType) {
        return R.layout.search_result_item;
    }

    @Override
    public int getItemCount() {
        return appDataList.size();
    }

}
