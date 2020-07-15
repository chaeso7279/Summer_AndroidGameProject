package com.mobileisaccframework.State;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;

public abstract class GameState {

    public abstract void Initialize();
    public abstract void Update(long _gameTime);
    public void Render(Canvas canvas) {
        //canvas.drawColor(Color.BLACK);
    }

    public abstract boolean onKeyDown(int _keyCode, KeyEvent _event);
    public abstract boolean onTouchEvent(MotionEvent event);

    public abstract void Destroy();
}
