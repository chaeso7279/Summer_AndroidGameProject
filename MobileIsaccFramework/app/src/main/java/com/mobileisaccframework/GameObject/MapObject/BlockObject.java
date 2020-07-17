package com.mobileisaccframework.GameObject.MapObject;


import android.graphics.Bitmap;

import com.mobileisaccframework.GameObject.GameObject;
import static com.mobileisaccframework.State.GameState.OBJ_BOMB_PLAYER;

public class BlockObject extends MapObject {
    public BlockObject(int iType, Bitmap bitmap, int _imgWidth, int _imgHeight, int _posX, int _posY){
        super(iType, bitmap, _imgWidth, _imgHeight, _posX, _posY);
    }

    @Override
    public void Initialize(){
        super.Initialize();
    }

//    @Override
//    public void ChangeState(int _state){
//        if(m_curState==_state)
//            return;
//
//        if(m_objectState != null){
//            m_objectState.Destroy();
//        }
//
//        int rID = 0;
//
//        switch(_state){
//            case BlOCK_ON:
//                rID = R.drawable.rocks_basement;
//                break;
//            case BLOCK_OFF:
//                System.out.println("블럭소멸");
//                break;
//        }
//
//        //AppManager로부터 비트맵 가져옴
//        Bitmap bitmap = AppManager.getInstance().getBitmap(rID);
//
//        //이미지 크기
//        int width = AppManager.getInstance().getBitmapWidth(rID)*4;
//        int height = AppManager.getInstance().getBitmapHeight(rID)*4;
//
//        //오브젝트 스테이지 지정
//     //   m_objectState = new GameObjectState(this, bitmap, width, height);
//      //  m_objectState.Initialize();
//
//        m_curState=_state;
//
//
//
//    }

    //충돌 시 호출되는 함수. 인자인 obj는 자신과 충돌한 오브젝트임!
    @Override
    public void OnCollision(GameObject obj, int objID) {
       if (objID == OBJ_BOMB_PLAYER) // 플레이어 폭탄과 충돌 시 죽음
            m_isDead = true;
    }
}
