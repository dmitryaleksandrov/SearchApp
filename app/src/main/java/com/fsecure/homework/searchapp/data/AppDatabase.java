package com.fsecure.homework.searchapp.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {SearchQueryData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "search_app_db";

    public abstract SearchQueryDao searchQueryDao();
}
