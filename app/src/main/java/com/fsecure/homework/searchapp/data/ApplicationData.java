package com.fsecure.homework.searchapp.data;

import android.graphics.drawable.Drawable;

public class ApplicationData {

    final String appLabel;
    final String appPackage;
    private final Drawable appIcon;

    public ApplicationData(String appLabel, String appPackage, Drawable appIcon) {
        this.appLabel = appLabel;
        this.appPackage = appPackage;
        this.appIcon = appIcon;
    }

    public String getLabel() {
        return appLabel;
    }

    public String getPackage() {
        return appPackage;
    }

    public Drawable getIcon() {
        return appIcon;
    }

}
