package com.example.myapplication.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

public class TabLayoutIndictor2 extends TabLayout {
    private Paint mPaint;
    private Path mPath;
    private int mTriangleWidth;
    private int mTriangleHigh;
    private static final float BILI=1/6F;
    private int initTranslationX;
    private int mTranslationX;
    public TabLayoutIndictor2(Context context) {
        super(context);
    }

    public TabLayoutIndictor2(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化画笔
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#000000"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(initTranslationX+mTranslationX,getHeight());
        //绘制图形，mPath：形状，mPaint：画笔
        canvas.drawPath(mPath,mPaint);

        canvas.restore();
        super.dispatchDraw(canvas);
    }

    /*
        需要根据控件的宽高的设置可以在该方法中进行设置
         */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTriangleWidth= (int) (w/3*BILI);
        initTranslationX=w/3/2-mTriangleWidth/2;
        initTrangle();
    }

    //初始化三角形
    private void initTrangle() {

        mTriangleHigh=mTriangleWidth/2;

        //使用path去构建图形形状，以手机屏幕的左上角为（0，0）点，向右为X正，向下为Y正
        mPath=new Path();
        mPath.moveTo(0,0);
        //lineTo方法将X,Y轴上的点连线
        mPath.lineTo(mTriangleWidth,0);
        mPath.lineTo(mTriangleWidth/2,-mTriangleHigh);
        //close方法回电原点形成闭合图形
        mPath.close();
    }

    /**
     * 指示器跟随fragment进行滚动
     * @param position
     * @param Offset
     */
    private void scroll(int position, float Offset) {
        int tabWidth =getWidth()/3;
        //确定偏移量
        mTranslationX= (int) (tabWidth*(Offset+position));

        //进行重绘的方法
        invalidate();
    }
    public void setViewPager2(ViewPager mVpMain) {
        mVpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scroll(position,positionOffset);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
