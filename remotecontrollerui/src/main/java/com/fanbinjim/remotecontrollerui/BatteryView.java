package com.fanbinjim.remotecontrollerui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class BatteryView extends View {

    private int mPower = 0;
    private int width = 0;
    private int height = 0;
    private int power_color;
    private int mBatteryStatus;

    public BatteryView(Context context) {
        super(context);
        init();
    }

    public BatteryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        initAttr(context, attrs);
    }

    public BatteryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initAttr(context, attrs);
    }

    private void init() {
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        power_color = Color.GREEN;
    }
    private void initAttr(Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BatteryView);
        mPower = typedArray.getInteger(R.styleable.BatteryView_battery_power, 0);
        power_color = typedArray.getColor(R.styleable.BatteryView_battery_power_color, Color.GREEN);
        mBatteryStatus = typedArray.getInteger(R.styleable.BatteryView_battery_status, 0);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBatteryPower(canvas);
    }
    private void drawBatteryPower(Canvas canvas) {
        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        // 先画电池外部
        float strokeWidth = (width - getPaddingLeft() - getPaddingRight()) / 20.f;
        float halfWidth = strokeWidth/2;
        float radius = halfWidth * 3;
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(strokeWidth);
        RectF rectf = new RectF(getPaddingLeft() + halfWidth,
                getPaddingTop() + halfWidth,
                width - strokeWidth - halfWidth - getPaddingRight(),
                height - halfWidth - getPaddingBottom());
        canvas.drawRoundRect(rectf, radius, radius, mPaint);

        mPaint.setStrokeWidth(strokeWidth * 2);
        canvas.drawLine(width - getPaddingRight() - strokeWidth,
                getPaddingTop() + strokeWidth * 3,
                width - getPaddingRight() - strokeWidth,
                height - getPaddingBottom() - strokeWidth * 3, mPaint);

        // 画电池里面
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.SQUARE);
        mPaint.setColor(power_color);
        mPaint.setStrokeWidth(strokeWidth);
        float top = getPaddingTop() + strokeWidth * 2f;
        float botton = height - getPaddingBottom() - strokeWidth * 2f;
        float start = getPaddingLeft() + strokeWidth * 2f;
        float end = width - getPaddingRight() - 3f * strokeWidth;
        float oneWidth = (end - start + strokeWidth * 0.75f) / 5;
        float size = oneWidth - strokeWidth  * 0.75f;

        int block = mPower / 20;

        for (int i = 0; i < block; i++) {
            rectf = new RectF(start + oneWidth * i, top, start + oneWidth * i + size, botton);
            canvas.drawRect(rectf, mPaint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    public int getBatteryStatus() {
        return mBatteryStatus;
    }

    public void setBatteryStatus(@BatteryStatus int mBatteryStatus) {
        this.mBatteryStatus = mBatteryStatus;
    }

    // 定义状态注解
    @Retention(RetentionPolicy.SOURCE)
    public @interface BatteryStatus {
        int normal = 0;
        int charging = 1;
        int battery_error = -1;
        int charger_error = -2;
    }

    public int getPower() {
        return mPower;
    }

    public void setPower(int mPower) {
        if (mPower > 100 || mPower < 0) return;
        this.mPower = mPower;
        invalidate();
    }
}
