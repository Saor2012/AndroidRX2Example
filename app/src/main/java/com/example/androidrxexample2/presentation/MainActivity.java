package com.example.androidrxexample2.presentation;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.androidrxexample2.R;
import com.example.androidrxexample2.presentation.base.BaseActivity;

public class MainActivity extends BaseActivity implements MainContract.View {
    private MainContract.Presenter presenter;
    private AppCompatTextView textInput;
    private AppCompatEditText editTextOutput, editKeyOutput;
    private AppCompatButton btnOk, btnCancel, btnDelete;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    protected void createView(@Nullable Bundle savedInstanceState) {
        presenter = new Presenter();
        textInput = findViewById(R.id.output);
        editKeyOutput = findViewById(R.id.enterKey);
        editTextOutput = findViewById(R.id.enterText);
        btnOk = findViewById(R.id.ok);
        btnCancel = findViewById(R.id.cancel);
        btnDelete = findViewById(R.id.delete);
        btnOk.setOnClickListener(v -> {
            presenter.onClickOk();
        });
        btnCancel.setOnClickListener(v ->{
            presenter.onClickCancel();
        });
        btnDelete.setOnClickListener(v ->{
            presenter.onClickCancel();
        });
    }

    @Override
    protected void destroyView() {
        if (presenter != null) presenter = null;
        if (textInput != null) textInput = null;
        if (editKeyOutput != null) editKeyOutput = null;
        if (editTextOutput != null) editTextOutput =null;
        if (btnOk != null) btnOk = null;
        if (btnCancel != null) btnCancel = null;
    }

    @Override
    protected void startView() {
        presenter.startView(this);
    }

    @Override
    protected void stopView() {

    }

    @Override
    protected void resumeView() {

    }

    @Override
    protected void pauseView() {

    }


    @Override
    public void setView(String str) {
        textInput.setText(str);
    }

    @Override
    public String getText() {
        return String.valueOf(editTextOutput.getText());
    }

    @Override
    public String getKey() {
        return String.valueOf(editKeyOutput.getText());
    }

    @Override
    public void clearView() {
        textInput.setText("");
    }
}
