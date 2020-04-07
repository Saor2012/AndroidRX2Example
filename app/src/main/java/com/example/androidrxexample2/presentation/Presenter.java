package com.example.androidrxexample2.presentation;

import android.annotation.SuppressLint;
import android.os.Build;

import com.example.androidrxexample2.data.model.Entry;
import com.example.androidrxexample2.domain.IInteractor;
import com.example.androidrxexample2.domain.Interactor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import timber.log.Timber;

public class Presenter implements MainContract.Presenter {
    private MainContract.View view;
    private IInteractor interactor;
    private String data;
    private Disposable disposable;
    private int curremtOreration = -1, isHide = 0;

    private List<Entry> list;

    public Presenter() {
        interactor = new Interactor();
    }

    @Override
    public void startView(MainContract.View view) {
        this.view = view;
        final String[] response = {""};
        final int[] isReady = {0};
        interactor.listenerList()
            .subscribe(new DisposableSubscriber<List<Entry>>() {
                @Override
                public void onNext(List<Entry> entries) {
                    Timber.e("listenerList onNext list size %s",entries.size());
                    list = entries;
                    if (entries.size() > 0) isReady[0] = 1;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        if (entries.size() > 1) {
                            final int[] index = {0};
                            final String[] response = {""};

                            entries.forEach(item -> {
                                if (index[0] < 5) {
                                    response[0] += item.getValue().concat(item.getDate().equals("") ? " - " : item.getDate() + " - ").concat(item.getKeyId()).concat("\n");
                                   /* Timber.e("Value: %s", item.getValue());
                                    Timber.e("Date: %s", item.getDate());
                                    Timber.e("Index: %s", index[0]);
                                    Timber.e("Text: %s", response[0]); //<---*/
                                }
                                index[0] += 1;
                            });
                            view.setView(response[0]);
                            Timber.e("Text: %s", response[0]);
                        } else view.setView(entries.get(0).getValue());
                    } else view.setView(entries.get(0).getValue());
                }

                @Override
                public void onError(Throwable t) {
                    Timber.e(t);
                }

                @Override
                public void onComplete() {
                    Timber.e("interactor.listenerList onComplete");
                }
            });
        /*if (isReady[0] == 0) {
            interactor.insert(new Entry("KEY","Start text"))
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Timber.e("interactor.insert() onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
        }*/
    }

    @Override
    public void stopView() {
        if (view != null) view = null;
        if (data != null) data = null;
    }

    private void selectOper(int oper) {
        switch (oper){
            case 1 :
                curremtOreration = 1;
                singleJust();
                break;
            case 2:
                curremtOreration = 2;
                obsInterval();
                break;
            case 3:
                curremtOreration = 3;
                zip();
                break;
            case 4:
                curremtOreration = 4;
                megre();
                break;
            case 5:
                curremtOreration = 5;
                listener();
                break;
            case 6:
                curremtOreration = 6;
                add();
                break;
            default: throw new NullPointerException();
        }
    }

    private void add() {
        if (!view.getKey().equals("") && !view.getKey().equals("KEY2") || !view.getText().equals("")) {
            interactor.insert(new Entry(view.getKey(),view.getText()))
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Timber.e("interactor.insert() onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
        }
    }

    private void listener() {
        interactor.insert(new Entry("KEY2",view.getText()))
            .subscribe(new DisposableCompletableObserver() {
                @Override
                public void onComplete() {
                    Timber.e("interactor.insert() onComplete");
                }

                @Override
                public void onError(Throwable e) {
                    Timber.e(e);
                }
            });
    }

    private void megre() {
        Single<String> str = Single.defer(() -> {
            return Single.just(dateToStringConvertB(new Date()));
        }).subscribeOn(Schedulers.io());
        Single<String> str2 = Single.defer(() -> {
            return Single.timer(3, TimeUnit.SECONDS).flatMap(v -> {

                return Single.just(view.getText());
            });
//            .delay(5, TimeUnit.SECONDS)
        }).subscribeOn(AndroidSchedulers.mainThread());

        Observable.defer(() -> {
            return Observable.merge(str.toObservable(), str2.toObservable());
        }).buffer(2)
        .timeout(2, TimeUnit.SECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .doOnError(throwable -> {
            Timber.e("Error: %s", throwable.getMessage());
        }).subscribe(new DisposableObserver<List<String>>() {
            /*@Override
            public void onNext(String s) {

//                dispose();
            }*/

            @Override
            public void onNext(List<String> strings) {
                view.setView(strings.get(0).concat(" ").concat(strings.get(1)));
            }

            @Override
            public void onError(Throwable e) {
                view.setView(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    @SuppressLint("CheckResult")
    private void zip() {
        Single<String> str = Single.defer(() -> {
            return Single.just(dateToStringConvertB(new Date()));
        }).subscribeOn(Schedulers.io());
        Single<String> str2 = Single.defer(() -> {
            return Single.just(view.getText());
        }).subscribeOn(AndroidSchedulers.mainThread());

        Single.defer(() -> Single.zip(str, str2, (d, t) -> {
            return d.concat(" ").concat(t.toString());
        })).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableSingleObserver<String>() {
            @Override
            public void onSuccess(String s) {
                view.setView(s);
                dispose();
            }

            @Override
            public void onError(Throwable e) {
                Timber.e("Error: %s", e.getMessage());
            }
        });
    }

    private Observable<String> convert(Long value) {
        return Observable.just(value)
                .map(v -> String.valueOf(value).concat(" ").concat("test"));
    }

    private void singleJust(){
        Timber.e("before Single");
        /*Single.just(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSingleObserver<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Timber.e("onSuccess Single");
                        view.setView(s);
                        dispose();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("onError Single");
                    }
                });*/
        Observable.just("Text")
            .timestamp()
            .concatMap(v -> {
                return Observable.interval(1, TimeUnit.SECONDS, Schedulers.io())
                    .concatMap(val -> convert(val));
            }).observeOn(AndroidSchedulers.mainThread())
            .subscribe(new DisposableObserver<String>() {
                @Override
                public void onNext(String s) {
                    Timber.e("onNext: %s", s);
                }

                @Override
                public void onError(Throwable e) {
                    Timber.e("onError: %s", e);
                }

                @Override
                public void onComplete() {
                    Timber.e("onComplete");
                }
            });
        Timber.e("after Single");
//           Single<String> singleA = Single.just(data);
//           Single<String> singleB = Single.just(data);
//           Single<String> singleC = Single.just(data);
//
//           Single<String> zip = Single.zip(singleA, singleB, singleC, (s, s2, s3) -> s+s2+s3);
//        Flowable<String> merge = Single.merge(singleA, singleB, singleC);
//        merge.lastOrError().
//                subscribe(new DisposableSingleObserver<String>() {
//                    @Override
//                    public void onSuccess(String s) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//                });
//


    }

    @SuppressLint("SimpleDateFormat")
    public static String dateToStringConvertB(Date date) {
        Date mDate = date != null ? date : new Date();
        SimpleDateFormat dataFormat = new SimpleDateFormat(" dd.MM.yyyy HH:mm:ss");
        return dataFormat.format(mDate);
    }

    private void obsInterval(){
        Observable.interval(1000,500, TimeUnit.MILLISECONDS,Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        String val = dateToStringConvertB(new Date());
                        view.setView(val);
                        Timber.e("obsInterval onNext %s",val);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onClickOk() {
        data = view.getText();
        if (data == null){
            throw  new NullPointerException();
        }
        selectOper(6);
    }

    @Override
    public void onClickCancel() {

    }

    @Override
    public void onClickDelete() {
        if (!view.getKey().equals("")) {
            interactor.delete(view.getKey())
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Timber.e("onClickDelete() onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
            /*interactor.listenerList().subscribe(new DisposableSubscriber<List<Entry>>() {
                @Override
                public void onNext(List<Entry> entries) {
                    Timber.e("onClickDelete() > listenerList onNext list size %s",entries.size());
                    if (list.size() == entries.size()) {
                        Timber.e("Exeption at delete");
                    } else {
                        Timber.e("Complete at delete");
                        Timber.e("Value: %s", entries.toString());
                    }
                }

                @Override
                public void onError(Throwable t) {
                    Timber.e(t);
                }

                @Override
                public void onComplete() {
                    Timber.e("interactor.listenerList onComplete");
                }
            });*/
        } else {
            view.setView("Введіть ключ");
            Timber.e("Check out key for delete item");
        }
    }

    @Override
    public void onLongClickCancel() {
        if (isHide == 0) {
            isHide = 1;
            view.setView(list.get(0).getValue().concat(list.get(0).getDate() + " - ").concat(list.get(0).getKeyId()).concat("\n"));
        } else {
            isHide = 0;
            final String[] value = {""};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                list.forEach(item -> {
                    value[0] += item.getValue().concat(item.getDate().equals("") ? " - " : item.getDate() + " - ").concat(item.getKeyId()).concat("\n");
                });
            } else {
                for (int i = 0; i < list.size(); i++) {
                    value[0] += list.get(i).getValue().concat(list.get(i).getDate().equals("") ? " - " : list.get(i).getDate() + " - ").concat(list.get(i).getKeyId()).concat("\n");
                }
            }
            view.setView(value[0]);
        }
    }

    @Override
    public void onLongClickDelete() {
        String lastStr = view.getText();
        if (view.getKey().equals("KEY2")) {
            interactor.deleteList()
                .subscribe(new DisposableCompletableObserver() {
                    @Override
                    public void onComplete() {
                        Timber.e("onLongClickDelete() onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("onLongClickDelete onError: %s", e);
                    }
                });
            view.setView("");
        } else {
            view.setView("Введіть головний ключ");
            Timber.e("Check out main key for delete item");
            Observable.timer(4, TimeUnit.SECONDS, Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long aLong) {
                        if (aLong == 0) {
                            view.setView(lastStr);
                        }
                        Timber.e("onLongClickDelete() onNext: %s", aLong);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("onLongClickDelete onError: %s", e);
                    }

                    @Override
                    public void onComplete() {
                        Timber.e("onLongClickDelete() onComplete");
                    }
                });
        }
    }
}
