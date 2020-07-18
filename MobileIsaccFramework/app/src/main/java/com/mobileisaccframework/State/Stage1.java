package com.mobileisaccframework.State;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.GameObject.door.Door;
import com.mobileisaccframework.GameObject.player.Player;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.Pad;
import com.mobileisaccframework.R;

public class Stage1 extends GameState {
    @Override
    public void Initialize() {
        m_stageID = STATE_ONE;
        super.Initialize();
    }

    @Override
    public void Update(long _gameTime) {
        if(!m_isInit) // 아직 Initialize 가 진행되지 않았다면 더 이상 진행 X
            return;

        m_backGround.Update(_gameTime);

        for(int i = 0; i < OBJ_END; ++i) {          // 반복자 하나로 모든 오브젝트 접근 -> iterator 패턴사용
            for (int j = 0; j < m_lstObject[i].size(); ++j) {
                GameObject obj = m_lstObject[i].get(j);

                int iEvent = obj.Update(_gameTime);
                if (iEvent == GameObject.DEAD_OBJ) { // 오브젝트가 죽었을 때
                    m_lstObject[i].remove(j);
                }
            }
        }

        // 문열기 검사
        CheckOpenDoor();

        // 오브젝트의 업데이트(좌표 등의 변경이 이뤄짐)를 모두 끝낸 후 충돌체크
        CheckCollision();
    }

    @Override
    public void Render(Canvas canvas) {
        super.Render(canvas);

        if(!m_isInit) // 아직 Initialize 가 진행되지 않았다면 더 이상 진행 X
            return;
        if(canvas == null)
            return;

        // 배경 출력
        m_backGround.Render(canvas);

        //GameObject 출력
        for(int i = 0; i < OBJ_END; ++i) {          // 반복자 하나로 모든 오브젝트 접근 -> iterator 패턴사용
            for(GameObject obj : m_lstObject[i])
                obj.Render(canvas);
        }

        // Pad 출력
        m_pad.Render(canvas);
    }

    @Override
    public void AddObject() {
        // 백그라운드
        CreateBackground(166, 26);

        // 문
        CreateDoor(Door.DOOR_FRONT);
        // 전 스테이지에서 들어온 문(장식용임)
        GameObject object = new GameObject(AppManager.getInstance().getBitmap(R.drawable.golddoor_left),
                AppManager.getInstance().getBitmapWidth(R.drawable.golddoor_left),
                AppManager.getInstance().getBitmapHeight(R.drawable.golddoor_left),
                210, 532);
        m_lstObject[OBJ_ETC].add(object);

        // 플레이어
        CreatePlayer(400, 580);
        // 이전 스테이지에서 저장한 체력을 현재 플레이어에 전달
        AppManager.getInstance().LoadPlayerHP();

//        //몬스터
//        //Enemy_1
//        CreateEnemy_1(400,500);
//        CreateEnemy_1(1900,900);
//
//        //Enemy_2
//        CreateEnemy_2(395,307);
//        CreateEnemy_2(1900,1000);

        // 불꽃
//        CreateFire(620, 532);
//        CreateFire(995, 682);
//        CreateFire(1520,457);
//        CreateFire(1595,982);
//
//        // 블록
//        CreateBlock(1970,532);

        // UI
        CreateUI();
    }

    @Override
    public boolean onKeyDown(int _keyCode, KeyEvent _event) {
        if(_event.getAction() != KeyEvent.ACTION_DOWN)
            return true;

        if(_keyCode == KeyEvent.KEYCODE_SPACE)
            AppManager.getInstance().m_bRenderRect = !AppManager.getInstance().m_bRenderRect;

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(m_pad != null)
            m_pad.OnTouchEvent(event);
        return true;
    }

    @Override
    public void Destroy() {

    }
}
