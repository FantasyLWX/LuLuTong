package com.fantasy.lulutong.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.fantasy.lulutong.util.ActivityCollector;

/**
 * 自定义活动类，重载了onCreate()和onDestroy()
 * @author Fantasy
 * @version 1.0, 2017-02-17
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 始终保持竖屏
        getSupportActionBar().hide(); // 隐藏状态栏
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}
