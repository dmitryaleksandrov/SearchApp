package com.fsecure.homework.searchapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface SearchQueryDao {

    @Query("SELECT * FROM queries ORDER BY tstamp DESC")
    LiveData<List<SearchQueryData>> getSearchQueryDataList();

    @Delete
    void deleteSearchQueryData(SearchQueryData queryData);

    @Insert(onConflict = REPLACE)
    void insertSearchQueryData(SearchQueryData queryData);

}
