package com.mobileisaccframework.State;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.GameObject.enemy.Enemy_2;
import com.mobileisaccframework.GameObject.enemy.Enemy_Boss;
import com.mobileisaccframework.GameObject.player.Player;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.Manager.CollisionManager;
import com.mobileisaccframework.Pad;
import com.mobileisaccframework.R;

public class Stage_Boss extends GameState {
    @Override
    public void Initialize() {
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

        // 플레이어
        CreatePlayer(1200,500);

        // 보스
        CreateBoss(1200,500);

        // UI
        CreateUI();
    }

    @Override
    public boolean onKeyDown(int _keyCode, KeyEvent _event) {
        if(_keyCode == KeyEvent.KEYCODE_1){
            m_lstObject[OBJ_ENEMY].get(0).ChangeState(Enemy_Boss.STATE_IDLE);
        }
        if(_keyCode == KeyEvent.KEYCODE_2){
            m_lstObject[OBJ_ENEMY].get(0).ChangeState(Enemy_Boss.STATE_ATTACK);
        }
        if(_keyCode == KeyEvent.KEYCODE_3){
            m_lstObject[OBJ_ENEMY].get(0).ChangeState(Enemy_Boss.STATE_JUMP);
        }
        if(_keyCode == KeyEvent.KEYCODE_4){
            m_lstObject[OBJ_ENEMY].get(0).setPosition(400,700);
        }
        if(_keyCode == KeyEvent.KEYCODE_5){
            m_lstObject[OBJ_ENEMY].get(0).setPosition(400,230);
        }
        if(_keyCode == KeyEvent.KEYCODE_6){
            m_lstObject[OBJ_ENEMY].get(0).setPosition(1400,230);
        }
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
