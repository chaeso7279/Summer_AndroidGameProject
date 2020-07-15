package com.mobileisaccframework.State;

import android.view.KeyEvent;
import android.view.MotionEvent;

public abstract class GameState {

    public abstract void Initialize();
    public abstract void Update();
    public void Render() {

    }

    public abstract boolean onKeyDown(int _keyCode, KeyEvent _event);
    public abstract boolean onTouchEvent(MotionEvent event);

    public abstract void Destroy();
}
