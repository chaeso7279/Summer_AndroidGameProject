package com.mobileisaccframework.GameObject.bullet;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.GameObject.GameObjectState;
import com.mobileisaccframework.GameObject.effect.BulletEffect;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.Manager.EFFECT_ENUM;
import com.mobileisaccframework.Manager.SoundManager;
import com.mobileisaccframework.R;
import com.mobileisaccframework.State.GameState;
import com.mobileisaccframework.Vector2D;

public class Bullet extends GameObject {
    int m_gapX = 25 * 4;
    int m_gapY = 23 * 4;

    int m_speed = 20;

    //Player: true, Enemy: false
    boolean m_isPlayer = true;

    public Bullet(boolean _isPlayer, int _posX, int _posY, Vector2D _vecDir) {
        Bitmap bitmap;

        // 플레이어와 적 구분해서 이미지 변경
        if(_isPlayer)
            bitmap = AppManager.getInstance().getBitmap(R.drawable.bullet_player);
        else
            bitmap = AppManager.getInstance().getBitmap(R.drawable.bullet_enemy);

        m_imgWidth =  AppManager.getInstance().getBitmapWidth(R.drawable.bullet_player) * 4;
        m_imgHeight =  AppManager.getInstance().getBitmapHeight(R.drawable.bullet_player) * 4;

        m_isPlayer = _isPlayer;
        m_vecPos = new Vector2D(_posX,_posY);
        m_vecDir = _vecDir;

        // No Animation
        m_objectState = new GameObjectState(this, bitmap, m_imgWidth, m_imgHeight,1, 1, false);

        Initialize();
    }

    @Override
    public void Initialize() {
        // 이미지 크기와 충돌박스의 크기에 차이를 둘 것임(이미지가 너무 큼)
        m_gapX = 25 * 4;
        m_gapY = 23 * 4;

        m_boundBox = new Rect();
        // 15 -> 실제 Bullet 의 Width, Height
        m_boundBox.set(m_vecPos.x + m_gapX, m_vecPos.y + m_gapY,
                m_vecPos.x + m_gapX + (15 * 4), m_vecPos.y + m_gapY + (15 * 4));
    }

    @Override
    public int Update(long _gameTime) {
        if(m_isDead) {
            // 파괴 될 때 파괴이펙트 생성하고 삭제
            GameObject obj;
            if(m_isPlayer)
                obj = new BulletEffect(true, m_vecPos.x, m_vecPos.y);
            else
                obj = new BulletEffect(false, m_vecPos.x, m_vecPos.y);
            // 스테이지에 추가 해줌
            if(obj != null)
                AppManager.getInstance().getCurGameState().
                        m_lstObject[GameState.OBJ_EFFECT].add(obj);

            // 효과음
            SoundManager.getInstance().PlayEffectSound(EFFECT_ENUM.FX_BULLET_DIE);

            return DEAD_OBJ;
        }

        // GameObjectState 업데이트
        if(m_objectState != null)
            m_objectState.Update(_gameTime);

        // boundBox 위치 업데이트
        m_boundBox.set(m_vecPos.x + m_gapX, m_vecPos.y + m_gapY,
                m_vecPos.x + m_gapX + (15 * 4), m_vecPos.y + m_gapY + (15 * 4));

        m_vecPos.x += m_vecDir.x * m_speed;
        m_vecPos.y += m_vecDir.y * m_speed;

        // 벽 넘어가면 삭제
        if(m_vecPos.x < AppManager.MIN_X || m_vecPos.x > AppManager.MAX_X
                || m_vecPos.y < AppManager.MIN_Y || m_vecPos.y > AppManager.MAX_Y)
            m_isDead = true;

        return NO_EVENT;
    }

    @Override
    public void Render(Canvas canvas) {
        super.Render(canvas);
    }

    @Override
    public void OnCollision(GameObject obj, int objID) {
        // 맵 오브젝트 충돌 시 죽음
        if(objID == GameState.OBJ_MAP)
            m_isDead = true;

        if(m_isPlayer){ // 플레이어 Bullet 일 경우 적과 부딫히면 죽음
            if(objID == GameState.OBJ_ENEMY)
                m_isDead = true;
        }

        else{ // 적 Bullet 일 경우 플레이어와 부딫히면 죽음
            if(objID == GameState.OBJ_PLAYER)
                m_isDead = true;
        }
    }
}