package com.mobileisaccframework.State;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.GameObject.enemy.Enemy_2;
import com.mobileisaccframework.GameObject.enemy.Enemy_Boss;
import com.mobileisaccframework.GameObject.player.Player;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.R;

public class Stage_Boss extends GameState{
    GameObject m_background;

    @Override
    public void Initialize() {
        super.Initialize();

        AddObject();
    }
    @Override
    public void Update(long _gameTime) {
        m_background.Update(_gameTime);

        for(int i = 0; i < OBJ_END; ++i) {          // 반복자 하나로 모든 오브젝트 접근 -> iterator 패턴사용
            for(GameObject obj : m_lstObject[i])
                obj.Update(_gameTime);
        }
    }

    @Override
    public void Render(Canvas canvas) {
        super.Render(canvas);

        if(canvas == null)
            return;

        // 배경 출력
        m_background.Render(canvas);

        for(int i = 0; i < OBJ_END; ++i) {          // 반복자 하나로 모든 오브젝트 접근 -> iterator 패턴사용
            for(GameObject obj : m_lstObject[i])
                obj.Render(canvas);
        }
    }

    @Override
    public void AddObject() {
        GameObject object = null;

        //배경
        m_background = CreateBackground(166,26);

        // 플레이어
        m_lstObject[OBJ_PLAYER].add(CreatePlayer(1200,500));

        //몬스터
        m_lstObject[OBJ_ENEMY].add(CreateBoss(1200,500));
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
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public void Destroy() {

    }
}
