package com.example.androidrxexample2.domain;

import com.example.androidrxexample2.data.model.Entry;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public interface IInteractor {
    Completable insert(Entry model);
    Flowable<Entry> listener(String string);
    Flowable<List<Entry>> listenerList();
    Completable delete(String string);
}
