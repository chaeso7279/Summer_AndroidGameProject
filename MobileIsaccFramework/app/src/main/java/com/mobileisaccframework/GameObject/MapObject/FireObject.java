package com.mobileisaccframework.GameObject.MapObject;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.GameObject.GameObjectState;
import com.mobileisaccframework.GameObject.player.Player;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.R;
import com.mobileisaccframework.State.GameState;

import static com.mobileisaccframework.State.GameState.OBJ_BULLET_PLAYER;
import static com.mobileisaccframework.State.GameState.OBJ_PLAYER;

public class FireObject extends GameObject {
    protected int fire_hp;   // 불꽃 체력(bollet 세 번 맞아야 불이 꺼짐)

    public static final int STATE_START = 0;
    public static final int FIRE_ON = 1;
    public static final int FIRE_OFF = 2;
    public static final int STATE_END = 3;
    public int firestate = FIRE_ON;

    public FireObject(Bitmap bitmap, int _imgWidth, int _imgHeight, int _fps, int _frameCnt, boolean _isLoop) {
        super(bitmap, _imgWidth, _imgHeight, _fps, _frameCnt, _isLoop);
    }

    public FireObject(Bitmap bitmap, int _imgWidth, int _imgHeight, int _posX, int _posY, int _fps, int _frameCnt, boolean _isLoop) {
        super(bitmap, _imgWidth, _imgHeight, _posX, _posY, _fps, _frameCnt, _isLoop);
    }


    @Override
    public void Initialize(){
        super.Initialize();

        //초기 state 설정
        m_curState = FIRE_ON;

        //프레임 개수 설정
        m_arrFrameCnt = new int[STATE_END];

        //모두 5
        for(int i=STATE_START; i<STATE_END;i++) {
            m_arrFrameCnt[i] = 6;
        }
    }

    @Override
    public void Update(long _gameTime){
        super.Update(_gameTime);
    }

    @Override
    public void ChangeState(int _state){
        if(m_curState==_state)
            return;

        if(m_objectState != null){
            m_objectState.Destroy();
        }

        //바꿀 수 있으면 fire사이즈 바꾸기
        //fire hp가 0이되면 fire state를 off로 바꾸기
        if(fire_hp<1)
            firestate=FIRE_OFF;

        int rID = 0;
        int fps = 0;
        boolean isLoop = true;

        switch (_state){
            case FIRE_ON:
                rID = R.drawable.effect_fire;
                fps = 5;
                break;
            case FIRE_OFF:
                System.out.println("파이어 소멸");
                break;
        }

        //AppManager 로부터 비트맵 가져옴
        Bitmap bitmap = AppManager.getInstance().getBitmap(rID);

        //이미지크기
        int width = (AppManager.getInstance().getBitmapWidth(rID)*4)/m_arrFrameCnt[_state];
        int height = AppManager.getInstance().getBitmapHeight(rID)*4;

        //오브젝트 스테이지 지정
        m_objectState = new GameObjectState(this, bitmap, width, height, fps, m_arrFrameCnt[_state], isLoop);
        m_objectState.Initialize();

        //오브젝트 스테이트를 숫자로 쓰는 용도
        m_curState=_state;

    }

    // 충돌 시 호출 되는 함수. 인자인 obj는 자신과 충돌한 오브젝트임!
    public void OnCollision(GameObject obj, int objID) {
        // 플레이어랑 충돌
        if(objID== OBJ_PLAYER){
        }

        //플레이어 불릿이랑 충돌
        else if(objID == OBJ_BULLET_PLAYER) {
            if (fire_hp > 1)
                --fire_hp;
            else {
                firestate = FIRE_OFF;
            }
        }
    }

}
