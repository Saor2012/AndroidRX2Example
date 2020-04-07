package com.example.androidrxexample2.presentation;

public interface BasePresenter<View> {
    void startView(View view);

    void stopView();
}
