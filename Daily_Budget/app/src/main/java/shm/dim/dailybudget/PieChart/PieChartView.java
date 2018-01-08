package shm.dim.dailybudget.PieChart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class PieChartView extends View {

    private Paint paint;
    private static int[] colors = {
            Color.rgb(255, 255, 0), Color.rgb(0, 255, 0), Color.rgb(128, 128, 0),
            Color.rgb(0, 128, 0), Color.rgb(0, 128, 128), Color.rgb(0, 0, 128),
            Color.rgb(0, 0, 255), Color.rgb(0, 255, 255), Color.rgb(255, 0, 255),
            Color.rgb(128, 0, 128), Color.rgb(255, 0, 0), Color.rgb(128, 0, 0),
            Color.rgb(0, 0 ,0), Color.rgb(128, 128, 128), Color.rgb(192, 192, 192)};
    private float[] dataPoints;
    private int width;


    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.FILL);
    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        if (this.dataPoints != null) {
            RectF rectf = new RectF(0, 0, getWidth(), getWidth());

            float[] scaledValues = scale();
            float sliceStartPoint = 0;
            for (int i = 0; i < scaledValues.length; i++) {
                paint.setColor(colors[i]);
                canvas.drawArc(rectf, sliceStartPoint, scaledValues[i], true, paint);
                sliceStartPoint += scaledValues[i];
            }
            paint.setColor(Color.WHITE);
            rectf = new RectF(width, width, getWidth() - width, getWidth() - width);
            canvas.drawArc(rectf, 0, 360, true, paint);
        }
    }

    public void setDataPoints(float[] datapoints) {
        this.dataPoints = datapoints;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    private float[] scale() {
        float[] scaledValues = new float[this.dataPoints.length];
        float total = getTotal();
        for (int i = 0; i < this.dataPoints.length; i++) {
            scaledValues[i] = (this.dataPoints[i] / total) * 360;
        }
        return scaledValues;
    }

    private float getTotal() {
        float total = 0;
        for (float val : this.dataPoints)
            total += val;
        return total;
    }

    public static int getColor(int index) {
        return colors[index];
    }
}