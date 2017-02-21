package com.fantasy.lulutong.activity.me;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.fantasy.lulutong.R;
import com.fantasy.lulutong.activity.BaseActivity;

/**
 * “注册”的页面
 * @author Fantasy
 * @version 1.0, 2017-02-
 */
public class RegisterActivity extends BaseActivity {

    private RelativeLayout relativeBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        relativeBack = (RelativeLayout) findViewById(R.id.relative_register_back);
        relativeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
