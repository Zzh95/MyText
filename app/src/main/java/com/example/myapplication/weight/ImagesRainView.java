package com.example.myapplication.weight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.bean.ImageRainBean;
import com.example.myapplication.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @data:2019/6/16
 * @author:zzh
 * @description:多个图片从顶部飘下来（图片雨）
 */
public class ImagesRainView extends View {
    //图片雨是否在运行
    private boolean isRaining = false;
    //设置图片的大小
    private float mBitmapHeight, mBitmapWidth;
    private Context context;
    //下落的持续时间
    private int maxDuration = 2000;
    //两张图片间隔时间
    private int mInterval = 200;
    //获取随机数的工具
    private Random random;
    private List<ImageRainBean> imageRainList = new ArrayList<>();
    //结合Canvas对bitmap进行处理的工具
    private Matrix matrix;
    //画笔
    private Paint paint;
    private long mStartTime;

    public ImagesRainView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        random = new Random();
        matrix = new Matrix();
        paint = new Paint();

    }

    public void startRain(final List<Bitmap> bitmaps) {
        stopRain();
        setVisibility(VISIBLE);
        initBitmap(bitmaps);
        isRaining = true;
        invalidate();
    }

    /**
     * 初始化bitmap
     *
     * @param bitmaps
     */
    private void initBitmap(final List<Bitmap> bitmaps) {
        mBitmapHeight = mBitmapWidth = DisplayUtil.dip2px(context, 50);

        imageRainList.clear();
        mStartTime = System.currentTimeMillis();
        int currentDuration = 0;
        int i = 0;
        int size = bitmaps.size();

        while (currentDuration < maxDuration) {
            ImageRainBean bean = new ImageRainBean();
            bean.setBitmap(bitmaps.get(i % size));
            bean.setX(random.nextInt(getWidth()));
            bean.setY(-(int) (Math.ceil(mBitmapHeight)));

            float duration = random.nextInt(500) + maxDuration;
            //android系统中每16毫秒系统会发出一次重绘的消息
            bean.setVelocityY((int) (getHeight() * 16 / duration));
            bean.setVelocityX(Math.round(random.nextFloat()));
            bean.setAppearTime(currentDuration);

            currentDuration += random.nextInt(250);
            imageRainList.add(bean);
            i++;
        }
    }

    public void stopRain() {
        setVisibility(GONE);
        isRaining = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //如果图片雨正在运行或bean对象初始化失败，则直接返回
        if (!isRaining || imageRainList.size() == 0) {
            return;
        }
        //当前时间距离开始时间的间隔
        long totalTime = System.currentTimeMillis() - mStartTime;
        for (int i = 0; i < imageRainList.size(); i++) {
            ImageRainBean rainBean = imageRainList.get(i);
            if (rainBean.getBitmap().isRecycled() || rainBean.getY() > getHeight()
                    || totalTime < rainBean.getAppearTime()) {
                continue;
            }
            //缩放图片，将其宽缩为mBitmapWidth，高缩为mBitmapHeight
            matrix.reset();
            float heightScale = mBitmapHeight / rainBean.getBitmap().getHeight();
            float widthScale = mBitmapWidth / rainBean.getBitmap().getWidth();
            matrix.setScale(heightScale, widthScale);

            //移动距离为X，Y原来的位置加上随机变化的速率，得到新的X，Y坐标
            rainBean.setX(rainBean.getX() + rainBean.getVelocityX());
            rainBean.setY(rainBean.getY() + rainBean.getVelocityY());

            //设置新的位置
            matrix.postTranslate(rainBean.getX(), rainBean.getY());
            //绘制方法
            canvas.drawBitmap(rainBean.getBitmap(), matrix, paint);
        }
        //调用该函数进行重绘
        postInvalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean onclienter=false;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //获取屏幕上点击的坐标
                float x=event.getX();
                float y = event.getY();
                //如果坐标在我们的区域内，则设置点击事件
                for (int i=0;i<imageRainList.size();i++) {
                    if (x > imageRainList.get(i).getX() && x>imageRainList.get(i).getX()+50
                    && y>imageRainList.get(i).getY() && y<imageRainList.get(i).getY()+50) {
                        Toast.makeText(context,"点击",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                //点击抬起后，回复初始位置。

                return true;
        }
        return super.onTouchEvent(event);
    }
}
