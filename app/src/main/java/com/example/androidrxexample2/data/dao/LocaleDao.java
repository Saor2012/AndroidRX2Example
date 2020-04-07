package com.example.androidrxexample2.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.androidrxexample2.data.model.Entry;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface LocaleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertListEntry(List<Entry> list);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEntry(Entry entry);

    @Query("select * from entry_model2")
    Flowable<List<Entry>> queryEntryList();

    @Query("SELECT * FROM entry_model2 WHERE keyId IS :key")
    Flowable<Entry> queryEntry(String key);

    @Query("DELETE FROM entry_model2 WHERE keyId = :key")
    Completable deleteEntry(String key);

    @Query("DELETE FROM entry_model2")
    Completable deleteEntryList();
}
