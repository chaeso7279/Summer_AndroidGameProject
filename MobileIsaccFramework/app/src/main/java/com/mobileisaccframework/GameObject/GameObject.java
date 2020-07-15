package com.mobileisaccframework.GameObject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.mobileisaccframework.Vector2D;

public class GameObject {
    protected Bitmap m_bitmap;

    // 위치 좌표
    protected Vector2D m_vecPos;

    // 방향
    protected Vector2D m_vecDir;

    // 충돌체 BondBox 사용
    protected Rect m_bondBox;

    public GameObject(Bitmap bitmap) {
        m_bitmap = bitmap;
        m_vecPos = new Vector2D(0,0);
        m_vecDir = new Vector2D(0, 0);
    }

    public GameObject(Bitmap bitmap, int _posX, int _posY) {
        m_bitmap = bitmap;
        m_vecPos = new Vector2D(_posX,_posY);
        m_vecDir = new Vector2D(0, 0);
    }

    public void setPosition(int _x, int _y) { m_vecPos.x = _x; m_vecPos.y = _y; }
    public void setDirection(int _dirX, int _dirY) { m_vecDir.x = _dirX; m_vecDir.y = _dirY; }
    public void setBondBox(Rect _rect) { m_bondBox = _rect; }

    public Vector2D getPos() { return m_vecPos; }
    public Vector2D getDirection() { return m_vecDir; }
    public Rect getBoundBox() { return m_bondBox; }

    // 초기 데이터 설정
    public void Initialize() {

    }

    // 매 프레임 실행
    public void Update(long _gameTime) {

    }

    // 이미지 출력
    public void Render(Canvas canvas) {
        canvas.drawBitmap(m_bitmap, m_vecPos.x, m_vecPos.y, null);
    }
}