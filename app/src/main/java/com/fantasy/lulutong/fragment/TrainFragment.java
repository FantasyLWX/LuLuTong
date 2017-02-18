package com.fantasy.lulutong.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fantasy.lulutong.R;

/**
 * “火车票预定”模块的碎片
 * @author Fantasy
 * @version 1.0, 2017-02-18
 */
public class TrainFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_train, container, false);
        return view;
    }
}
