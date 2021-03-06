package com.example.cookshare;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class OnDoubleTapListener implements View.OnTouchListener {

    private GestureDetector gestureDetector;

    public OnDoubleTapListener(Context c) {
        gestureDetector = new GestureDetector(c, new GestureListener());
    }

    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);

    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            OnDoubleTapListener.this.onDoubleTap(e);
            Log.i("DoubleTapClass", " " + super.onDoubleTap(e));
            return super.onDoubleTap(e);

        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            OnDoubleTapListener.this.onSingleTapConfirmed(e);
            Log.i("SingleTapConfirmedClass", " " + super.onSingleTapConfirmed(e));
            return super.onSingleTapConfirmed(e);

        }


    }

    public void onDoubleTap(MotionEvent e) {
        // To be overridden when implementing listener
    }

    public void onSingleTapConfirmed(MotionEvent e) {
        // To be overridden when implementing listener
    }
}