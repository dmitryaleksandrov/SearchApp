package com.fsecure.homework.searchapp.adapter;

import android.databinding.ViewDataBinding;

import com.fsecure.homework.searchapp.R;
import com.fsecure.homework.searchapp.data.SearchQueryData;

import java.util.ArrayList;
import java.util.List;


public class SearchDataListAdapter extends GenericRecyclerViewAdapter<SearchQueryData> {

    private List<SearchQueryData> searchQueryDataList = new ArrayList<>();

    @Override
    public int getItemCount() {
        return searchQueryDataList.size();
    }

    @Override
    protected GenericViewHolder<SearchQueryData> createViewHolder(ViewDataBinding binding, int viewType) {
        return new SearchDataViewHolder(binding);
    }

    @Override
    protected SearchQueryData getItem(int position) {
        return searchQueryDataList.get(position);
    }

    @Override
    protected int getViewLayoutId(int viewType) {
        return R.layout.search_data_item;
    }

    public void setData(List<SearchQueryData> searchQueryDataList) {
        this.searchQueryDataList = new ArrayList<>(searchQueryDataList);
        notifyDataSetChanged();
    }

}
