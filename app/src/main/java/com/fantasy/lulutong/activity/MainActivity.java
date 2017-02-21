package com.fantasy.lulutong.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fantasy.lulutong.R;
import com.fantasy.lulutong.fragment.CarFragment;
import com.fantasy.lulutong.fragment.MeFragment;
import com.fantasy.lulutong.fragment.MeLoginFragment;
import com.fantasy.lulutong.fragment.PlaneFragment;
import com.fantasy.lulutong.fragment.TrainFragment;
import com.fantasy.lulutong.util.ActivityCollector;

/**
 * 主界面
 * @author Fantasy
 * @version 1.0, 2017-02-17
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout linearCar;
    private LinearLayout linearTrain;
    private LinearLayout linearPlane;
    private LinearLayout linearMe;

    private ImageView imageCar;
    private ImageView imageTrain;
    private ImageView imagePlane;
    private ImageView imageMe;

    private TextView textCar;
    private TextView textTrain;
    private TextView textPlane;
    private TextView textMe;

    private Fragment carFragment;
    private Fragment trainFragment;
    private Fragment planeFragment;
    private Fragment meFragment;
    /** 点击返回键的时间 */
    private long exitTime = 0;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        linearCar = (LinearLayout) findViewById(R.id.linear_car);
        linearTrain = (LinearLayout) findViewById(R.id.linear_train);
        linearPlane = (LinearLayout) findViewById(R.id.linear_plane);
        linearMe = (LinearLayout) findViewById(R.id.linear_me);

        imageCar = (ImageView) findViewById(R.id.image_car);
        imageTrain = (ImageView) findViewById(R.id.image_train);
        imagePlane = (ImageView) findViewById(R.id.image_plane);
        imageMe = (ImageView) findViewById(R.id.image_me);

        textCar = (TextView) findViewById(R.id.text_car);
        textTrain = (TextView) findViewById(R.id.text_train);
        textPlane = (TextView) findViewById(R.id.text_plane);
        textMe = (TextView) findViewById(R.id.text_me);

        prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        linearCar.setOnClickListener(this);
        linearTrain.setOnClickListener(this);
        linearPlane.setOnClickListener(this);
        linearMe.setOnClickListener(this);

        initFragment(0); // 默认一开始显示第一页，即“汽车票预定”
    }

    @Override
    public void onClick(View v) {
        // 在每次点击后将底部所有按钮的颜色改为默认颜色（灰色），然后根据点击着色
        imageCar.setImageResource(R.mipmap.car_normal);
        imageTrain.setImageResource(R.mipmap.train_normal);
        imagePlane.setImageResource(R.mipmap.plane_normal);
        imageMe.setImageResource(R.mipmap.me_normal);
        textCar.setTextColor(0xff999999);
        textTrain.setTextColor(0xff999999);
        textPlane.setTextColor(0xff999999);
        textMe.setTextColor(0xff999999);

        switch (v.getId()) {
            case R.id.linear_car:
                imageCar.setImageResource(R.mipmap.car_pressed);
                textCar.setTextColor(0xff7cbe01);
                initFragment(0);
                break;
            case R.id.linear_train:
                imageTrain.setImageResource(R.mipmap.train_pressed);
                textTrain.setTextColor(0xff7cbe01);
                initFragment(1);
                break;
            case R.id.linear_plane:
                imagePlane.setImageResource(R.mipmap.plane_pressed);
                textPlane.setTextColor(0xff7cbe01);
                initFragment(2);
                break;
            case R.id.linear_me:
                imageMe.setImageResource(R.mipmap.me_pressed);
                textMe.setTextColor(0xff7cbe01);
                initFragment(3);
                break;
            default:
                break;
        }
    }

    /**
     * 加载Fragment页面
     * @param index 从0开始计数，0表示第一页
     */
    private void initFragment(int index) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // 隐藏所有Fragment
        if (carFragment != null) {
            transaction.hide(carFragment);
        }
        if (trainFragment != null) {
            transaction.hide(trainFragment);
        }
        if (planeFragment != null) {
            transaction.hide(planeFragment);
        }
        if (meFragment != null) {
            transaction.hide(meFragment);
        }

        switch (index) {
            case 0:
                if (carFragment == null) {
                    carFragment = new CarFragment();
                    transaction.add(R.id.frame_content, carFragment);
                } else {
                    transaction.show(carFragment);
                }
                break;
            case 1:
                if (trainFragment == null) {
                    trainFragment = new TrainFragment();
                    transaction.add(R.id.frame_content, trainFragment);
                } else {
                    transaction.show(trainFragment);
                }
                break;
            case 2:
                if (planeFragment == null) {
                    planeFragment = new PlaneFragment();
                    transaction.add(R.id.frame_content, planeFragment);
                } else {
                    transaction.show(planeFragment);
                }
                break;
            case 3:
                if (prefs.getString("name", null) == null) { // 登录名为null，则证明用户未登录
                    if (meFragment == null) {
                        meFragment = new MeLoginFragment();
                        transaction.add(R.id.frame_content, meFragment);
                    } else {
                        transaction.show(meFragment);
                    }
                } else {
                    if (meFragment == null) {
                        meFragment = new MeFragment();
                        transaction.add(R.id.frame_content, meFragment);
                    } else {
                        transaction.show(meFragment);
                    }
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }

    /**
     * 实现点击两次返回键，退出程序的功能
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK &&
                event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis() - exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出路路通",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ActivityCollector.finishAll();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
