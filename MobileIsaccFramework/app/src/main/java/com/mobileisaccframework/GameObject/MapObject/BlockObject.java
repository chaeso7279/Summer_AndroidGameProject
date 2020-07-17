package com.mobileisaccframework.GameObject.MapObject;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.GameObject.GameObjectState;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.R;

import static com.mobileisaccframework.State.GameState.OBJ_BULLET_PLAYER;
import static com.mobileisaccframework.State.GameState.OBJ_PLAYER;

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
        if(m_curState==_state)
            return;

        if(m_objectState != null){
            m_objectState.Destroy();
        }

        int rID = 0;

        switch(_state){
            case BlOCK_ON:
                rID = R.drawable.rocks_basement;
                break;
            case BLOCK_OFF:
                System.out.println("블럭소멸");
                break;
        }

        //AppManager로부터 비트맵 가져옴
        Bitmap bitmap = AppManager.getInstance().getBitmap(rID);

        //이미지 크기
        int width = AppManager.getInstance().getBitmapWidth(rID)*4;
        int height = AppManager.getInstance().getBitmapHeight(rID)*4;

        //오브젝트 스테이지 지정
     //   m_objectState = new GameObjectState(this, bitmap, width, height);
      //  m_objectState.Initialize();

        m_curState=_state;



    }

    //충돌 시 호출되는 함수. 인자인 obj는 자신과 충돌한 오브젝트임!
    @Override
    public void OnCollision(GameObject obj, int objID) {
        // 플레이어랑 충돌
        if (objID == OBJ_PLAYER) {
            //플레이어 못 지나가게
        }
        //폭탄이랑 충돌
        else if (objID == OBJ_BULLET_PLAYER) {
            m_curState = BLOCK_OFF;
        }
    }


}
