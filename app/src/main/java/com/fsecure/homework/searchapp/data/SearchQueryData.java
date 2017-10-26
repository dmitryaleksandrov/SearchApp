package com.fsecure.homework.searchapp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.security.Timestamp;
import java.util.Date;

@Entity(tableName = "queries")
public class SearchQueryData {

    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "query")
    private String searchText;

    @ColumnInfo(name = "tstamp")
    private long timestamp;

    public SearchQueryData(int id, String searchText, long timestamp) {
        this.id = id;
        this.searchText = searchText;
        this.timestamp = timestamp;
    }

    @Ignore
    public SearchQueryData(String searchText) {
        this.searchText = searchText;
        this.timestamp = new Date().getTime();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SearchQueryData))
            return false;

        SearchQueryData that = (SearchQueryData) obj;

        return searchText.equalsIgnoreCase(that.searchText);
    }
}
