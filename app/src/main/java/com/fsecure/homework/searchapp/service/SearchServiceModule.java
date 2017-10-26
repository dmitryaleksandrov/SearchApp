package com.fsecure.homework.searchapp.service;

import android.content.Context;

import java.util.concurrent.Executors;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class SearchServiceModule {

    @Provides
    static SearchServiceManager provideSearchServiceManager(Context context, @Named("delayMillis") long delayMillis) {
        return new SearchServiceManagerImpl(context, Executors.newCachedThreadPool(), delayMillis);
    }

    @ContributesAndroidInjector
    abstract SearchService contributeSearchService();

}
