package com.fsecure.homework.searchapp.ui;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = {MainActivityModule.class})
    abstract MainActivity contributeMainActivityInjector();

}
