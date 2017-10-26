package com.fsecure.homework.searchapp.ui;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.fsecure.homework.searchapp.data.AppDatabase;

import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule {
    @Provides
    MainActivityViewModel provideMainActivityViewModel(AppDatabase database) {
        return new MainActivityViewModel(database);
    }

    @Provides
    ViewModelProvider.Factory provideViewModelProviderFactory(final MainActivityViewModel viewModel) {
        return new ViewModelProvider.Factory() {
            @Override
            public <T extends ViewModel> T create(Class<T> modelClass) {
                if (modelClass.isAssignableFrom(viewModel.getClass())) {
                    return (T) viewModel;
                }

                throw new IllegalArgumentException("Unknown view model type");
            }
        };
    }
}
