package com.app.rxjava.rxjavademo;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/5/21.
 */

public class BaseActivity extends AppCompatActivity {

    public void showMsg(Object content) {
        Toast.makeText(this, content.toString(), Toast.LENGTH_SHORT).show();
    }




}
