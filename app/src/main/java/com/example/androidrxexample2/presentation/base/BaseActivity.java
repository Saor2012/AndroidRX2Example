package com.example.androidrxexample2.presentation.base;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidrxexample2.presentation.BasePresenter;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());
        createView(savedInstanceState);
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    @Override
    protected void onResume() {
        super.onResume();
        resumeView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startView();
    }

    @Override
    protected void onStop() {
        stopView();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (getPresenter() != null){
            getPresenter().stopView();
        }
        destroyView();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        pauseView();
        super.onPause();
    }

    protected abstract BasePresenter getPresenter();

    protected abstract void createView(@Nullable Bundle savedInstanceState);

    protected abstract void destroyView();

    protected abstract void startView();

    protected abstract void stopView();

    protected abstract void resumeView();

    protected abstract void pauseView();


}
