package com.example.androidrxexample2.presentation;

public interface MainContract {
    interface View{
        void setView(String str);

        String getText();

        String getKey();

        void clearView();
    }

    interface Presenter extends BasePresenter<View>{
        void onClickOk();

        void onClickCancel();

        void onClickDelete();
    }
}
