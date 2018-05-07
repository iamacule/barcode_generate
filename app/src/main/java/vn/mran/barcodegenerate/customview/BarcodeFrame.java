package vn.mran.barcodegenerate.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

/**
 * Created by Mr An on 20/09/2017.
 */

public class BarcodeFrame extends View {

    private final String TAG = getClass().getSimpleName();
    private Bitmap bpBarcode;
    private Bitmap bpLogo;
    private String number = "1234567";
    private Paint pNumber;
    private Paint pLine;

    public BarcodeFrame(Context contex) {
        super(contex);
        init();
    }

    private void init() {
        bpBarcode = Bitmap.createBitmap(472, 75, Bitmap.Config.ARGB_8888);
        bpLogo = Bitmap.createBitmap(472, 56, Bitmap.Config.ARGB_8888);
        pNumber = new Paint();
        pNumber.setColor(Color.BLACK);
        pNumber.setTextSize(50);
        pNumber.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));

        pLine = new Paint();
        pLine.setColor(Color.BLACK);
        pLine.setStyle(Paint.Style.STROKE);
    }

    public BarcodeFrame(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect rectLogo = new Rect(0, 0, canvas.getWidth(), canvas.getHeight() * 3 / 10);
        canvas.drawBitmap(bpLogo, null, rectLogo, null);

        Rect rectBarcode = new Rect(0, canvas.getHeight() * 3 / 10, canvas.getWidth(), canvas.getHeight() * 7 / 10);
        canvas.drawBitmap(bpBarcode, null, rectBarcode, null);
//
        int xPos = (int) ((canvas.getWidth() / 2) - (pNumber.measureText(number) / 2));
        int yPos = (int) ((canvas.getHeight() * 8.5 / 10) - ((pNumber.descent() + pNumber.ascent()) / 2));
        canvas.drawText(number, xPos, yPos, pNumber);

        Rect r = new Rect(1,1,canvas.getWidth()-1,canvas.getHeight()-1);
        canvas.drawRect(r,pLine);
    }

    public void setData(String number, Bitmap barcodeImage) {
        this.number = number;
        this.bpBarcode = barcodeImage;
        invalidate();
    }

    public void setBpLogo(Bitmap bpLogo) {
        this.bpLogo = bpLogo;
        invalidate();
    }

    private Bitmap flipAndRotate(Bitmap src) {
        Matrix m = new Matrix();
        m.postRotate(-90);
        m.preScale(1, -1);
        Bitmap dst = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), m, false);
        dst.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        Log.d(TAG, "flipAndRotate: w = "+dst.getWidth());
        Log.d(TAG, "flipAndRotate: h = "+dst.getHeight());
        return dst;
    }
}
