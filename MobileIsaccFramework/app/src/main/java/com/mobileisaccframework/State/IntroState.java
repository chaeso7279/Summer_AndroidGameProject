package com.mobileisaccframework.State;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.Manager.SoundManager;
import com.mobileisaccframework.R;

public class IntroState extends GameState {
    GameObject m_introUI;

    @Override
    public void Initialize() {
        m_stageID = STATE_INTRO;
        AddObject();
        m_isInit = true;

        // 배경음악 재생
        SoundManager.getInstance().PlayBGM(SoundManager.BGM_INTRO);
    }

    @Override
    public void Update(long _gameTime) {
        if(!m_isInit) // 아직 Initialize 가 진행되지 않았다면 더 이상 진행 X
            return;

        m_backGround.Update(_gameTime);
        m_introUI.Update(_gameTime);
    }

    @Override
    public void Render(Canvas canvas) {
        super.Render(canvas);

        if(!m_isInit) // 아직 Initialize 가 진행되지 않았다면 더 이상 진행 X
            return;
        if(canvas == null)
            return;

        m_backGround.Render(canvas);
        m_introUI.Render(canvas);
    }

    @Override
    public void AddObject() {
        // iterator 패턴 사용 안한 버전
        m_backGround = new GameObject(AppManager.getInstance().getBitmap(R.drawable.title_background),
                AppManager.getInstance().getBitmapWidth(R.drawable.title_background),
                AppManager.getInstance().getBitmapHeight(R.drawable.title_background));
        m_introUI = new GameObject(AppManager.getInstance().getBitmap(R.drawable.title),
                AppManager.getInstance().getBitmapWidth(R.drawable.title),
                AppManager.getInstance().getBitmapHeight(R.drawable.title),
                (AppManager.WIDTH / 2) - 250, (AppManager.HEIGHT / 2) - 100, 5, 2, true);
    }

    @Override
    public boolean onKeyDown(int _keyCode, KeyEvent _event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        AppManager.getInstance().ChangeGameState(new StageTestState());
        return true;
    }

    @Override
    public void Destroy() {

    }
}
