package com.mobileisaccframework.GameObject.MapObject;


import android.graphics.Bitmap;

import com.mobileisaccframework.GameObject.GameObject;

public class MapObject extends GameObject {
    // 맵 오브젝트 타입을 나눌 것임
    public static final int MAP_FIRE = 0;
    public static final int MAP_BLOCK = 1;
    protected int m_type;

    // 애니메이션 X
    public MapObject(int iType, Bitmap bitmap, int _imgWidth, int _imgHeight, int _posX, int _posY){
        super(bitmap, _imgWidth, _imgHeight, _posX, _posY);
        m_type = iType;
    }

    // 애니메이션 O
    public MapObject(int iType, Bitmap bitmap, int _imgWidth, int _imgHeight, int _posX, int _posY, int _fps, int _frameCnt, boolean _isLoop) {
        super(bitmap, _imgWidth, _imgHeight, _posX, _posY, _fps, _frameCnt, _isLoop);
        m_type = iType;
    }

    public int GetMapObjectType() { return m_type; }
}