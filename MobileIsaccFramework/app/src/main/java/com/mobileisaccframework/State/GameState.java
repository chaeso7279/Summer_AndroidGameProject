package com.mobileisaccframework.State;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;

public abstract class GameState {       // 교수님 코드에서의 IState
    // 오브젝트 구분 번호
    public static int OBJ_MAP = 0;
    public static int OBJ_PLAYER = 1;
    public static int OBJ_ENEMY = 2;
    public static int OBJ_BULLET_PLAYER = 3;
    public static int OBJ_BULLET_ENEMY = 4;
    public static int OBJ_EFFECT = 5;
    public static int OBJ_UI = 6;
    public static int OBJ_END = 7;

    public abstract void Initialize();
    public abstract void Update(long _gameTime);
    public void Render(Canvas canvas) {
        if(canvas == null)
            return;
        canvas.drawColor(Color.BLACK);
    }

    public abstract boolean onKeyDown(int _keyCode, KeyEvent _event);
    public abstract boolean onTouchEvent(MotionEvent event);

    public abstract void Destroy();
}
