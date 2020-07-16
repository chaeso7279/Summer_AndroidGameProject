package com.mobileisaccframework.GameObject.MapObject;

import android.graphics.Bitmap;

import com.mobileisaccframework.GameObject.GameObject;

public class FireObject extends GameObject {
    public static final int FIRE_ON = 0;
    public static final int FIRE_OFF = 1;
    public int m_FireHp = 3;
    public int m_firestate = FIRE_ON;


    public FireObject(Bitmap bitmap, int _imgWidth, int _imgHeight) {
        super(bitmap, _imgWidth, _imgHeight);
    }

    public void Update(){
        if(m_FireHp<=0) m_firestate = FIRE_OFF;
    }
}
