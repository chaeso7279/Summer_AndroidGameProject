package com.mobileisaccframework.State;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.Manager.SoundManager;
import com.mobileisaccframework.R;

public class Stage_Boss extends GameState {
    // 보스가 죽어도 바로 크레딧으로 넘어가지 않도록 할 것임
    private static final int GAP_CREDIT = 2000;
    private long m_creditTimer = 0;

    @Override
    public void Initialize() {
        m_stageID = STATE_BOSS;
        super.Initialize();
        SoundManager.getInstance().PlayBGM(SoundManager.BGM_BOSS);
    }
    @Override
    public void Update(long _gameTime) {
        if(!m_isInit) // 아직 Initialize 가 진행되지 않았다면 더 이상 진행 X
            return;

        m_backGround.Update(_gameTime);

        for(int i = 0; i < OBJ_END; ++i) {          // 반복자 하나로 모든 오브젝트 접근 -> iterator 패턴사용
            for (int j = 0; j < m_lstObject[i].size(); ++j) {
                GameObject obj = m_lstObject[i].get(j);

                int iEvent = obj.Update(_gameTime);
                if (iEvent == GameObject.DEAD_OBJ) { // 오브젝트가 죽었을 때
                    m_lstObject[i].remove(j);
                }
            }
        }

        CheckCollision();

        // 보스가 죽어도 바로 크레딧으로 넘어가지 않도록 할 것임
        if(AppManager.m_boss == null){
            if(_gameTime - m_creditTimer > GAP_CREDIT){
                m_creditTimer = _gameTime;
                AppManager.getInstance().GameClear();
            }
        }
    }

    @Override
    public void Render(Canvas canvas) {
        super.Render(canvas);

        if(!m_isInit) // 아직 Initialize 가 진행되지 않았다면 더 이상 진행 X
            return;
        if(canvas == null)
            return;

        // 배경 출력
        m_backGround.Render(canvas);

        for(int i = 0; i < OBJ_END; ++i) {          // 반복자 하나로 모든 오브젝트 접근 -> iterator 패턴사용
            for(GameObject obj : m_lstObject[i])
                obj.Render(canvas);
        }

        // Pad 출력
        m_pad.Render(canvas);
    }

    @Override
    public void AddObject() {
        // 백그라운드
        CreateBackground(166, 26);

        // 전 스테이지에서 들어온 문(장식용임)
        GameObject object = new GameObject(AppManager.getInstance().getBitmap(R.drawable.golddoor_left),
                AppManager.getInstance().getBitmapWidth(R.drawable.golddoor_left),
                AppManager.getInstance().getBitmapHeight(R.drawable.golddoor_left),
                210, 532);
        m_lstObject[OBJ_ETC].add(object);

        // 플레이어
        CreatePlayer(400, 580);
        // 이전 스테이지에서 저장한 체력을 현재 플레이어에 전달
        AppManager.getInstance().LoadPlayerHP();

        // 보스
        CreateBoss(1100,500);

        // UI
        CreateUI();
    }

    @Override
    public boolean onKeyDown(int _keyCode, KeyEvent _event) {
        if(_event.getAction() != KeyEvent.ACTION_DOWN)
            return true;

        if(_keyCode == KeyEvent.KEYCODE_SPACE)
            AppManager.getInstance().m_bRenderRect = !AppManager.getInstance().m_bRenderRect;

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(m_pad != null)
            m_pad.OnTouchEvent(event);
        return true;
    }

    @Override
    public void Destroy() {

    }

    public void StartTimer() {
        m_creditTimer = System.currentTimeMillis();
    }
}
