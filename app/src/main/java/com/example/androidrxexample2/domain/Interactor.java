package com.example.androidrxexample2.domain;

import android.annotation.SuppressLint;
import android.os.Build;

import com.example.androidrxexample2.data.Repository;
import com.example.androidrxexample2.data.RepositoryContract;
import com.example.androidrxexample2.data.model.Entry;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class Interactor implements IInteractor {
    private RepositoryContract repository;

    public Interactor() {
        this.repository = new Repository();
    }

    @Override
    public Completable insert(Entry model) {
        return Completable.defer(() -> Completable.fromAction(() ->  repository.insert(model)))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(Timber::e);
    }

    @Override
    public Flowable<Entry> listener(String key) {
        return repository.listener(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(Timber::e);
    }

    @Override
    public Flowable<List<Entry>> listenerList() {
//        return repository.listenerList()
//                .flatMap(v -> {
//                    if (v == null){
//                        return Flowable.empty();
//                    }else {
//                        return Flowable.just(v);
//                    }
//                }).filter(v -> !v.isEmpty())
//                .flatMap(v -> {
//                    Flowable<String> date = Flowable.defer(() -> {
//                        return Flowable.just(dateToStringConvert(new Date()));
//                    });
//                    Flowable<String> string = Flowable.defer(() -> {
//                        return Flowable.just(v.get(0).getValue());
//                    });
//                    return Flowable.zip(date, string, (d, t) -> {
//                        v.set(0, new Entry("KEY", d.concat(" ").concat(t.toString())));
//                        return v;
//                    });
//                }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnError(Timber::e);
        Flowable<List<Entry>> entriListFlowable = getEntry();
        Flowable<String> dateFlowable = date();
        return Flowable.defer(() -> {
            return Flowable.zip(dateFlowable, entriListFlowable, (date, entry) -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        if (entry.size() > 1) {
                            entry.forEach(item -> {
                                if (!item.getValue().equals("")) {
                                    if (item.getDate().equals("")) {
    //                                    item.setValue(item.getValue().concat(date));
                                        item.setDate(date);
                                    }
                                }
                            });
                        } else entry.get(0).setValue(entry.get(0).getValue().concat(date));
                    } else entry.get(0).setValue(entry.get(0).getValue().concat(date));
                    return entry;
                });
            }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(Timber::e);
    }

    @SuppressLint("CheckResult")
    @Override
    public Completable delete(String key) {
        return repository.delete(key)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(Timber::e);
    }

    @Override
    public Completable deleteList() {
        return repository.deleteList()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError(Timber::e);
    }

    private Flowable<String> date(){
        return Observable.interval(50, TimeUnit.MILLISECONDS)
            .flatMap(v -> Observable.just(dateToStringConvert(new Date())))
            .toFlowable(BackpressureStrategy.BUFFER);
    }

    private Flowable<List<Entry>> getEntry(){
       return repository.listenerList()
           .flatMap(v -> {
               if (v == null){
                   return Flowable.empty();
               } else {
                   return Flowable.just(v);
               }
           }).filter(v -> !v.isEmpty());
    }

    @SuppressLint("SimpleDateFormat")
    protected static String dateToStringConvert(Date date) {
        Date mDate = date != null ? date : new Date();
        SimpleDateFormat dataFormat = new SimpleDateFormat(" dd.MM.yyyy HH:mm:ss");
        return dataFormat.format(mDate);
    }
}
