package shm.dim.dailybudget.PieChart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

import java.io.File;

import shm.dim.dailybudget.FileManager;
import shm.dim.dailybudget.MainActivity;

public class PieChartView extends View {

    private Paint paint;
    private static int[] colors = new int[15];
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static int getColor(int index,  File file) {
        for(int i = 0; i < 14; i++) {
            String color = FileManager.readFile(file, i);
            colors[i] = Color.parseColor(color);
        }
        return colors[index];
    }
}