package com.mobileisaccframework.GameObject.bullet;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.GameObject.GameObjectState;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.R;
import com.mobileisaccframework.State.GameState;
import com.mobileisaccframework.Vector2D;

public class Bomb extends GameObject {
    private static final int TIME_EXPLOSION = 3000; // 폭탄이 터지기까지 걸릴 시간
    private long m_explosionTimer = System.currentTimeMillis(); // 폭탄 터질 타이밍 재기 위한 타이머

    private int m_gap = 30 * 4;

    public Bomb(int _posX, int _posY) {
        Bitmap bitmap = AppManager.getInstance().getBitmap(R.drawable.bomb);

        m_imgWidth =  AppManager.getInstance().getBitmapWidth(R.drawable.bomb) * 4;
        m_imgHeight =  AppManager.getInstance().getBitmapHeight(R.drawable.bomb) * 4;

        m_vecPos = new Vector2D(_posX,_posY);

        // No Animation
        m_objectState = new GameObjectState(this, bitmap, m_imgWidth, m_imgHeight,1, 1, false);

        Initialize();
    }

    @Override
    public void Initialize() {
        // 이미지 크기보다 충돌박스의 크기 크게 해서 오브젝트들이 폭탄과 충돌하도록 함
        m_boundBox = new Rect();
        m_boundBox.set(m_vecPos.x - 80, m_vecPos.y - 80,
                m_vecPos.x + 160 , m_vecPos.y + 160);
    }

    @Override
    public int Update(long _gameTime) {
        if(m_isDead) {
            // 파괴 될 때 파괴이펙트 생성하고 삭제
            GameObject obj = new BombEffect(m_vecPos.x - 140, m_vecPos.y - 250);

            // 스테이지에 추가 해줌
            if(obj != null)
                AppManager.getInstance().getCurGameState().
                        m_lstObject[GameState.OBJ_EFFECT].add(obj);

            return DEAD_OBJ;
        }

        // GameObjectState 업데이트
        if(m_objectState != null)
            m_objectState.Update(_gameTime);

        // 터지는 시간 다되면 객체 죽음
        if(_gameTime > m_explosionTimer + TIME_EXPLOSION) {
            m_isDead = true;
        }

        return NO_EVENT;
    }
}
