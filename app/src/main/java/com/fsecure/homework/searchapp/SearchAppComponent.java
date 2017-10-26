package com.fsecure.homework.searchapp;

import android.content.Context;

import com.fsecure.homework.searchapp.data.DataManagerModule;
import com.fsecure.homework.searchapp.service.SearchService;
import com.fsecure.homework.searchapp.service.SearchServiceModule;
import com.fsecure.homework.searchapp.ui.ActivityBuilder;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {AndroidInjectionModule.class, ActivityBuilder.class, DataManagerModule.class,
        SearchServiceModule.class})
public interface SearchAppComponent {

    void inject(SearchApp app);

    void inject(SearchService service);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder setApplicationContext(Context applicationContext);

        @BindsInstance
        Builder setDelayMillis(@Named("delayMillis") long delayMillis);

        SearchAppComponent build();
    }
}
