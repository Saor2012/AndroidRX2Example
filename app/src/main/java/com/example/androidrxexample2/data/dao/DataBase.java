package com.example.androidrxexample2.data.dao;


import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.androidrxexample2.data.model.Entry;

@Database(entities = {Entry.class}, version = 1,exportSchema = false)
public abstract class DataBase extends RoomDatabase {
    public abstract LocaleDao getDao();
}
