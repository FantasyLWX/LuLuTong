package com.fantasy.lulutong.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;

import com.fantasy.lulutong.R;

/**
 * 欢迎界面
 * @author Fantasy
 * @version 1.2, 2017/02/18
 */
public class WelcomeActivity extends Activity {

    // 如果这个类继承BaseActivity，加载速度会慢1秒，从而影响体验

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_welcome);

        // Handler中的postDelayed方法将一个Runnable对象加入主线程中执行，时间延迟3000ms后执行。
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2500);
    }

    // 屏蔽返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return false;
        }
        return false;
    }
}
