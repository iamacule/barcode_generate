package vn.mran.barcodegenerate.utils;

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

public class TouchEffect {

    private static float TOUCH_ALPHA = (float) 0.5;

    private static Rect rect;

    public static void addAlpha(View view) {

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setAlpha(TOUCH_ALPHA);
                    rect = new Rect(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setAlpha(1);
                    if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                        // Contact moved inside bounds
                        v.performClick();
                    }
                } else {
                    if (rect.contains(v.getLeft() + (int) event.getX(), v.getTop() + (int) event.getY())) {
                        // Contact moved inside bounds
                        v.setAlpha(TOUCH_ALPHA);
                    } else {
                        // Contact moved outside bounds
                        v.setAlpha(1);
                    }
                }
                return true;
            }
        });
    }
}
