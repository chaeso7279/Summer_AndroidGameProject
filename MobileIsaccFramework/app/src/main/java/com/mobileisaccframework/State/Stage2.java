package com.mobileisaccframework.State;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.GameObject.door.Door;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.R;

public class Stage2 extends GameState {
    @Override
    public void Initialize() {
        m_stageID = STATE_TWO;
        super.Initialize();
    }

    @Override
    public void Update(long _gameTime) {
        if(!m_isInit) // 아직 Initialize 가 진행되지 않았다면 더 이상 진행 X
            return;

        m_backGround.Update(_gameTime);

        for (int i = 0; i < OBJ_END; ++i) { // 반복자 하나로 모든 오브젝트 접근 -> iterator 패턴사용
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
        CreateDoor(Door.DOOR_RIGHT);
        // 전 스테이지에서 들어온 문(장식용임)
        GameObject object = new GameObject(AppManager.getInstance().getBitmap(R.drawable.golddoor_back),
                AppManager.getInstance().getBitmapWidth(R.drawable.golddoor_back),
                AppManager.getInstance().getBitmapHeight(R.drawable.golddoor_back),
                1200, 1160);
        m_lstObject[OBJ_ETC].add(object);

        // 플레이어
        CreatePlayer(1200, 1000);
        // 이전 스테이지에서 저장한 체력을 현재 플레이어에 전달
        AppManager.getInstance().LoadPlayerHP();

        // 불꽃
        CreateFire(545, 682);
        CreateFire(1220, 382);
        CreateFire(1370,907);

        // 블록
        CreateBlock(1940, 402);
        CreateBlock(1940,652);

//        // 적
//        CreateEnemy_2(1000,230);

        // UI
        CreateUI();
    }

    @Override
    public boolean onKeyDown(int _keyCode, KeyEvent _event) {
        return false;
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
