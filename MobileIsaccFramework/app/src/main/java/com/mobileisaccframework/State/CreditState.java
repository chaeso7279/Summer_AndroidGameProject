package com.mobileisaccframework.State;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.R;
import com.mobileisaccframework.Vector2D;

public class CreditState extends GameState {
    static final int BUTTON_HP = 0;
    static final int BUTTON_RESTART = 1;
    static final int BUTTON_TURNOFF = 2;
    static final int BUTTON_END = 3;


    GameObject m_buttonUI;
    Rect m_rectButton[] = new Rect[BUTTON_END]; // 각 버튼 터치 영역

    boolean m_bIsClear;

    public CreditState(boolean isClear){
        m_bIsClear = isClear;
        Initialize();
    }

    @Override
    public void Initialize() {
        m_stageID = STATE_INTRO;
        AddObject();

        // 각 버튼 터치 영역 설정
        Vector2D buttonPos = m_buttonUI.getPosition();

        for(int i = 0; i < BUTTON_END; ++i)
            m_rectButton[i] = new Rect(buttonPos.x + (105*4 * (i)), buttonPos.y,
                    buttonPos.x + (105*4 * (i + 1)), buttonPos.y + (109*4));

        m_isInit = true;
    }

    @Override
    public void Update(long _gameTime) {
        if(!m_isInit) // 아직 Initialize 가 진행되지 않았다면 더 이상 진행 X
            return;
    }

    @Override
    public void Render(Canvas canvas) {
        super.Render(canvas);

        if(!m_isInit) // 아직 Initialize 가 진행되지 않았다면 더 이상 진행 X
            return;
        if(canvas == null)
            return;

        m_backGround.Render(canvas);
        m_buttonUI.Render(canvas);

    }

    @Override
    public void AddObject() {
        if(m_bIsClear)
            m_backGround = new GameObject(AppManager.getInstance().getBitmap(R.drawable.credit_background_clear),
                    AppManager.getInstance().getBitmapWidth(R.drawable.credit_background_clear),
                    AppManager.getInstance().getBitmapHeight(R.drawable.credit_background_clear));
        else
            m_backGround = new GameObject(AppManager.getInstance().getBitmap(R.drawable.credit_background_die),
                    AppManager.getInstance().getBitmapWidth(R.drawable.credit_background_die),
                    AppManager.getInstance().getBitmapHeight(R.drawable.credit_background_die));

        m_buttonUI = new GameObject(AppManager.getInstance().getBitmap(R.drawable.ui_buttoncredit),
                AppManager.getInstance().getBitmapWidth(R.drawable.ui_buttoncredit),
                AppManager.getInstance().getBitmapHeight(R.drawable.ui_buttoncredit),
                (AppManager.WIDTH / 2) - 600, (AppManager.HEIGHT / 2) - 350);
    }

    @Override
    public boolean onKeyDown(int _keyCode, KeyEvent _event) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getActionMasked() != MotionEvent.ACTION_DOWN)
            return true;

        int x = (int)event.getX();
        int y = (int)event.getY();

        // 각 영역마다 터치 확인
        if(m_rectButton[BUTTON_HP].contains(x,y)) // 건강한 피와 터치 시 플레이어 안죽음 모드
            AppManager.getInstance().m_isNoDead = true;

        else if(m_rectButton[BUTTON_RESTART].contains(x,y)) // 재시작 시 인트로 화면으로
            AppManager.getInstance().ReStartGame();

        else if(m_rectButton[BUTTON_TURNOFF].contains(x,y)) // 종료 시 앱 종료
            android.os.Process.killProcess(android.os.Process.myPid());

        return true;
    }

    @Override
    public void Destroy() {

    }
}
