package com.mobileisaccframework.GameObject.door;

import android.graphics.Bitmap;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.GameObject.GameObjectState;
import com.mobileisaccframework.GameView;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.R;
import com.mobileisaccframework.State.GameState;
import com.mobileisaccframework.State.Stage1;
import com.mobileisaccframework.State.Stage2;
import com.mobileisaccframework.State.Stage_Boss;
import com.mobileisaccframework.Vector2D;

public class Door extends GameObject {
    public static int DOOR_RIGHT = 0;
    public static int DOOR_FRONT = 1;

    int m_doorType;
    int m_stageID;

    boolean m_isOpen = false; // 문 열렸는지 여부

    public Door(int iDirType, int iStageID) {
        m_doorType = iDirType;
        m_stageID = iStageID;

        int rID = 0;
        if(iDirType == DOOR_RIGHT){
            m_vecPos = new Vector2D(2160,532);
            rID = R.drawable.golddoor_right;
        }
        else{
            m_vecPos = new Vector2D(1200, 82);
            rID = R.drawable.golddoor_front;
        }

        Bitmap bitmap = AppManager.getInstance().getBitmap(rID);

        m_imgWidth = (AppManager.getInstance().getBitmapWidth(rID) / 2) * 4;
        m_imgHeight = AppManager.getInstance().getBitmapHeight(rID) * 4;


        m_vecDir = new Vector2D(0, 0);

        m_objectState = new GameObjectState(this, bitmap,
                m_imgWidth, m_imgHeight, 1, 2, false);

        // Animation 이 있지만 스테이지에 몬스터가 없을때만 열리게(애니 재생) 할것임
        m_objectState.SetPlay(false);

        Initialize();
    }

    public void SetIsOpen(boolean _isOpen) {
        m_isOpen = _isOpen;
        m_objectState.SetPlay(true); // 문 열린 이미지로 변경
    }

    @Override
    // 매 프레임 실행
    public int Update(long _gameTime) {
        return super.Update(_gameTime);
    }

    @Override
    // 충돌 시 호출 되는 함수. 인자인 obj 는 자신과 충돌한 오브젝트임!
    public void OnCollision(GameObject obj, int objID) {
        if(m_isOpen && objID == GameState.OBJ_PLAYER) {
            GameState gameState = null;
            switch (m_stageID){     // 각 스테이지에 맞춰 다음 스테이지를 생성해줌
                case GameState.STATE_TEST:
                    gameState = new Stage1();
                    break;
                case GameState.STATE_ONE:
                    gameState = new Stage2();
                    break;
                case GameState.STATE_TWO:
                    gameState = new Stage_Boss();
                    break;
            }
            AppManager.getInstance().ChangeGameState(gameState); // 생성한 스테이지로 GameState 변경
        }
    }
}