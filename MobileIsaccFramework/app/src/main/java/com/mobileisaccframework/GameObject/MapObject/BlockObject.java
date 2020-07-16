package com.mobileisaccframework.GameObject.MapObject;

import android.graphics.Bitmap;

import com.mobileisaccframework.GameObject.GameObject;

public class BlockObject extends GameObject {

    public static final int BLOCK_ON = 0;
    public static final int BLOCK_OFF = 1;

    public int m_firestate = BLOCK_ON;


    public BlockObject(Bitmap bitmap, int _imgWidth, int _imgHeight) {
        super(bitmap, _imgWidth, _imgHeight);
    }

    public void Update(){

    }
}
