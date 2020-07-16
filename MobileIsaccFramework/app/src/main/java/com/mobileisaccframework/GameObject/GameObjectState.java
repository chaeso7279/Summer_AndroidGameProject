package com.mobileisaccframework.GameObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.mobileisaccframework.Manager.AppManager;
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
    private int m_curFrame = 0;     // 현재 프레임

    private boolean m_isLoop;   // 반복 재생 애니메이션 유무
    private boolean m_isPlay = true;   // 재생

    public GameObjectState(GameObject _target, Bitmap bitmap, int _imgWidth, int _imgHeight, int _fps, int _frameCnt, boolean _isLoop) {
        m_targetObject = _target;
        m_bitmap = bitmap;

        m_imgWidth = _imgWidth;
        m_imgHeight = _imgHeight;

        m_imgRect = new Rect(0, 0, m_imgWidth, m_imgHeight);

        m_fps = 1000/_fps;
        m_frameCnt = _frameCnt;

        if(m_frameCnt <= 1)
            m_isPlay = false;

        m_isLoop = _isLoop;
    }

    public void Initialize() {

    }

    // 매 프레임 실행
    public void Update(long _gameTime) {
        if(m_targetObject == null)
            return;

        if(m_isPlay){       // 애니메이션 재생 중
            if(_gameTime > m_frameTime + m_fps) {
                m_frameTime = _gameTime;
                ++m_curFrame;

                    if(m_curFrame >= m_frameCnt) {
                        if (m_isLoop)       // 반복재생일 경우 0부터 다시 재생
                            m_curFrame = 0;
                        else {              // 반복 재생 아니면 재생을 멈춤
                            m_isPlay = false;
                            --m_curFrame; // 가장 마지막 프레임으로 돌려둠
                        }
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
        if(canvas == null)
            return;

        int x = m_targetObject.getPos().x;
        int y = m_targetObject.getPos().y;

        Rect dest = new Rect(x, y, x + m_imgWidth, y + m_imgHeight);
        canvas.drawBitmap(m_bitmap, m_imgRect, dest , null);
    }

    public void Destroy() {

    }
}