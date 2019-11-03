package com.example.myapplication.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.ui.ImageRainActivity;
import com.example.myapplication.ui.MyWebViewTestActivity;

public class MainFragment extends Fragment implements View.OnClickListener {
    private View mView;
    private Button mBtnJump;
    private Button mBtnWebView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.fragment_main,null);
        initView();
        return mView;
    }

    private void initView() {
        mBtnJump= (Button) mView.findViewById(R.id.btn_jump);
        mBtnJump.setOnClickListener(this);

        mBtnWebView= (Button) mView.findViewById(R.id.btn_jump2);
        mBtnWebView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_jump:
                Intent intent=new Intent(getActivity(), ImageRainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_jump2:
                Intent intent2=new Intent(getActivity(), MyWebViewTestActivity.class);
                startActivity(intent2);
                break;

        }
    }
}
