package com.mobileisaccframework.Manager;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.View;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.GameObject.player.Player;
import com.mobileisaccframework.GameView;
import com.mobileisaccframework.State.CreditState;
import com.mobileisaccframework.State.GameState;
import com.mobileisaccframework.State.IntroState;

public class AppManager {
    public static int WIDTH = 2440;
    public static int HEIGHT = 1440;

    // 벽 좌표(객체는 이 좌표를 넘어가지 않음)
    public static int MIN_X = 300;
    public static int MIN_Y = 150;
    public static int MAX_X = 2045;
    public static int MAX_Y = 1020;

    public static GameObject m_player;

    private GameView m_gameView;
    private Resources m_resources;
    private GameState m_curGameState; // 현재 게임 State

    public int m_savedPlayerHP = 0;

    public boolean m_bRenderRect = false; // 충돌 박스 그릴 여부
    public boolean m_isNoDead = false;   // 플레이어 안죽게 할 지 여부

    public void setGameView(GameView _gameView) { m_gameView = _gameView; }
    public void setResources(Resources _resources) { m_resources = _resources; }
    public void setCurGameState(GameState _state) { m_curGameState = _state; }

    public GameView getGameView() { return m_gameView; }
    public Resources getResources() { return m_resources; }
    public GameState getCurGameState() { return m_curGameState; }
    public Bitmap getBitmap(int r) {
        return BitmapFactory.decodeResource(m_resources, r);
    }

    public int getBitmapWidth(int r) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(m_resources, r, options);

        return options.outWidth;
    }

    public int getBitmapHeight(int r) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(m_resources, r, options);

        return options.outHeight;
    }

    public void ChangeGameState(GameState _state) {
        if(m_gameView != null)
            m_gameView.changeGameState(_state);
    }

    public void SavePlayerHP() {     // 다음 스테이지의 플레이어에 체력 전달하기 위해 이전 스테이지 체력을 저장
        if(m_player == null)
            return;
        m_savedPlayerHP = ((Player)m_player).GetPlayerHP();
    }

    public void LoadPlayerHP() { // 이전 스테이지에서 저장한 체력을 현재 플레이어에 전달
        if(m_player == null)
            return;
        ((Player)m_player).SetPlayerHP(m_savedPlayerHP);
    }

    public void PlayerDead(){
        m_player = null;
        ChangeGameState(new CreditState(false));
    }

    public void GameClear() {
        ChangeGameState(new CreditState(true));
    }

    public void ReStartGame() {
        ChangeGameState(new IntroState());
    }

    // 싱글톤
    private static AppManager m_instance;

    public static AppManager getInstance() {
        if(m_instance == null)
            m_instance = new AppManager();
        return m_instance;
    }
}
