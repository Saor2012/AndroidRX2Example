package com.example.androidrxexample2.presentation.base;

import com.example.androidrxexample2.presentation.MainActivity;

public class Router implements IRouter {
    private BaseView view;
    private static IRouter instance;
 /* <--
 *1. Зробити роботу з роутером
 *2. Зв'язок між активностями - Intent: дві активності в першій eidtText, button, в другої eidtText, button, textView
 * Якщо натиснути на кнопку, перейти на іншу активність та вивести значення з попередньої активності, завершити роботу активності 1.
 * Якщо натиснути на кнопку, повернути значення Ок та перейти на активність 1, завершити роботу активності 2.
 *3. Виконати вказівки за файлом TZ.docs*/
    private Router() {}

    @Override
    public void initView(BaseView view) {
        this.view = view;
    }

    @Override
    public void destroyView() {
        if (view != null) {
            view = null;
        }
    }

    public static synchronized IRouter getInstance() {
        return instance;
    }

    @Override
    public void transition(String key) {
        switch (key) {
            case "Main":
                view.transactionActivity(MainActivity.class);
                break;
            default:
                break;
        }
    }
}
