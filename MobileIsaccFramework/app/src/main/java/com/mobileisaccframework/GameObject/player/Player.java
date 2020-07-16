package com.mobileisaccframework.GameObject.player;

import android.graphics.Bitmap;
import android.graphics.Rect;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.GameObject.GameObjectState;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.R;

enum PLAYER_STATE { }

public class Player extends GameObject {
    static final int IDLE_FRONT = 0;
    static final int IDLE_BACK = 1;
    static final int IDLE_LEFT = 2;
    static final int IDLE_RIGHT = 3;
    static final int WALK_FRONT = 4;
    static final int WALK_BACK = 5;
    static final int WALK_LEFT = 6;
    static final int WALK_RIGHT = 7;
    static final int STATE_END = 8;

    public Player(Bitmap bitmap, int _imgWidth, int _imgHeight, int _fps, int _frameCnt, boolean _isLoop) {
        super(bitmap, _imgWidth, _imgHeight, _fps, _frameCnt, _isLoop);
    }

    public Player(Bitmap bitmap, int _imgWidth, int _imgHeight, int _posX, int _posY, int _fps, int _frameCnt, boolean _isLoop) {
        super(bitmap, _imgWidth, _imgHeight, _posX, _posY, _fps, _frameCnt, _isLoop);
    }

    // 초기 데이터 설정
    @Override
    public void Initialize() {
       super.Initialize();

       m_state = IDLE_FRONT;
       m_arrFrameCnt = new int[STATE_END];

       for(int i = IDLE_FRONT; i <= IDLE_RIGHT; ++i)
           m_arrFrameCnt[i] = 2;
    }

    // 매 프레임 실행
    @Override
    public void Update(long _gameTime) {
        super.Update(_gameTime);

    }

    @Override
    public void ChangeState(int _state) {
        if(m_state == _state)
            return;

        if(m_objectState != null)
            m_objectState.Destroy();

        int rID = 0;
        int fps = 0;
        boolean isLoop = true;

        switch (_state) {
            case IDLE_FRONT:
                rID = R.drawable.player_idle_front;
                fps = 10;
                break;
            case IDLE_BACK:
                rID = R.drawable.player_idle_back;
                fps = 10;
                break;
            case IDLE_LEFT:
                rID = R.drawable.player_idle_left;
                fps = 10;
                break;
            case IDLE_RIGHT:
                rID = R.drawable.player_idle_right;
                fps = 10;
                break;
        }

        Bitmap bitmap = AppManager.getInstance().getBitmap(rID);

        int width = (AppManager.getInstance().getBitmapWidth(rID) * 4) / m_arrFrameCnt[_state];
        int height = AppManager.getInstance().getBitmapHeight(rID) * 4;

        m_objectState = new GameObjectState(this,bitmap, width, height,
                fps, m_arrFrameCnt[_state], isLoop);
        m_objectState.Initialize();

        m_state = _state;
    }
}
