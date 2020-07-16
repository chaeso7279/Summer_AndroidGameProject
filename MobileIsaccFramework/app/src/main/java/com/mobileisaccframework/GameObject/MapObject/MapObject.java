package com.mobileisaccframework.GameObject.MapObject;


import android.graphics.Bitmap;

import com.mobileisaccframework.GameObject.GameObject;

public class MapObject extends GameObject {

    BlockObject m_block;
    FireObject m_fire;

    public MapObject(Bitmap bitmap, int _imgWidth, int _imgHeight) {
        super(bitmap, _imgWidth, _imgHeight);
    }


}
