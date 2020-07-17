package com.mobileisaccframework;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.mobileisaccframework.GameObject.player.Player;
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
    protected Vector2D m_vecCenter;

    // 충돌 렉트 4개
    Rect m_rect[];
    // 비트맵
    Bitmap m_bitmap;

    int m_iWidth;
    int m_iHeight;

    public Pad(int posX, int posY) {
        m_vecPos = new Vector2D(posX, posY);
        m_vecCenter = new Vector2D();

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

        m_vecCenter.x = m_vecPos.x + (m_iWidth / 2);
        m_vecCenter.y = m_vecPos.y + (m_iHeight / 2);
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

    public void OnTouchEvent(MotionEvent _event) {
        if(m_rect[0] == null) // 배열로 생성이라 첫번쨰것만 검사
            return;

        int action = _event.getAction();

        // 싱글 터치
        int x = (int)_event.getX();
        int y = (int)_event.getY();

        Vector2D vecDir = new Vector2D(0, 0);

        // 터치 할때
        if(_event.getAction() == KeyEvent.ACTION_DOWN) {
            // 위
            if (m_rect[DIR_UP].contains(x, y)) {
                if (m_rect[DIR_LEFT].contains(x, y)) {
                    // 위 왼
                    vecDir = vecDir.getDirection(new Vector2D(-1, 1));
                }
                else if(m_rect[DIR_RIGHT].contains(x, y)) {
                    // 위 오른
                    vecDir = vecDir.getDirection(new Vector2D(1, 1));
                }
                else {
                    // 위
                    vecDir = vecDir.getDirection(new Vector2D(0, -1));
                }
                AppManager.getInstance().m_player.Move(vecDir, Player.WALK_BACK);
                return;
            }

            // 아래
            if (m_rect[DIR_DOWN].contains(x, y)) {
                if (m_rect[DIR_LEFT].contains(x, y)) {
                    // 아래 왼
                    vecDir = vecDir.getDirection(new Vector2D(-1, -1));
                }
                else if(m_rect[DIR_RIGHT].contains(x, y)) {
                    // 아래 오른
                    vecDir = vecDir.getDirection(new Vector2D(1, -1));
                }
                else {
                    // 아래
                    vecDir = vecDir.getDirection(new Vector2D(0, 1));
                }
                AppManager.getInstance().m_player.Move(vecDir, Player.WALK_FRONT);
                return;
            }

            // 왼
            if(m_rect[DIR_LEFT].contains(x, y)) {
                vecDir = vecDir.getDirection(new Vector2D(-1, 0));
                AppManager.getInstance().m_player.Move(vecDir, Player.WALK_LEFT);
                return;
            }

            if(m_rect[DIR_RIGHT].contains(x, y)) {
                vecDir = vecDir.getDirection(new Vector2D(1, 0));
                AppManager.getInstance().m_player.Move(vecDir, Player.WALK_RIGHT);
                return;
            }
        }

        // 터치에서 손을 뗄 때
        else if(_event.getAction() == KeyEvent.ACTION_UP) {
            AppManager.getInstance().m_player.MoveStop();
        }
    }
}
