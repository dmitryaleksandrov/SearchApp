package com.fsecure.homework.searchapp.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class SearchService extends Service {

    public static final String TAG = SearchService.class.getSimpleName();


    public static final int MSG_SEARCH_APP = 1;

    public static final String PKG_NAME_KEY = "pkg_name";
    public static final String APP_NAME_KEY = "app_name";

    private Messenger serviceMessenger;

    @Inject
    SearchServiceManager searchServiceManager;

    public void onCreate() {
        super.onCreate();
        AndroidInjection.inject(this);

        serviceMessenger = new Messenger(new Handler() {
            public void handleMessage(Message msg) {
                onMessageReceived(msg);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        searchServiceManager.shutdown();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return serviceMessenger.getBinder();
    }

    private boolean onMessageReceived(Message msg) {

        switch (msg.what) {
            case MSG_SEARCH_APP:
                Log.d(TAG, "MSG_SEARCH_APP request was received");
                search(msg);
                return true;

            default:
                return false;
        }
    }

    private void search(Message msg) {
        Bundle data = msg.getData();

        if (data != null && data.containsKey(PKG_NAME_KEY)) {
            String pkgName = data.getString(PKG_NAME_KEY);
            String appName = data.getString(APP_NAME_KEY);

            searchServiceManager.handleSearchAppRequest(appName, pkgName,
                    new SearchAppMessenger(msg.replyTo));
        }
    }

    private class SearchAppMessenger implements SearchServiceManager.SearchAppMessenger {

        private final Messenger replyTo;

        private SearchAppMessenger(Messenger replyTo) {
            this.replyTo = replyTo;
        }

        @Override
        public void send(ArrayList<ApplicationInfo> appInfoList) throws RemoteException {
            Bundle data = new Bundle();
            data.putParcelableArrayList(null, appInfoList);

            Message message = Message.obtain(null, MSG_SEARCH_APP);
            message.setData(data);
            replyTo.send(message);
        }
    }
}
