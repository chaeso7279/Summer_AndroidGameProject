package com.mobileisaccframework.State;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.mobileisaccframework.GameObject.GameObject;

import java.util.ArrayList;

public class StageTestState extends GameState {
    // 오브젝트 구분 번호
    public static int OBJ_MAP = 0;
    public static int OBJ_PLAYER = 1;
    public static int OBJ_ENEMY = 2;
    public static int OBJ_BULLET_PLAYER = 3;
    public static int OBJ_BULLET_ENEMY = 4;
    public static int OBJ_EFFECT = 5;
    public static int OBJ_UI = 6;
    public static int OBJ_END = 7;

    ArrayList<GameObject>[] m_lstObject;

    @Override
    public void Initialize() {
        m_lstObject = new ArrayList[OBJ_END];
        for(int i = 0; i < OBJ_END; ++i)
            m_lstObject[i] = new ArrayList<GameObject>();
    }

    @Override
    public void Update(long _gameTime) {
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
        
        for(int i = 0; i < OBJ_END; ++i) {          // 반복자 하나로 모든 오브젝트 접근 -> iterator 패턴사용
            for(GameObject obj : m_lstObject[i])
                obj.Render(canvas);
        }
    }

    @Override
    public boolean onKeyDown(int _keyCode, KeyEvent _event) {
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
