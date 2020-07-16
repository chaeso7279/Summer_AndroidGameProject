package com.mobileisaccframework;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.State.GameState;
import com.mobileisaccframework.State.IntroState;
import com.mobileisaccframework.State.Stage1;
import com.mobileisaccframework.State.Stage2;
import com.mobileisaccframework.State.Stage_Boss;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private GameViewThread m_thread;
    private GameState m_state;

    public GameView(Context context){
        super(context);

        setFocusable(true);

        Initialize();

        getHolder().addCallback(this);
        m_thread = new GameViewThread(getHolder(), this);
    }

    void Initialize() {
        // 각 매니저 초기화
        AppManager.getInstance().setGameView(this);
        AppManager.getInstance().setResources(getResources());

        // 스테이트 설정
        //changeGameState(new IntroState());
//        changeGameState(new Stage1());
        //changeGameState(new Stage2());
        changeGameState(new Stage_Boss());
    }

    public void Update() {
        long gameTime = System.currentTimeMillis();

        // State Update
        m_state.Update(gameTime);
    }

    @Override
    public void onDraw(Canvas canvas) {
        // State Render
        m_state.Render(canvas);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 충돌 박스 출력 끄고 킬 수 있게
        if(keyCode == KeyEvent.KEYCODE_F1)
            AppManager.getInstance().m_bRenderRect = !AppManager.getInstance().m_bRenderRect;

        return m_state.onKeyDown(keyCode, event);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return m_state.onTouchEvent(event);
    }

    // State 변경 함수
    public void changeGameState(GameState _state) {
        if(m_state != null)
            m_state.Destroy();
        _state.Initialize();
        m_state = _state;

        AppManager.getInstance().setCurGameState(m_state);
    }

    // Surface 관련 함수
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        m_thread.setRunning(true);
        m_thread.start(); // 스레드 실행
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        m_thread.setRunning(false);
        while (retry) {
            try{
                // 스레드 중지
                m_thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // 에러일 경우 스레드가 종료되도록 계속 시도함(retry가 true이기때문)
            }
        }
    }
}
