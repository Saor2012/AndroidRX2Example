package com.example.androidrxexample2.presentation;

import com.example.androidrxexample2.presentation.base.BaseView;

public interface MainContract {
    interface View extends BaseView {
        void setView(String str);
        String getText();
        String getKey();
        void clearView();
        void toast(String message);
    }

    interface Presenter extends BasePresenter<View>{
        void onClickOk();
        void onClickCancel();
        void onClickDelete();
        void onLongClickCancel();
        void onLongClickDelete();
    }
}
