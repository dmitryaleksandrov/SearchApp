package com.fsecure.homework.searchapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.fsecure.homework.searchapp.R;
import com.fsecure.homework.searchapp.adapter.SearchDataListAdapter;
import com.fsecure.homework.searchapp.data.SearchQueryData;
import com.fsecure.homework.searchapp.service.SearchService;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String IS_SEARCH_IN_PROG_KEY = "search_in_prog";

    @Inject
    ViewModelProvider.Factory viewModelProviderFactory;

    private MainActivityViewModel viewModel;
    private EditText searchTxtView;
    private Button searchBtnView;
    private ProgressBar searchPrgView;

    private Messenger service;

    private boolean isSearchInProgress = false;
    private boolean isSearchInterrupted = false;

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            service = new Messenger(binder);

            if (isSearchInterrupted) {
                search(null);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            service = null;
        }
    };

    private final Messenger responseMessanger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what != SearchService.MSG_SEARCH_APP) return;

            isSearchInterrupted = isSearchInProgress = false;
            setProgressState(false);

            SearchResultDialogFragment resultDialog = new SearchResultDialogFragment();
            resultDialog.setArguments(msg.getData());
            resultDialog.show(getSupportFragmentManager(), null);
        }
    });

    private final SearchDataListAdapter listAdapter = new SearchDataListAdapter() {
        @Override
        protected void onItemClick(View view, SearchQueryData data, int position) {
            if (!isSearchInProgress) {
                searchTxtView.setText(data.getSearchText());
                startSearch(data.getSearchText());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        viewModel = ViewModelProviders.of(this, viewModelProviderFactory).get(MainActivityViewModel.class);
        viewModel.onCreate(this);
        viewModel.getSearchQueryDataList().observe(this, new Observer<List<SearchQueryData>>() {
            @Override
            public void onChanged(@Nullable List<SearchQueryData> list) {
                listAdapter.setData(list);
            }
        });

        RecyclerView listView = findViewById(R.id.list);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.setAdapter(listAdapter);

        searchTxtView = findViewById(R.id.search_txt);

        searchBtnView = findViewById(R.id.search_btn);
        searchBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String searchQuery = searchTxtView.getText().toString().trim();
                if (!TextUtils.isEmpty(searchQuery)) {
                    startSearch(searchQuery);
                }
            }
        });

        searchPrgView = findViewById(R.id.search_prg);

        if (savedInstanceState != null) {
            isSearchInProgress = savedInstanceState.getBoolean(IS_SEARCH_IN_PROG_KEY);
            isSearchInterrupted = isSearchInProgress;
            setProgressState(isSearchInProgress);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, SearchService.class);
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(IS_SEARCH_IN_PROG_KEY, isSearchInProgress);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (service != null) {
            unbindService(serviceConnection);
        }
    }

    private void startSearch(String query) {
        setProgressState(isSearchInProgress = true);
        viewModel.setLastSearchQueryData(new SearchQueryData(query));
        search(query);
    }

    private void search(String query) {
        if (service == null) return;

        Bundle data = new Bundle();
        data.putString(SearchService.PKG_NAME_KEY, getApplication().getPackageName());

        if (!isSearchInterrupted)
            data.putString(SearchService.APP_NAME_KEY, query);

        Message message = Message.obtain(null, SearchService.MSG_SEARCH_APP);
        message.setData(data);
        message.replyTo = responseMessanger;

        try {
            service.send(message);
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to communicate with service", e);
        }
    }

    private void setProgressState(boolean isSearchInProgress) {
        searchBtnView.setEnabled(!isSearchInProgress);
        searchPrgView.setVisibility(
                isSearchInProgress
                        ? View.VISIBLE
                        : View.INVISIBLE);
    }
}
