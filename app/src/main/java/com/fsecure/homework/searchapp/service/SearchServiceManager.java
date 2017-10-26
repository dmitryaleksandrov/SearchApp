package com.fsecure.homework.searchapp.service;

import android.content.pm.ApplicationInfo;
import android.os.RemoteException;

import java.util.ArrayList;

public interface SearchServiceManager {

    void handleSearchAppRequest(String appName, String pkgName, SearchAppMessenger messenger);

    void shutdown();

    interface SearchAppMessenger {
        void send(ArrayList<ApplicationInfo> appInfoList) throws RemoteException;
    }

}
