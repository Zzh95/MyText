package com.example.myapplication.bean;

import android.graphics.Bitmap;

public class ImageRainBean {
    /**
     * 需要绘制的Bitmap（也就是图片）
     */
    private Bitmap bitmap;
    /**
     * 初始的的X，Y轴的坐标
     */
    private int x;
    private int y;

    /**
     * X,Y轴的下落速率
     */
    private int velocityX;
    private int velocityY;

    private int appearTime;

    //图标开始下落的时间
    public int getAppearTime() {
        return appearTime;
    }

    public void setAppearTime(int appearTime) {
        this.appearTime = appearTime;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(int velocityX) {
        this.velocityX = velocityX;
    }

    public int getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(int velocityY) {
        this.velocityY = velocityY;
    }
}
