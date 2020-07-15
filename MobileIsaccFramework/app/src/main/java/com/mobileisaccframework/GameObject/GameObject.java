package com.mobileisaccframework.GameObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.mobileisaccframework.Vector2D;

import java.io.File;
import java.util.ArrayList;

public class GameObject {
    protected Bitmap m_bitmap;

    // 위치 좌표
    protected Vector2D m_vecPos;
    // 방향
    protected Vector2D m_vecDir;

    // 충돌체 BoundBox 사용
    protected Rect m_boundBox;
    // 이미지 크기
    protected int m_imgWidth;
    protected int m_imgHeight;

    // 각 오브젝트 상태 (Idle, Attack, ... )
    protected int m_state = 0;

    public GameObject(Bitmap bitmap) {
        m_bitmap = bitmap;
        m_vecPos = new Vector2D(0,0);
        m_vecDir = new Vector2D(0, 0);

        Initialize();
    }

    public GameObject(Bitmap bitmap, int _posX, int _posY) {
        m_bitmap = bitmap;
        m_vecPos = new Vector2D(_posX,_posY);
        m_vecDir = new Vector2D(0, 0);

        Initialize();
    }

    // Setter
    public void setPosition(int _x, int _y) { m_vecPos.x = _x; m_vecPos.y = _y; }
    public void setDirection(int _dirX, int _dirY) { m_vecDir.x = _dirX; m_vecDir.y = _dirY; }
    public void setBondBox(Rect _rect) { m_boundBox = _rect; }

    // Getter
    public Vector2D getPos() { return m_vecPos; }
    public Vector2D getDirection() { return m_vecDir; }
    public Rect getBoundBox() { return m_boundBox; }

    // 초기 데이터 설정
    public void Initialize() {
        m_boundBox = new Rect();

        m_imgWidth = m_bitmap.getWidth() * 3;
        m_imgHeight = m_bitmap.getHeight() * 3;

        // bondBox 위치 업데이트
        m_boundBox.set(m_vecPos.x, m_vecPos.y, m_vecPos.x + m_imgWidth, m_vecPos.y + m_imgHeight);
    }

    // 매 프레임 실행
    public void Update(long _gameTime) {
        // bondBox 위치 업데이트
        m_boundBox.set(m_vecPos.x, m_vecPos.y, m_vecPos.x + m_imgWidth, m_vecPos.y + m_imgHeight);
    }

    // 이미지 출력
    public void Render(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);

        canvas.drawRect(m_boundBox, paint);
        canvas.drawBitmap(m_bitmap, m_vecPos.x, m_vecPos.y, null);
    }
}