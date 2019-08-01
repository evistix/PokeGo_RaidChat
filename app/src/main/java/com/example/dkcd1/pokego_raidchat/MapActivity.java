package com.example.dkcd1.pokego_raidchat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class MapActivity extends AppCompatActivity {

    ScrollView scroll_view;

    // float values to allow for a swipe action
    float xSwipe, ySwipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        scroll_view = findViewById(R.id.scroll_view);
        scroll_view.setOnTouchListener(new MapActivity.OnSwipeTouchListener(MapActivity.this));
    }

    // Detects right to left swipes across the scroll view and switches to MessageActivity
    public class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener(Context context) {
            gestureDetector = new GestureDetector(context, new MapActivity.OnSwipeTouchListener.GestureListener());
        }

        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_DISTANCE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                xSwipe = e2.getX() - e1.getX();
                ySwipe = e2.getY() - e1.getY();
                if (Math.abs(xSwipe) > Math.abs(ySwipe) && Math.abs(xSwipe) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (xSwipe < 0)
                    {
                        Intent i = new Intent(MapActivity.this, MessageActivity.class);
                        startActivity(i);
                    }
                    return true;
                }
                return false;
            }
        }
    }
}
