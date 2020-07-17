package com.mobileisaccframework.State;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.GameObject.MapObject.BlockObject;
import com.mobileisaccframework.GameObject.MapObject.FireObject;
import com.mobileisaccframework.GameObject.enemy.Enemy_2;
import com.mobileisaccframework.GameObject.player.Player;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.R;

public class Stage2 extends GameState {
    GameObject m_door;

    @Override
    public void Initialize() {
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
    }

    @Override
    public void AddObject() {
        // 백그라운드
        CreateBackground(166, 26);

        // 문
        m_door = CreateDoor(1520, 82, R.drawable.golddoor_front);

        // 플레이어
        CreatePlayer(1200, 700);

        // 적
        CreateEnemy_2(1000,230);

        // 불꽃
        CreateFire(545, 682);
        CreateFire(920, 982);
        CreateFire(1220,382);
        CreateFire(1670,907);
        CreateFire(1745,382);

        // 블록
        CreateBlock(770,607);
        CreateBlock(1370,757);
        CreateBlock(1820,532);

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
