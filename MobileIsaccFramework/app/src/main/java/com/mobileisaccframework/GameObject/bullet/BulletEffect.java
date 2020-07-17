package com.mobileisaccframework.GameObject.bullet;

import android.graphics.Bitmap;

import com.mobileisaccframework.GameObject.GameObject;

public class BulletEffect extends GameObject {
    public BulletEffect(Bitmap bitmap, int _imgWidth, int _imgHeight, int _posX, int _posY, int _fps, int _frameCnt, boolean _isLoop) {
        super(bitmap, _imgWidth, _imgHeight, _posX, _posY, _fps, _frameCnt, _isLoop);
    }
}
