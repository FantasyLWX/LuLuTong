package com.fantasy.lulutong.activity.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.fantasy.lulutong.R;
import com.fantasy.lulutong.activity.BaseActivity;
import com.fantasy.lulutong.util.ActivityCollector;

/**
 * “通知”的页面
 * @author Fantasy
 * @version 1.0, 2017-02-
 */
public class InformActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout relativeBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_inform);

        relativeBack = (RelativeLayout) findViewById(R.id.relative_inform_back);

        relativeBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_inform_back:
                finish();
                break;
            /*case :
                break;*/
            default:
                break;
        }
    }

}
