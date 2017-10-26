package com.fsecure.homework.searchapp.ui;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.fsecure.homework.searchapp.data.AppDatabase;
import com.fsecure.homework.searchapp.data.SearchQueryData;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivityViewModel extends ViewModel {

    private static final int ITEM_COUNT_LIMIT = 3;

    private final AppDatabase database;

    private final ExecutorService executorService =
            Executors.newFixedThreadPool(1);

    private final LinkedList<SearchQueryData> queryDataList = new LinkedList<>();

    public MainActivityViewModel(AppDatabase database) {
        this.database = database;
    }

    public void onCreate(LifecycleOwner owner) {
        database.searchQueryDao().getSearchQueryDataList().observe(owner, new Observer<List<SearchQueryData>>() {
            @Override
            public void onChanged(@Nullable List<SearchQueryData> list) {
                queryDataList.clear();
                if (list != null) queryDataList.addAll(list);
            }
        });
    }

    public LiveData<List<SearchQueryData>> getSearchQueryDataList() {
        return database.searchQueryDao().getSearchQueryDataList();
    }

    public void setLastSearchQueryData(SearchQueryData data) {
        if (!queryDataList.isEmpty() && queryDataList.getFirst().equals(data))
            return;

        executorService.submit(
                (queryDataList.size() > ITEM_COUNT_LIMIT - 1)
                        ? new InsertDeleteTask(data, queryDataList.getLast())
                        : new InsertDeleteTask(data, null));
    }


    private class InsertDeleteTask implements Runnable {

        private final SearchQueryData insertData;
        private final SearchQueryData deleteData;

        private InsertDeleteTask(@NonNull SearchQueryData insertData, @Nullable SearchQueryData deleteData) {
            this.insertData = insertData;
            this.deleteData = deleteData;
        }

        @Override
        public void run() {
            database.searchQueryDao().insertSearchQueryData(insertData);

            if (deleteData != null)
                database.searchQueryDao().deleteSearchQueryData(deleteData);
        }
    }

}
