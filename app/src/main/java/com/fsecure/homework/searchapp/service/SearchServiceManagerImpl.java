package com.fsecure.homework.searchapp.service;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SearchServiceManagerImpl implements SearchServiceManager {
    private static final String TAG = SearchServiceManagerImpl.class.getSimpleName();

    private final Context context;
    private final ExecutorService executorService;
    private final long delayMillis;

    private final Map<String, ArrayList<ApplicationInfo>> resultCache = new HashMap<>();
    private final Map<String, SearchAppMessenger> messengers = new HashMap<>();

    private final Handler handler;

    @Inject
    public SearchServiceManagerImpl(Context context, ExecutorService executorService, long delayMillis) {
        this(context, executorService, delayMillis, new Handler());
    }

    public SearchServiceManagerImpl(Context context, ExecutorService executorService, long delayMillis,
                                    Handler handler) {
        this.context = context;
        this.executorService = executorService;
        this.delayMillis = delayMillis;
        this.handler = handler;
    }

    @Override
    public void handleSearchAppRequest(String appName, String pkgName, SearchAppMessenger messenger) {
        messengers.put(pkgName, messenger);

        if (appName != null) {
            executorService.submit(new SearchTask(appName, pkgName));
        } else if (resultCache.containsKey(pkgName)) {
            ArrayList<ApplicationInfo> appInfoList = resultCache.get(pkgName);
            if (appInfoList != null && onResultAvailable(pkgName, appInfoList)) {
                resultCache.remove(pkgName);
            }
        }
    }

    public void shutdown() {
        executorService.shutdownNow();
    }

    private boolean onResultAvailable(String pkgName, ArrayList<ApplicationInfo> appInfoList) {
        if (messengers.containsKey(pkgName)) {
            SearchAppMessenger messenger = messengers.get(pkgName);

            try {
                messenger.send(appInfoList);
            } catch (RemoteException e) {
                resultCache.put(pkgName, appInfoList);
                return false;
            } finally {
                messengers.remove(pkgName);
            }

            return true;
        } else {
            resultCache.put(pkgName, appInfoList);
            return false;
        }
    }

    private class SearchTask implements Runnable {

        private final String appName;
        private final String pkgName;

        private SearchTask(String appName, String pkgName) {
            this.appName = appName;
            this.pkgName = pkgName;
        }

        @Override
        public void run() {
            long tstamp = System.currentTimeMillis() + delayMillis;

            ArrayList<ApplicationInfo> appInfoList = new ArrayList<>();
            PackageManager pm = context.getPackageManager();

            for (ApplicationInfo ai : pm.getInstalledApplications(0)) {
                if (appName.equalsIgnoreCase(pm.getApplicationLabel(ai).toString())) {
                    appInfoList.add(ai);
                }
            }

            long diff = tstamp - System.currentTimeMillis();
            if (diff > 0) {
                try {
                    Thread.sleep(diff);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            submitResult(appInfoList);
        }

        private void submitResult(final ArrayList<ApplicationInfo> appInfoList) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    onResultAvailable(pkgName, appInfoList);
                }
            });
        }
    }


}
