package com.example.myapplication.weight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.myapplication.bean.ImageRainBean;
import com.example.myapplication.util.DisplayUtil;

import java.util.Random;
/**
 *@data:2019/6/16
 *@author:zzh
 *@description:单个图片从顶部落下来
 */
public class OneImageRainView extends View {
    //图片雨是否在运行
    private boolean isRaining=false;
    //设置图片的大小
    private float mBitmapHeight,mBitmapWidth;
    private Context context;
    //下落的最大时间
    private int maxDuration=2000;
    //获取随机数的工具
    private Random random;
    private ImageRainBean imageRainBean;
    //结合Canvas对bitmap进行处理的工具
    private Matrix matrix;
    //画笔
    private Paint paint;

    public OneImageRainView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        random=new Random();
        matrix=new Matrix();
        paint=new Paint();
    }

    public void startRain(final Bitmap bitmap){
        stopRain();
        setVisibility(VISIBLE);
        initBitmap(bitmap);
        isRaining=true;
        invalidate();
    }

    /**
     * 初始化bitmap
     * @param bitmap
     */
    private void initBitmap(final Bitmap bitmap) {
        mBitmapHeight=mBitmapWidth= DisplayUtil.dip2px(context,50);

        ImageRainBean bean=new ImageRainBean();
        bean.setBitmap(bitmap);
        bean.setX(random.nextInt(getWidth()));
        bean.setY( -(int)(Math.ceil(mBitmapHeight)));

        float duration=random.nextInt(500)+maxDuration;
        //android系统中每16毫秒系统会发出一次重绘的消息
        bean.setVelocityY((int) (getHeight()*16/duration));
        bean.setVelocityX(Math.round(random.nextFloat()));
        imageRainBean=bean;
    }

    public void stopRain(){
        setVisibility(GONE);
        isRaining=false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //如果图片雨正在运行或bean对象初始化失败，则直接返回
        if (!isRaining || imageRainBean==null){
            return;
        }
        //如果位图已被回收或图片雨的位置已在屏幕下，则直接返回
        if (imageRainBean.getBitmap().isRecycled() || imageRainBean.getY()>getHeight()){
            return;
        }

        //缩放图片，将其宽缩为mBitmapWidth，高缩为mBitmapHeight
        matrix.reset();
        float heightScale=mBitmapHeight/imageRainBean.getBitmap().getHeight();
        float widthScale=mBitmapWidth/imageRainBean.getBitmap().getWidth();
        matrix.setScale(heightScale,widthScale);

        //移动距离为X，Y原来的位置加上随机变化的速率，得到新的X，Y坐标
        imageRainBean.setX(imageRainBean.getX()+imageRainBean.getVelocityX());
        imageRainBean.setY(imageRainBean.getY()+imageRainBean.getVelocityY());
        //设置新的位置
        matrix.postTranslate(imageRainBean.getX(),imageRainBean.getY());
        //绘制方法
        canvas.drawBitmap(imageRainBean.getBitmap(),matrix,paint);
        //调用该函数进行重绘
        postInvalidate();
    }
}
