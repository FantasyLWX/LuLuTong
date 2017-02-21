package com.fantasy.lulutong.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fantasy.lulutong.R;
import com.fantasy.lulutong.activity.me.AboutActivity;
import com.fantasy.lulutong.activity.me.InformActivity;
import com.fantasy.lulutong.activity.me.RegisterActivity;
import com.fantasy.lulutong.activity.me.UserInfoActivity;

/**
 * 已登录，“我的”模块的碎片
 * @author Fantasy
 * @version 1.0, 2017-02-21
 */
public class MeFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout relativeName;
    private RelativeLayout relativeQueryOrder;
    private RelativeLayout relativeSetting;
    private RelativeLayout relativeInform;
    private RelativeLayout relativeAbout;
    private TextView textName;
    private Intent intent;
    private SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        relativeName = (RelativeLayout) view.findViewById(R.id.relative_name);
        relativeQueryOrder = (RelativeLayout) view.findViewById(R.id.relative_query_order);
        relativeSetting = (RelativeLayout) view.findViewById(R.id.relative_setting);
        relativeInform = (RelativeLayout) view.findViewById(R.id.relative_inform);
        relativeAbout = (RelativeLayout) view.findViewById(R.id.relative_about);
        textName = (TextView) view.findViewById(R.id.text_name);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        textName.setText(prefs.getString("real_name", null));

        relativeName.setOnClickListener(this);
        relativeQueryOrder.setOnClickListener(this);
        relativeSetting.setOnClickListener(this);
        relativeInform.setOnClickListener(this);
        relativeAbout.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_name:
                intent = new Intent(getActivity(), UserInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.relative_query_order: // 订单查询
                //intent = new Intent(getActivity(), RegisterActivity.class);
                //startActivity(intent);
                break;
            case R.id.relative_setting: // 设置
                //intent = new Intent(getActivity(), RegisterActivity.class);
                //startActivity(intent);
                break;
            case R.id.relative_inform:
                intent = new Intent(getActivity(), InformActivity.class);
                startActivity(intent);
                break;
            case R.id.relative_about:
                intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}
