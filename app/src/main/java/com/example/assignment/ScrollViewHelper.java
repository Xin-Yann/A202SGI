package com.example.assignment;

import android.content.Context;
import android.view.View;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class ScrollViewHelper {
    private Context context;
    private float lastTouchY = 0;
    private boolean scrollingDown = false;
    private int maxScrollY;

    public void setOnTouchListener(ScrollView scrollView, Context context) {
        this.context = context;
        maxScrollY = dpToPx(200);

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float currentTouchY = event.getY();
                int scrollY = v.getScrollY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastTouchY = currentTouchY;
                        scrollingDown = scrollY > maxScrollY;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float deltaY = currentTouchY - lastTouchY;

                        deltaY *= -1;

                        if ((scrollY + deltaY) > maxScrollY) {
                            deltaY = maxScrollY - scrollY;
                        }

                        v.scrollBy(0, (int) deltaY);

                        lastTouchY = currentTouchY;
                        break;
                    case MotionEvent.ACTION_UP:
                        lastTouchY = 0;
                        break;
                }

                return true;
            }
        });
    }

    private int dpToPx(int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
}
