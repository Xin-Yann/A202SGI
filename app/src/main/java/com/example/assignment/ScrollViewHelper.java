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
        maxScrollY = dpToPx(200); // Set the maximum scroll position to 200dp

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

                        // Invert the scroll delta to reverse the scrolling direction
                        deltaY *= -1;

                        // Limit the scroll delta to prevent exceeding the maximum scroll position
                        if ((scrollY + deltaY) > maxScrollY) {
                            deltaY = maxScrollY - scrollY;
                        }

                        // Scroll the ScrollView by the limited deltaY
                        v.scrollBy(0, (int) deltaY);

                        lastTouchY = currentTouchY;
                        break;
                    case MotionEvent.ACTION_UP:
                        lastTouchY = 0;
                        break;
                }

                return true; // Consume the event to prevent further scrolling
            }
        });
    }

    private int dpToPx(int dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density);
    }
}
