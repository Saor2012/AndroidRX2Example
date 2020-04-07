package com.example.androidrxexample2.data;

import com.example.androidrxexample2.app.App;
import com.example.androidrxexample2.data.model.Entry;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import timber.log.Timber;

public class Repository implements RepositoryContract {

    @Override
    public void insert(Entry model) {
        App.localeStorage().insertEntry(model);
    }

    @Override
    public Flowable<Entry> listener(String key) {
        return App.localeStorage().queryEntry(key)
                .doOnError(throwable -> Timber.e("Error App.localeStorage().queryEntry(key) - %s", throwable.getMessage()));
    }

    @Override
    public Flowable<List<Entry>> listenerList() {
        return App.localeStorage().queryEntryList()
                .doOnError(throwable -> Timber.e("Error App.localeStorage().queryEntryList() - %s", throwable.getMessage()));
    }

    @Override
    public Completable delete(String key) {
        return App.localeStorage().deleteEntry(key)
                .doOnError(throwable -> Timber.e("Error App.localeStorage().deleteEntry(key) - %s", throwable.getMessage()));
    }
}
