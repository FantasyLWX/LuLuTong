package com.fantasy.lulutong.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.fantasy.lulutong.R;
import com.fantasy.lulutong.activity.AboutActivity;
import com.fantasy.lulutong.activity.InformActivity;
import com.fantasy.lulutong.activity.LoginActivity;
import com.fantasy.lulutong.activity.RegisterActivity;

/**
 * “我的”模块的碎片
 * @author Fantasy
 * @version 1.0, 2017-02-18
 */
public class MeFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout relativeLogin;
    private RelativeLayout relativeRegister;
    private RelativeLayout relativeInform;
    private RelativeLayout relativeAbout;
    private Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        relativeLogin = (RelativeLayout) view.findViewById(R.id.relative_login);
        relativeRegister = (RelativeLayout) view.findViewById(R.id.relative_register);
        relativeInform = (RelativeLayout) view.findViewById(R.id.relative_inform);
        relativeAbout = (RelativeLayout) view.findViewById(R.id.relative_about);

        relativeLogin.setOnClickListener(this);
        relativeRegister.setOnClickListener(this);
        relativeInform.setOnClickListener(this);
        relativeAbout.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_login:
                intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.relative_register:
                intent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(intent);
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
