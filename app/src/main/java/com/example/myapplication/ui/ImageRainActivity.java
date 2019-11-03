package com.example.myapplication.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.weight.ImagesRainView;
import com.example.myapplication.weight.OneImageRainView;

import java.util.ArrayList;
import java.util.List;

public class ImageRainActivity extends Activity implements View.OnClickListener {
    private TextView mTvRain;
    private OneImageRainView mOneImageRainView;
    private ImagesRainView mImagesRains;
    private TextView mTvRains;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rain);
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mOneImageRainView != null) {
            mOneImageRainView.stopRain();
        }
    }

    private void initView() {
        mTvRain = (TextView) findViewById(R.id.btn_rain);
        mTvRain.setOnClickListener(this);
        mOneImageRainView = (OneImageRainView) findViewById(R.id.rain);
        mImagesRains = (ImagesRainView) findViewById(R.id.rains);
        mTvRains = (TextView) findViewById(R.id.tv_rains);
        mTvRains.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_rain:
                mOneImageRainView.startRain(BitmapFactory.decodeResource(getResources(), R.mipmap.image1));
                break;
            case R.id.tv_rains:
                mImagesRains.startRain(setList());
                break;
        }
    }

    private List<Bitmap> setList() {
        List<Bitmap> mList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            mList.add(BitmapFactory.decodeResource(getResources(), R.mipmap.image1));
        }
        return mList;
    }
}
