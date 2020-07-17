package com.mobileisaccframework.GameObject.bullet;

import android.graphics.Bitmap;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.GameObject.GameObjectState;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.R;
import com.mobileisaccframework.Vector2D;

public class BulletEffect extends GameObject {
    public BulletEffect(boolean _isPlayer, int _posX, int _posY){
        Bitmap bitmap;
        if(_isPlayer)
            bitmap = AppManager.getInstance().getBitmap(R.drawable.effect_bullet_player);
        else
            bitmap = AppManager.getInstance().getBitmap(R.drawable.effect_bullet_enemy);

        m_imgWidth =  (AppManager.getInstance().getBitmapWidth(R.drawable.effect_bullet_player) * 4) / 15;
        m_imgHeight =  AppManager.getInstance().getBitmapWidth(R.drawable.effect_bullet_player) * 4;

        m_vecPos = new Vector2D(_posX,_posY);

        // Animation
        m_objectState = new GameObjectState(this, bitmap, m_imgWidth, m_imgHeight,20, 15, false);

        Initialize();
    }

    @Override
    public int Update(long _gameTime) {
        if(m_objectState.IsPlay() == false) // 애니메이션 재생이 멈췄을 경우
            m_isDead = true; // 객체 삭제

        return super.Update(_gameTime);
    }
}
