package com.mobileisaccframework.GameObject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.mobileisaccframework.R;

public class GameObjectState {
    private GameObject m_targetObject;    // State를 생성한 오브젝트

    private Rect m_imgRect;     // 이미지에서 그려줄 사각 영역
    private Bitmap m_bitmap;

    // 이미지 크기
    protected int m_imgWidth;
    protected int m_imgHeight;

    private long m_frameTime;
    private int m_fps;          // 초당 프레임
    private int m_frameCnt;     // 프레임 개수(iFrames)
    private int m_curFrame;     // 현재 프레임

    private boolean m_isLoop;   // 반복 재생 애니메이션 유무
    private boolean m_isPlay = true;   // 재생

    public GameObjectState(GameObject _target, Bitmap bitmap, int _fps, int _frameCnt, boolean _isLoop) {
        m_targetObject = _target;
        m_bitmap = bitmap;
        m_imgWidth = (m_bitmap.getWidth() / _frameCnt) * 3;
        m_imgHeight = (m_bitmap.getHeight()) * 3;

        m_imgRect = new Rect(0, 0, m_imgWidth, m_imgHeight);

        m_fps = 1000/_fps;
        m_frameCnt = _frameCnt;

        m_isLoop = _isLoop;
    }

    // 매 프레임 실행
    public void Update(long _gameTime) {
        if(m_targetObject == null)
            return;

        if(m_isPlay){
            if(_gameTime > m_frameTime + m_fps) {
                m_frameTime = _gameTime;
                m_curFrame += 1;

                    if(m_curFrame >= m_frameCnt) {
                        if (m_isLoop)
                            m_curFrame = 0;
                        else
                            m_isPlay = false;
                    }
            }
        }

        m_imgRect.left = m_curFrame * m_imgWidth;
        m_imgRect.right = m_imgRect.left + m_imgWidth;
    }

    // 이미지 출력
    public void Render(Canvas canvas) {
        if(m_targetObject == null)
            return;
        int x = m_targetObject.getPos().x;
        int y = m_targetObject.getPos().y;

        Rect dest = new Rect(x, y, x + m_imgWidth, y + m_imgHeight);
        canvas.drawBitmap(m_bitmap, m_imgRect, dest , null);
    }
}