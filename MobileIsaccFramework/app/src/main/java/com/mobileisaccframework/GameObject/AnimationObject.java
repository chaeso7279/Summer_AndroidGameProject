package com.mobileisaccframework.GameObject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class AnimationObject extends GameObject {
    private int m_fps;          // 초당 프레임
    private int m_frameCnt;     // 프레임 개수(iFrames)
    private long m_frameTime;
    private int m_curFrame;     // 현재 프레임

    protected Rect m_imgRect; // 이미지에서 그려줄 사각 영역

    public AnimationObject(Bitmap bitmap) {
        super(bitmap);
    }

    public AnimationObject(Bitmap bitmap, int _posX, int _posY) {
        super(bitmap, _posX, _posY);
    }

    public void InitSpriteData(int _fps, int _frameCnt) {
        m_boundBox = new Rect();

        m_imgWidth = (m_imgWidth / _frameCnt) * 3;


        // bondBox 위치 업데이트
        m_boundBox.set(m_vecPos.x, m_vecPos.y, m_vecPos.x + m_imgWidth, m_vecPos.y + m_imgHeight);
    }

    @Override
    // 매 프레임 실행
    public void Update(long _gameTime) {
        // bondBox 위치 업데이트
        m_boundBox.set(m_vecPos.x, m_vecPos.y, m_vecPos.x + m_imgWidth, m_vecPos.y + m_imgHeight);
    }

    @Override
    // 이미지 출력
    public void Render(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);

        canvas.drawRect(m_boundBox, paint);
        canvas.drawBitmap(m_bitmap, m_vecPos.x, m_vecPos.y, null);
    }
}
