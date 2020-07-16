package com.mobileisaccframework.State;

import android.view.KeyEvent;
import android.view.MotionEvent;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.R;

public class Stage1 extends GameState {


    GameObject m_sbackgound;

    @Override
    public void Initialize() {
        AddObject();
    }

    @Override
    public void Update(long _gameTime) {
        m_sbackgound.Update(_gameTime);

    }



    @Override
    public void AddObject() {
        m_sbackgound = new GameObject(AppManager.getInstance().getBitmap(R.drawable.mapbackground),
                AppManager.getInstance().getBitmapWidth(R.drawable.mapbackground),
                AppManager.getInstance().getBitmapHeight(R.drawable.mapbackground),
                170, 82);
    }

    @Override
    public boolean onKeyDown(int _keyCode, KeyEvent _event) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public void Destroy() {

    }
}
