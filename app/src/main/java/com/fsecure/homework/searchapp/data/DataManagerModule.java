package com.fsecure.homework.searchapp.data;

import android.arch.persistence.room.Room;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DataManagerModule {
    @Singleton
    @Provides
    static AppDatabase createAppDatabase(Context applicationContext) {
        return Room.databaseBuilder(applicationContext, AppDatabase.class,
                AppDatabase.DATABASE_NAME).build();
    }

}
