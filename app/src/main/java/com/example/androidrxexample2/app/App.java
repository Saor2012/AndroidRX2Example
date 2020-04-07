package com.example.androidrxexample2.app;

import android.app.Application;

import androidx.room.Room;

import com.example.androidrxexample2.data.dao.DataBase;
import com.example.androidrxexample2.data.dao.LocaleDao;

import timber.log.Timber;

public class App extends Application {
    private static LocaleDao localeDao;

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        localeDao = initRoom().getDao();
    }

    private DataBase initRoom(){
        return Room.databaseBuilder(
                this.getApplicationContext(), DataBase.class, "dao_test")
                .build();
    }

    public static LocaleDao localeStorage(){
        return localeDao;
    }
}
