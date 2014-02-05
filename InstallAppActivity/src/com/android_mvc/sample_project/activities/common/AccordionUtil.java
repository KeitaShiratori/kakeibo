package com.android_mvc.sample_project.activities.common;

import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class AccordionUtil {

    private LinearLayout _btn;
    private LinearLayout _content;
    private Handler _handler;
    private float _height;
    private float current = 0.0f;
    private Thread _thread;
    private String _bound = "close";
    private int _startTime;
    private DecelerateInterpolator mInterpolator = new DecelerateInterpolator();
    private int easeTime = 400;

    public AccordionUtil(LinearLayout btn, LinearLayout content) {
        _btn = btn;
        _content = content;
        _handler = new Handler();
        _height = _content.getHeight();
        mInterpolator = new DecelerateInterpolator();
        _content.setLayoutParams(new LinearLayout.LayoutParams(
                LayoutParams.FILL_PARENT, 0));
        _btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                _startTime = (int) System.currentTimeMillis();
                if (_bound.equals("open")) {
                    _bound = "close";
                } else {
                    _bound = "open";
                }
                if (_thread == null || !_thread.isAlive()) {
                    _thread = null;
                    makeThread();
                    _thread.start();
                }

            }
        });

    }

    private void makeThread() {
        _thread = new Thread(new Runnable() {
            public void run() {
                while (easeTime > (int) System.currentTimeMillis() - _startTime) {
                    int diff = (int) System.currentTimeMillis() - _startTime;
                    if (_bound.equals("open")) {
                        current = _height * mInterpolator.getInterpolation((float) diff / (float) easeTime);
                    } else {
                        current = _height - _height * mInterpolator.getInterpolation((float) diff / (float) easeTime);
                    }
                    threadFunc();
                }
            }
        });
    }

    private void threadFunc() {
        _handler.post(new Runnable() {
            public void run() {
                _content.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT, (int) current));
            }
        });
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
        }
    }

    public void deleteAccordion() {
        _btn.setOnClickListener(null);
        _btn = null;
        _content = null;
    }
}
