package com.mobileisaccframework.GameObject.enemy;

import android.graphics.Bitmap;

import com.mobileisaccframework.GameObject.GameObject;

public abstract class Enemy extends GameObject {
    protected int m_hp;

    public Enemy(Bitmap bitmap, int _imgWidth, int _imgHeight, int _posX, int _posY, int _fps, int _frameCnt, boolean _isLoop){
        super(bitmap, _imgWidth, _imgHeight, _posX, _posY, _fps, _frameCnt, _isLoop);
    }

    protected abstract void Move();
    protected abstract void Attack();
    protected abstract void CreateDieEffect();
}
