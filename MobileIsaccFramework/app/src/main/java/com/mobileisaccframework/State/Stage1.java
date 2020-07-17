package com.mobileisaccframework.State;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.GameObject.MapObject.BlockObject;
import com.mobileisaccframework.GameObject.MapObject.FireObject;
import com.mobileisaccframework.GameObject.enemy.Enemy_1;
import com.mobileisaccframework.GameObject.enemy.Enemy_2;
import com.mobileisaccframework.GameObject.player.Player;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.Manager.CollisionManager;
import com.mobileisaccframework.Pad;
import com.mobileisaccframework.R;

public class Stage1 extends GameState {
    GameObject m_background;
    GameObject m_door;

    // 방향키 패드
    Pad m_pad;

    @Override
    public void Initialize() {
        super.Initialize();

        AddObject();
    }

    @Override
    public void Update(long _gameTime) {
        m_background.Update(_gameTime);
        m_door.Update(_gameTime);

        for(int i = 0; i < OBJ_END; ++i) {          // 반복자 하나로 모든 오브젝트 접근 -> iterator 패턴사용
            for (int j = 0; j < m_lstObject[i].size(); ++j) {
                GameObject obj = m_lstObject[i].get(j);

                int iEvent = obj.Update(_gameTime);
                if (iEvent == GameObject.DEAD_OBJ) { // 오브젝트가 죽었을 때
                    m_lstObject[i].remove(j);
                }
            }
        }
    }

    @Override
    public void Render(Canvas canvas) {
        super.Render(canvas);

        if(canvas == null)
            return;

        // 배경 출력
        m_background.Render(canvas);
        m_door.Render(canvas);

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
        m_door = CreateDoor(2045, 532, R.drawable.golddoor_right);

        // 플레이어
        CreatePlayer(1500, 800);

        //몬스터
        //Enemy_1
        CreateEnemy_1(400,500);
        CreateEnemy_1(1900,900);

        //Enemy_2
        CreateEnemy_2(395,307);
        CreateEnemy_2(1900,1000);

        // 불꽃
        CreateFire(620, 532);
        CreateFire(995, 682);
        CreateFire(1520,457);
        CreateFire(1595,982);

        // 블록
        CreateBlock(1970,532);

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
