package com.mobileisaccframework.GameObject.effect;

import android.graphics.Bitmap;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.GameObject.GameObjectState;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.R;
import com.mobileisaccframework.Vector2D;

public class BombEffect extends Effect {
    public BombEffect(int _posX, int _posY){
        Bitmap bitmap;

        bitmap = AppManager.getInstance().getBitmap(R.drawable.effect_bomb);

        m_imgWidth =  (AppManager.getInstance().getBitmapWidth(R.drawable.effect_bomb) * 4) / 12;
        m_imgHeight =  AppManager.getInstance().getBitmapHeight(R.drawable.effect_bomb) * 4;

        m_vecPos = new Vector2D(_posX,_posY);

        // Animation
        m_objectState = new GameObjectState(this, bitmap, m_imgWidth, m_imgHeight,20, 12, false);

        Initialize();
    }
}
