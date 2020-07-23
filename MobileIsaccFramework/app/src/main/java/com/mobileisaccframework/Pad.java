package com.mobileisaccframework;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.mobileisaccframework.GameObject.player.Player;
import com.mobileisaccframework.Manager.AppManager;

public class Pad {
    static final int DIR_UP = 0;
    static final int DIR_DOWN = 1;
    static final int DIR_LEFT = 2;
    static final int DIR_RIGHT = 3;
    static final int DIR_END = 4;

    static final int ATT_BULLET = 0;
    static final int ATT_BOMB = 1;
    static final int ATT_END = 2;

    // 위치 좌표
    protected Vector2D m_vecPos;
    protected Vector2D m_vecCenter;

    // 충돌 렉트 4개(화살표 충돌 박스)
    Rect m_rectArrow[];
    Rect m_rectAttack[];

    // 비트맵
    Bitmap m_bitmap;

    int m_iWidth;
    int m_iHeight;

    public Pad(int posX, int posY) {
        m_vecPos = new Vector2D(posX, posY);
        m_vecCenter = new Vector2D();

        Initialize();
    }

    public void SetAttackUIRect(int iType, Rect rect) {
        m_rectAttack[iType] = rect;
    }

    public void Initialize() {
        m_rectArrow = new Rect[DIR_END];
        for(int i = 0; i < DIR_END; ++i)
            m_rectArrow[i] = new Rect();

        m_rectAttack = new Rect[ATT_END];

        m_bitmap = AppManager.getInstance().getBitmap(R.drawable.ui_direction);
        m_iWidth = AppManager.getInstance().getBitmapWidth(R.drawable.ui_direction);
        m_iHeight = AppManager.getInstance().getBitmapHeight(R.drawable.ui_direction);

        // 각 Rect의 영역 설정
        m_rectArrow[DIR_UP].set(m_vecPos.x + (25 * 4), m_vecPos.y - (20 * 4),
                m_vecPos.x + (75 * 4), m_vecPos.y + (33 * 4));

        m_rectArrow[DIR_DOWN].set(m_vecPos.x + (25 * 4), m_vecPos.y + (67 * 4),
                m_vecPos.x + (75 * 4), m_vecPos.y + (121 * 4));

        m_rectArrow[DIR_LEFT].set(m_vecPos.x - (20 * 4), m_vecPos.y + (25 * 4),
                m_vecPos.x + (34 * 4), m_vecPos.y + (75 * 4));

        m_rectArrow[DIR_RIGHT].set(m_vecPos.x + (67 * 4), m_vecPos.y + (25 * 4),
                m_vecPos.x + (121 * 4), m_vecPos.y + (75 * 4));

        m_vecCenter.x = m_vecPos.x + (m_iWidth / 2);
        m_vecCenter.y = m_vecPos.y + (m_iHeight / 2);
    }

    public void Render(Canvas canvas) {
        if(canvas == null)
            return;

        canvas.drawBitmap(m_bitmap, m_vecPos.x, m_vecPos.y, null);
    }

    public void OnTouchEvent(MotionEvent _event) {
        if(m_rectArrow[0] == null) // 배열로 생성이라 첫번쨰것만 검사
            return;

        // 싱글 터치
        if(_event.getPointerCount() <= 1) {
            int x = (int)_event.getX();
            int y = (int)_event.getY();

            SingleTouch(x, y, _event);
        }

        // 멀티 터치
        else {
            int x1 = (int)_event.getX(0);
            int y1 = (int)_event.getY(0);

            int x2 = (int)_event.getX(1);
            int y2 = (int)_event.getY(1);

            MultiTouch(x1, y1, x2, y2, _event);
        }
    }

    private void SingleTouch(int x, int y, MotionEvent _event) {
        Vector2D vecDir = new Vector2D(0, 0);

        // 터치 할때(한번 터치 or 터치하고 움직임)
        if(_event.getActionMasked() == MotionEvent.ACTION_DOWN ||
                _event.getActionMasked() == MotionEvent.ACTION_MOVE) {

            if(_event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                // 총알 발사 시
                if(m_rectAttack[ATT_BULLET].contains(x, y)) {
                    ((Player)AppManager.getInstance().m_player).Attack(ATT_BULLET);
                    return;
                }

                // 총알 발사 시
                if(m_rectAttack[ATT_BOMB].contains(x, y)) {
                    ((Player)AppManager.getInstance().m_player).Attack(ATT_BOMB);
                    return;
                }
            }

            // 위
            if (m_rectArrow[DIR_UP].contains(x, y)) {
                vecDir = vecDir.getDirection(new Vector2D(0, -1));
                ((Player)AppManager.getInstance().m_player).Move(vecDir, Player.WALK_BACK);
                return;
            }

            // 아래
            if (m_rectArrow[DIR_DOWN].contains(x, y)) {
                vecDir = vecDir.getDirection(new Vector2D(0, 1));
                ((Player)AppManager.getInstance().m_player).Move(vecDir, Player.WALK_FRONT);
                return;
            }

            // 왼
            if(m_rectArrow[DIR_LEFT].contains(x, y)) {
                vecDir = vecDir.getDirection(new Vector2D(-1, 0));
                ((Player)AppManager.getInstance().m_player).Move(vecDir, Player.WALK_LEFT);
                return;
            }

            // 오
            if(m_rectArrow[DIR_RIGHT].contains(x, y)) {
                vecDir = vecDir.getDirection(new Vector2D(1, 0));
                ((Player)AppManager.getInstance().m_player).Move(vecDir, Player.WALK_RIGHT);
                return;
            }
        }

        // 터치에서 손을 뗄 때
        else if(_event.getActionMasked() == MotionEvent.ACTION_UP) {
            // 공격에서 손을 뗀 것이 아닐때(이동에서 손뗏을때만 적용됨)
            if(!m_rectAttack[ATT_BULLET].contains(x, y) && !m_rectAttack[ATT_BOMB].contains(x, y))
                ((Player)AppManager.getInstance().m_player).MoveStop();
        }
    }

    void MultiTouch(int x1, int y1, int x2, int y2, MotionEvent _event) {
        Vector2D vecDir = new Vector2D(0, 0);

        if(_event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN){
            // 총알 발사 시
            if(m_rectAttack[ATT_BULLET].contains(x1, y1) || m_rectAttack[ATT_BULLET].contains(x2, y2)) {
                ((Player)AppManager.getInstance().m_player).Attack(ATT_BULLET);
            }

            // 총알 발사 시
            if(m_rectAttack[ATT_BOMB].contains(x1, y1) || m_rectAttack[ATT_BOMB].contains(x2, y2)) {
                ((Player)AppManager.getInstance().m_player).Attack(ATT_BOMB);
            }
        }

        // 터치 할때(한번 터치 or 터치하고 움직임)
        if(_event.getActionMasked() == MotionEvent.ACTION_DOWN ||
                _event.getActionMasked() == MotionEvent.ACTION_MOVE) {
            // 위
            if (m_rectArrow[DIR_UP].contains(x1, y1) || m_rectArrow[DIR_UP].contains(x2, y2)) {
                vecDir = vecDir.getDirection(new Vector2D(0, -1));
                ((Player)AppManager.getInstance().m_player).Move(vecDir, Player.WALK_BACK);
                return;
            }

            // 아래
            if (m_rectArrow[DIR_DOWN].contains(x1, y1) || m_rectArrow[DIR_DOWN].contains(x2, y2)) {
                vecDir = vecDir.getDirection(new Vector2D(0, 1));
                ((Player)AppManager.getInstance().m_player).Move(vecDir, Player.WALK_FRONT);
                return;
            }

            // 왼
            if(m_rectArrow[DIR_LEFT].contains(x1, y1) || m_rectArrow[DIR_LEFT].contains(x2, y2)) {
                vecDir = vecDir.getDirection(new Vector2D(-1, 0));
                ((Player)AppManager.getInstance().m_player).Move(vecDir, Player.WALK_LEFT);
                return;
            }

            // 오
            if(m_rectArrow[DIR_RIGHT].contains(x1, y1) || m_rectArrow[DIR_RIGHT].contains(x2, y2)) {
                vecDir = vecDir.getDirection(new Vector2D(1, 0));
                ((Player)AppManager.getInstance().m_player).Move(vecDir, Player.WALK_RIGHT);
                return;
            }
        }

        // 터치에서 손을 뗄 때
        else if(_event.getActionMasked() == MotionEvent.ACTION_UP) {
            // 공격에서 손을 뗀 것이 아닐때(이동에서 손뗏을때만 적용됨)
            if(!m_rectAttack[ATT_BULLET].contains(x1, y1) && !m_rectAttack[ATT_BULLET].contains(x2, y2)
                    && !m_rectAttack[ATT_BOMB].contains(x1, y1) && !m_rectAttack[ATT_BOMB].contains(x2, y2))
                ((Player)AppManager.getInstance().m_player).MoveStop();
        }
    }
}
