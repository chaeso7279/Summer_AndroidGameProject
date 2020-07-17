package com.mobileisaccframework.GameObject.MapObject;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.Manager.AppManager;

public class BlockObject extends GameObject {

    public static final int BLOCK_START = 0;
    public static final int BlOCK_ON = 1;
    public static final int BLOCK_OFF = 2;
    public static final int BLOCK_END = 3;
    public int blockstate;

    public Rect m_BoundBox = new Rect();


    // 이미지만 넣을 때
    public BlockObject(Bitmap bitmap, int _imgWidth, int _imgHeight){
        super(bitmap, _imgWidth, _imgHeight);
    }

    // 이미지 + 위치 넣을 때
    public BlockObject(Bitmap bitmap, int _imgWidth, int _imgHeight, int _posX, int _posY){
        super(bitmap, _imgWidth, _imgHeight, _posX, _posY);
    }

    @Override
    public void Initialize(){
        super.Initialize();

        //초기 state 설정
        m_curState = BlOCK_ON;
    }

    @Override
    public void ChangeState(int _state){


    }

    //충돌 시 호출되는 함수. 인자인 obj는 자신과 충돌한 오브젝트임!
    @Override
    public void OnCollision(GameObject obj, int objID){

    }


}
