package com.example.xiongpeng.view04;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by xiongpeng on 2017/9/9.
 */

public class CustomVolumeControlBar extends View {

    private int mFirstColor;
    private  int mSecondColor;
    private int mCircleWidth;
    private Paint mPaint;
    private int mCurrentCount = 3;
    private Bitmap mImage;
    private int mSplitSize;
    private int mCount;
    private Rect mRect;


    public CustomVolumeControlBar(Context context) {
        this(context, null);
    }

    public CustomVolumeControlBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomVolumeControlBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomVolumeControlBar, defStyleAttr, 0);
        int n = a.getIndexCount();
        for(int i = 0; i < n; i++){
            int attr = a.getIndex(i);
            switch (attr){
                case R.styleable.CustomVolumeControlBar_firstColor:
                    mFirstColor = a.getColor(attr, Color.GREEN);
                break;
                case R.styleable.CustomVolumeControlBar_secondColor:
                    mSecondColor = a.getColor(attr, Color.CYAN);
                break;
                case R.styleable.CustomVolumeControlBar_circleWidth:
                    mCircleWidth = a.getDimensionPixelOffset(attr, 20);
                break;
                case R.styleable.CustomVolumeControlBar_dotCount:
                    mCount = a.getInt(attr, 20);
                break;
                case R.styleable.CustomVolumeControlBar_splitSize:
                    mSplitSize = a.getInt(attr, 20);
                break;
                case R.styleable.CustomVolumeControlBar_bg:
                    mImage = BitmapFactory.decodeResource(getResources(), a.getResourceId(attr, 0));
                    break;
            }

        }
        a.recycle();
        mPaint = new Paint();
        mRect = new Rect();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(mCircleWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        int center = getWidth() / 2;
        int radious = center - mCircleWidth / 2;

        drawOval(canvas, center, radious);

        int relRadius = radious - mCircleWidth / 2;
        mRect.left = (int)(radious - Math.sqrt(2) * 1.0f / 2 * relRadius) + mCircleWidth;
        mRect.top = (int)(relRadius  - Math.sqrt(2) * 1.0f / 2 * relRadius) + mCircleWidth;
        mRect.bottom = (int)(mRect.left + Math.sqrt(2)*relRadius);
        mRect.right = (int)(mRect.left + Math.sqrt(2)*relRadius);

        if(mImage.getWidth() < Math.sqrt(2) * relRadius){
            mRect.left = (int)(mRect.left + Math.sqrt(2)*relRadius* 1.0f / 2 - mImage.getWidth() * 1.0f  / 2);
            mRect.top = (int) (mRect.top + Math.sqrt(2) * relRadius * 1.0f / 2 - mImage.getHeight() * 1.0f / 2);;
            mRect.right = (int)(mRect.left + mImage.getWidth());
            mRect.bottom = (int)(mRect.top + mImage.getHeight());
        }
        canvas.drawBitmap(mImage, null, mRect, mPaint);
    }

    private void drawOval(Canvas canvas, int center, int radious) {
        float itemSize = (360 * 1.0f - mCount * mSplitSize) / mCount;
        RectF oval = new RectF(center - radious, center - radious, center + radious, center + radious);
        mPaint.setColor(mFirstColor);
        for(int i = 0; i < mCount; i++){
            canvas.drawArc(oval, i *(itemSize + mSplitSize),  itemSize, false, mPaint);
        }

        mPaint.setColor(mSecondColor);
        for(int i = 0; i < mCurrentCount; i++){
            canvas.drawArc(oval, i * (itemSize + mSplitSize), itemSize, false ,mPaint);
        }
    }


    public void up(){
        mCurrentCount++;
        postInvalidate();
    }

    public void down(){
        mCurrentCount--;
        postInvalidate();
    }

    private int yDown, yUp;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                yDown = (int)event.getY();
                break;
            case MotionEvent.ACTION_UP:
                yUp = (int) event.getY();

                if(yDown < yUp){
                    down();
                }else{
                    up();
                }
                break;
        }
        return true;
    }
}
