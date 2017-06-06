package com.example.agentzengyu.superdownloader.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ZengYu on 2017/6/6.
 */

public class CircleProgressView extends View {
    private int mMaxProgress = 100;
    private int mProgress = 0;
    private final int mCircleLineStrokeWidth = 10;
    private final int mTxtStrokeWidth = 3;

    private final RectF mRectF;
    private final Paint mPaint;
    private final Context mContext;

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        this.mRectF = new RectF();
        this.mPaint = new Paint();
        this.mContext = context;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = this.getWidth();
        int height = this.getHeight();
        int min = Math.min(width, height)-mCircleLineStrokeWidth;

        // 设置画笔相关属性
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.rgb(0xe9, 0xe9, 0xe9));
        canvas.drawColor(Color.TRANSPARENT);
        mPaint.setStrokeWidth(mCircleLineStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        // 位置
        mRectF.left = (width - min) / 2;
        mRectF.top = (height - min) / 2;
        mRectF.right = (min + width) / 2;
        mRectF.bottom = (min + height) / 2;

        // 绘制圆圈，进度条背景
        canvas.drawArc(mRectF, -90, 360, false, mPaint);
        mPaint.setColor(Color.rgb(0xf8, 0x60, 0x30));
        canvas.drawArc(mRectF, -90, ((float) mProgress / mMaxProgress) * 360, false, mPaint);

        mPaint.setStrokeWidth(mTxtStrokeWidth);
        String text = mProgress + "%";
        int textHeight = min / 3;
        mPaint.setTextSize(textHeight);
        int textWidth = (int) mPaint.measureText(text, 0, text.length());
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(text, width / 2 - textWidth / 2, height / 2 + textHeight / 2, mPaint);
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
        this.invalidate();
    }

    public void setProgressNotInUiThread(int progress) {
        this.mProgress = progress;
        this.postInvalidate();
    }
}
