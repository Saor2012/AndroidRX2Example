package com.example.androidrxexample2.presentation.base;

public interface IRouter {
     void initView(BaseView view);
     void destroyView();
     void transition(String key);
}
