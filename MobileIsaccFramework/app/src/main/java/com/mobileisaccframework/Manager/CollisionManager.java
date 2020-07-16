package com.mobileisaccframework.Manager;

import android.graphics.Rect;

public class CollisionManager {
    public static boolean CheckCollision(Rect src, Rect dst) {
        if(src.intersect(dst))
            return true;
        return false;
    }
}