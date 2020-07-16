package com.mobileisaccframework.GameObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.Vector2D;

import java.io.File;
import java.util.ArrayList;

public class GameObject {       // 교수님 코드에서의 GraphicObject (조금 차이 있음)
    // 위치 좌표
    protected Vector2D m_vecPos;
    // 방향
    protected Vector2D m_vecDir;

    // 충돌체 BoundBox 사용
    protected Rect m_boundBox;
    // 이미지 크기
    protected int m_imgWidth;
    protected int m_imgHeight;

    protected GameObjectState m_objectState;

    protected int[] m_arrFrameCnt; // 각 State마다의 이미지 프레임 개수
    protected int m_curState = 0; // 각 오브젝트 상태 (Idle, Attack, ... )

    // 이미지만 넣을 때
    public GameObject(Bitmap bitmap, int _imgWidth, int _imgHeight) {
        m_imgWidth =_imgWidth * 4;
        m_imgHeight = _imgHeight * 4;

        m_vecPos = new Vector2D(0,0);
        m_vecDir = new Vector2D(0, 0);

        // No Animation
        m_objectState = new GameObjectState(this, bitmap, m_imgWidth, m_imgHeight, 1, 1, false);

        Initialize();
    }

    // 이미지 + 위치
    public GameObject(Bitmap bitmap, int _imgWidth, int _imgHeight, int _posX, int _posY) {
        m_imgWidth =_imgWidth * 4;
        m_imgHeight = _imgHeight * 4;

        m_vecPos = new Vector2D(_posX,_posY);
        m_vecDir = new Vector2D(0, 0);

        // No Animation
        m_objectState = new GameObjectState(this, bitmap, m_imgWidth, m_imgHeight,1, 1, false);

        Initialize();
    }

    // 애니메이션
    public GameObject(Bitmap bitmap, int _imgWidth, int _imgHeight, int _fps, int _frameCnt, boolean _isLoop) {
        m_imgWidth = (_imgWidth / _frameCnt) * 4;
        m_imgHeight = _imgHeight * 4;

        m_vecPos = new Vector2D(0,0);
        m_vecDir = new Vector2D(0, 0);

        // Animation 있을 때
        m_objectState = new GameObjectState(this, bitmap, m_imgWidth, m_imgHeight,_fps, _frameCnt, _isLoop);

        Initialize();
    }

    // 애니메이션 + 위치
    public GameObject(Bitmap bitmap, int _imgWidth, int _imgHeight, int _posX, int _posY, int _fps, int _frameCnt, boolean _isLoop) {
        m_imgWidth = (_imgWidth / _frameCnt) * 4;
        m_imgHeight = _imgHeight * 4;

        m_vecPos = new Vector2D(_posX,_posY);
        m_vecDir = new Vector2D(0, 0);

        // Animation 있을 때
        m_objectState = new GameObjectState(this, bitmap, m_imgWidth, m_imgHeight, _fps, _frameCnt, _isLoop);

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

        // bondBox 위치 업데이트
        m_boundBox.set(m_vecPos.x, m_vecPos.y, m_vecPos.x + m_imgWidth, m_vecPos.y + m_imgHeight);
    }

    // 매 프레임 실행
    public void Update(long _gameTime) {
        // GameObjectState 업데이트
        if(m_objectState != null)
         m_objectState.Update(_gameTime);

        // bondBox 위치 업데이트
        m_boundBox.set(m_vecPos.x, m_vecPos.y, m_vecPos.x + m_imgWidth, m_vecPos.y + m_imgHeight);
    }

    // 이미지 출력
    public void Render(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);

        if(AppManager.getInstance().m_bRenderRect)
            canvas.drawRect(m_boundBox, paint);
        if(m_objectState != null)
            m_objectState.Render(canvas);
    }

    public void ChangeState(int _state) {
        // GameObjectState 변경하는 부분(플레이어 쪽 참고해서 코딩해주시면 됩니다)
    }

    // 충돌 시 호출 되는 함수. 인자인 obj는 자신과 충돌한 오브젝트임!
    public void OnCollision(GameObject obj, int objID) {

    }
}