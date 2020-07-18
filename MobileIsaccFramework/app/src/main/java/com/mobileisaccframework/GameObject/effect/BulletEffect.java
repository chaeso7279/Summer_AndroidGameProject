package com.mobileisaccframework.GameObject.effect;

import android.graphics.Bitmap;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.GameObject.GameObjectState;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.R;
import com.mobileisaccframework.Vector2D;

public class BulletEffect extends Effect {
    public BulletEffect(boolean _isPlayer, int _posX, int _posY){
        Bitmap bitmap;
        // 플레이어와 적 구분해서 이미지 변경
        if(_isPlayer)
            bitmap = AppManager.getInstance().getBitmap(R.drawable.effect_bullet_player);
        else
            bitmap = AppManager.getInstance().getBitmap(R.drawable.effect_bullet_enemy);

        m_imgWidth =  (AppManager.getInstance().getBitmapWidth(R.drawable.effect_bullet_player) * 4) / 15;
        m_imgHeight =  AppManager.getInstance().getBitmapHeight(R.drawable.effect_bullet_player) * 4;

        m_vecPos = new Vector2D(_posX,_posY);

        // Animation
        m_objectState = new GameObjectState(this, bitmap, m_imgWidth, m_imgHeight,20, 15, false);

        Initialize();
    }
}
