package com.rinish.factorfactor.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.RelativeLayout;

public class CountDownView extends RelativeLayout {

    private Path colorIndicatorPath;
    private Path timeIndicatorForegroundPath;
    private Path timeIndicatorBackgroundPath;
    private Paint colorIndicatorPaint;
    private Paint timeIndicatorForegroundPaint;
    private Paint timeIndicatorBackgroundPaint;
    private RectF ovalRect;
    private RectF ovalRect1;
    private Rect topFillerRect;
    private Rect innerOvalArcBounds;
    private float angle = 0;
    private float hue = 0;

    public CountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (timeIndicatorForegroundPath != null) {
            canvas.drawPath(timeIndicatorForegroundPath, timeIndicatorForegroundPaint);
            canvas.drawRect(innerOvalArcBounds, timeIndicatorForegroundPaint);
        }

        if (timeIndicatorBackgroundPath != null) {
            timeIndicatorBackgroundPath.reset();
            timeIndicatorBackgroundPath.arcTo(ovalRect1, 360, -angle, false);
            canvas.drawPath(timeIndicatorBackgroundPath, timeIndicatorBackgroundPaint);
        }

        if (colorIndicatorPath != null) {
            colorIndicatorPath.reset();
            colorIndicatorPaint.setColor(Color.HSVToColor(new float[]{hue, 100, 100}));
            colorIndicatorPath.arcTo(ovalRect, 180, 180, true);
            colorIndicatorPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);
            canvas.drawPath(colorIndicatorPath, colorIndicatorPaint);
        }

        canvas.drawRect(topFillerRect, colorIndicatorPaint);
        super.dispatchDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        colorIndicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        colorIndicatorPaint.setStyle(Paint.Style.FILL);
        colorIndicatorPaint.setColor(Color.HSVToColor(new float[]{hue, 255, 255}));
        colorIndicatorPath = new Path();

        timeIndicatorForegroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        timeIndicatorForegroundPaint.setStyle(Paint.Style.FILL);
        timeIndicatorForegroundPaint.setColor(0xff000000);
        timeIndicatorForegroundPath = new Path();

        timeIndicatorBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        timeIndicatorBackgroundPaint.setStyle(Paint.Style.STROKE);
        timeIndicatorBackgroundPaint.setStrokeWidth(px(22f));
        timeIndicatorBackgroundPaint.setColor(0xffffffff);
        timeIndicatorBackgroundPath = new Path();

        float horizontalOffset = px(10f);
        float top = h - h * .7f;
        float bottom = h + h * .7f;

        topFillerRect = new Rect(0, 0, w, (int) top);
        innerOvalArcBounds = new Rect(0, (int) px(10f), w, (int) (top + px(10f)));

        RectF outerOvalArcBounds = new RectF(
                -horizontalOffset,
                top + px(10f),
                w + horizontalOffset,
                bottom + px(10f));

        timeIndicatorForegroundPath.arcTo(outerOvalArcBounds, 180, 180, true);
        timeIndicatorForegroundPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);

        ovalRect = new RectF(-horizontalOffset, top, w + horizontalOffset, bottom);
        colorIndicatorPath.arcTo(ovalRect, 180, 180, true);
        colorIndicatorPath.setFillType(Path.FillType.INVERSE_EVEN_ODD);


        ovalRect1 = new RectF(-horizontalOffset, top, w + horizontalOffset, bottom);
        timeIndicatorBackgroundPath.arcTo(ovalRect1, 360, -angle);

    }

    private float px(float dp) {
        return dp * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public void setHue(float hue) {
        this.hue = hue;
        invalidate();
    }

    public void setAngle(float angle) {
        this.angle = angle;
        invalidate();
    }
}
