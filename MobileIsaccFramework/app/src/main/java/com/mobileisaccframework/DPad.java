package com.mobileisaccframework;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.mobileisaccframework.Manager.AppManager;

public class DPad {
    public static final int STICK_NONE = 0;
    public static final int STICK_UP = 1;
    public static final int STICK_UPRIGHT = 2;
    public static final int STICK_RIGHT = 3;
    public static final int STICK_DOWNRIGHT = 4;
    public static final int STICK_DOWN = 5;
    public static final int STICK_DOWNLEFT = 6;
    public static final int STICK_LEFT = 7;
    public static final int STICK_UPLEFT = 8;

    int OFFSET = 0;
    int m_imgWidth;
    int m_imgHeight;

    int m_posX = 0;
    int m_posY = 0;
    int m_minDist = 0;

    float m_distance = 0;
    float m_angle = 0;

    Bitmap m_bitmap;

    boolean touch_state = false;

    public DPad() {
        m_bitmap = AppManager.getInstance().getBitmap(R.drawable.circle);
        m_imgWidth = AppManager.getInstance().getBitmapWidth(R.drawable.circle);
        m_imgHeight = AppManager.getInstance().getBitmapHeight(R.drawable.circle);
    }

    public void onTouchEvent(MotionEvent event) {
        m_posX = (int) event.getX();
        m_posY = (int) event.getY();
        m_distance = (float) Math.sqrt(Math.pow(m_posX, 2) + Math.pow(m_posY, 2));
        m_angle = (float)CalcAngle(m_posX, m_posY);

        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if(m_distance <= -OFFSET) {
                m_posX = (int)event.getX();

                touch_state = true;
            }

        }
    }

    private double CalcAngle(float x, float y) {
        if(x >= 0 && y >= 0)
            return Math.toDegrees(Math.atan(y / x));
        else if(x < 0 && y >= 0)
            return Math.toDegrees(Math.atan(y / x)) + 180;
        else if(x < 0 && y < 0)
            return Math.toDegrees(Math.atan(y / x)) + 180;
        else if(x >= 0 && y < 0)
            return Math.toDegrees(Math.atan(y / x)) + 360;
        return 0;
    }
}
