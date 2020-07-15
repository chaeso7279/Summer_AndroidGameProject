package com.mobileisaccframework.State;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;

public abstract class GameState {       // 교수님 코드에서의 IState

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
