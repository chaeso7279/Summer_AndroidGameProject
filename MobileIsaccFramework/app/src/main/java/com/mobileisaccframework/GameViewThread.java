package com.mobileisaccframework;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameViewThread extends Thread{
    // 접근을 위한 멤버 변수
    private SurfaceHolder m_surfaceHolder;
    private GameView m_gameView;

    // 스레드 실행 상태 멤버 변수
    private boolean m_run = false;
    public GameViewThread(SurfaceHolder surfaceHolder, GameView gameView){
        m_surfaceHolder = surfaceHolder;
        m_gameView = gameView;
    }

    public void setRunning(boolean run) {
        m_run = run;
    }

    @SuppressLint("WrongCall")
    @Override
    public void run() {
        Canvas canvas;
        while(m_run){
            canvas = null;
            try{
                m_gameView.Update();
                // SurfaceHolder를 통해 Surface에 접근해서 가져옴
                canvas = m_surfaceHolder.lockCanvas(null);
                synchronized (m_surfaceHolder) {
                    m_gameView.onDraw(canvas); // 그림을 그림
                }
            } finally {
                if(canvas != null) // Surface를 화면에 표시
                    m_surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
