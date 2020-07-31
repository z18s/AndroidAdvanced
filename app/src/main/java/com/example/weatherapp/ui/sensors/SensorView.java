package com.example.weatherapp.ui.sensors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.weatherapp.R;

import java.util.Observable;
import java.util.Observer;

public class SensorView extends View implements Observer {

    private SensorValues sensorValues;

    private final float HUM_VALUE_MIN = 0.0f;
    private final float HUM_VALUE_MAX = 100.0f;
    private final float TEMP_VALUE_MIN = -273.1f;
    private final float TEMP_VALUE_MAX = 100.0f;

    private Paint paintText;
    private Paint paintBackground;
    private Paint paintBorder;
    private Paint paintGraphTempPlus;
    private Paint paintGraphTempMinus;
    private Paint paintGraphHumidity;

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
        initPaints();
    }

    private void initValues(SensorValuesHolder sensorValuesHolder) {
        sensorValues = sensorValuesHolder.getSensorValues();
        sensorValues.addObserver(this);
    }

    @SuppressLint("ResourceAsColor")
    private void initPaints() {
        paintText = new Paint();
        paintText.setColor(Color.BLACK);
        paintText.setStyle(Paint.Style.FILL);
        paintText.setTextSize(48.0f);

        paintBackground = new Paint();
        paintBackground.setColor(getResources().getColor(R.color.colorPaintBackground));
        paintBackground.setStyle(Paint.Style.FILL);

        paintBorder = new Paint();
        paintBorder.setColor(getResources().getColor(R.color.colorPaintBorder));
        paintBorder.setStyle(Paint.Style.STROKE);

        paintGraphTempPlus = new Paint();
        paintGraphTempPlus.setColor(getResources().getColor(R.color.colorPaintTempPlus));
        paintGraphTempPlus.setStyle(Paint.Style.FILL);

        paintGraphTempMinus = new Paint();
        paintGraphTempMinus.setColor(getResources().getColor(R.color.colorPaintTempMinus));
        paintGraphTempMinus.setStyle(Paint.Style.FILL);

        paintGraphHumidity = new Paint();
        paintGraphHumidity.setColor(getResources().getColor(R.color.colorPaintHumidity));
        paintGraphHumidity.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Temperature
        canvas.drawText(sensorValues.getTempText(), 10, 100, paintText);

        float grLeft = 600.0f;
        float grTop = 20.0f;
        float grRight = 700.0f;
        float grBottom = 120.0f;

        drawGraphBackground(canvas, grLeft, grTop, grRight, grBottom);

        float tempValDegree;
        if (sensorValues.getTempVal() >= 0) {
            tempValDegree = -sensorValues.getTempVal() * 360 / (TEMP_VALUE_MAX - 0);
            drawGraphMain(canvas, grLeft, grTop, grRight, grBottom, tempValDegree, paintGraphTempPlus);
        } else {
            tempValDegree = -sensorValues.getTempVal() * 360 / (0 - TEMP_VALUE_MIN);
            drawGraphMain(canvas, grLeft, grTop, grRight, grBottom, tempValDegree, paintGraphTempMinus);
        }

        // Humidity
        canvas.drawText(sensorValues.getHumText(), 10, 200, paintText);

        grLeft = 600.0f;
        grTop = 130.0f;
        grRight = 700.0f;
        grBottom = 230.0f;

        drawGraphBackground(canvas, grLeft, grTop, grRight, grBottom);

        float humValDegree = -sensorValues.getHumVal() * 360 / (HUM_VALUE_MAX - HUM_VALUE_MIN);
        drawGraphMain(canvas, grLeft, grTop, grRight, grBottom, humValDegree, paintGraphHumidity);
    }

    private void drawGraphBackground(Canvas canvas, float left, float top, float right, float bottom) {
        canvas.drawArc(left, top, right, bottom, 0, 360, true, paintBackground);
        canvas.drawArc(left, top, right, bottom, 0, 360, true, paintBorder);
    }

    private void drawGraphMain(Canvas canvas, float left, float top, float right, float bottom, float angle, Paint paint) {
        canvas.drawArc(left+7, top+7, right-7, bottom-7, 270, angle, true, paint);
    }

    @Override
    public void update(Observable observable, Object o) {
        postInvalidate();
    }
}