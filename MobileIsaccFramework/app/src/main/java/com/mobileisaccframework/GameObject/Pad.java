package com.mobileisaccframework.GameObject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.R;
import com.mobileisaccframework.Vector2D;

public class Pad {
    static final int DIR_UP = 0;
    static final int DIR_DOWN = 1;
    static final int DIR_LEFT = 2;
    static final int DIR_RIGHT = 3;
    static final int DIR_END = 4;

    // 위치 좌표
    protected Vector2D m_vecPos;
    // 충돌 렉트 4개
    Rect m_rect[];
    // 비트맵
    Bitmap m_bitmap;

    int m_iWidth;
    int m_iHeight;

    public Pad(int posX, int posY) {
        m_vecPos = new Vector2D(posX, posY);

        Initialize();
    }

    public void Initialize() {
        m_rect = new Rect[DIR_END];
        for(int i = 0; i < DIR_END; ++i)
            m_rect[i] = new Rect();

        m_bitmap = AppManager.getInstance().getBitmap(R.drawable.ui_direction);
        m_iWidth = AppManager.getInstance().getBitmapWidth(R.drawable.ui_direction);
        m_iHeight = AppManager.getInstance().getBitmapHeight(R.drawable.ui_direction);

        m_rect[DIR_UP].set(m_vecPos.x + (33 * 4), m_vecPos.y ,
                m_vecPos.x + (66 * 4), m_vecPos.y + (33 * 4));

        m_rect[DIR_DOWN].set(m_vecPos.x + (33 * 4), m_vecPos.y + (67 * 4),
                m_vecPos.x + (66 * 4), m_vecPos.y + (101 * 4));

        m_rect[DIR_LEFT].set(m_vecPos.x, m_vecPos.y + (34 * 4),
                m_vecPos.x + (34 * 4), m_vecPos.y + (67 * 4));

        m_rect[DIR_RIGHT].set(m_vecPos.x + (67 * 4), m_vecPos.y + (34 * 4),
                m_vecPos.x + (101 * 4), m_vecPos.y + (67 * 4));
    }

    public void Render(Canvas canvas) {
        if(canvas == null)
            return;

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);

        for(int i = 0; i < DIR_END; ++i)
            canvas.drawRect(m_rect[i], paint);
        canvas.drawBitmap(m_bitmap, m_vecPos.x, m_vecPos.y, null);
    }

    public void OnTouchEvent(MotionEvent Event) {

    }
}
