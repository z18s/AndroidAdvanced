package com.example.weatherapp.ui.sensors;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Observable;
import java.util.Observer;

public class SensorView extends View implements Observer {

    private SensorValues sensorValues;

    private Paint paint;

    public SensorView(Context context) {
        super(context);
        init(context);
    }

    public SensorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SensorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SensorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        initValues((SensorValuesHolder) context);
        initPaint();
    }

    private void initValues(SensorValuesHolder sensorValuesHolder) {
        sensorValues = sensorValuesHolder.getSensorValues();
        sensorValues.addObserver(this);
    }

    private void initPaint() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextSize(48.0f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(sensorValues.getTempText(), 10, 100, paint);
        canvas.drawText(sensorValues.getHumText(), 10, 200, paint);
    }

    @Override
    public void update(Observable observable, Object o) {
        postInvalidate();
    }
}