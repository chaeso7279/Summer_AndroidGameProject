package com.mobileisaccframework;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private GameViewThread m_thread;

    public GameView(Context context){
        super(context);

        setFocusable(true);

        getHolder().addCallback(this);
        m_thread = new GameViewThread(getHolder(), this);

        Initialize();
    }

    void Initialize() {
        // 각 매니저 초기화

        // 스테이트 설정

    }

    public void Update() {
        long gameTime = System.currentTimeMillis();

        // State Update
    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        // State Render
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //return m_scene.onKeyDown(keyCode, event);
        return true;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //return m_scene.onTouchEvent(event);
        return true;
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
