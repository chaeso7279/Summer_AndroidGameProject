package com.mobileisaccframework.GameObject.player;

import android.graphics.Bitmap;
import android.util.Log;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.GameObject.GameObjectState;
import com.mobileisaccframework.GameObject.bullet.Bomb;
import com.mobileisaccframework.GameObject.bullet.Bullet;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.R;
import com.mobileisaccframework.State.GameState;
import com.mobileisaccframework.Vector2D;

public class Player extends GameObject {
    public static final int IDLE_FRONT = 0;
    public static final int IDLE_BACK = 1;
    public static final int IDLE_LEFT = 2;
    public static final int IDLE_RIGHT = 3;
    public static final int WALK_FRONT = 4;
    public static final int WALK_BACK = 5;
    public static final int WALK_LEFT = 6;
    public static final int WALK_RIGHT = 7;
    public static final int STATE_END = 8;

    public static final int GAP_ATTACK = 800;

    static final int ATT_BULLET = 0;
    static final int ATT_BOMB = 1;

    private int m_moveSpeed;
    private long m_attackTimer = System.currentTimeMillis();

    boolean m_isMove = false;
    boolean m_isAttack = false;

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

       // 앱 매니저에 넣어줌
        AppManager.getInstance().m_player = this;

        m_curState = IDLE_FRONT;

        // 각 state 마다 프레임 개수가 다름 -> int 배열로 처리
        m_arrFrameCnt = new int[STATE_END];

        // IDLE 은 모두 프레임 개수가 1라서 이렇게 처리함
        for(int i = IDLE_FRONT; i <= IDLE_RIGHT; ++i)
           m_arrFrameCnt[i] = 1;

        for(int i = WALK_FRONT; i <= WALK_RIGHT; ++i)
            m_arrFrameCnt[i] = 10;

        // Speed 설정
        m_moveSpeed = 10;
    }

    // 매 프레임 실행
    @Override
    public int Update(long _gameTime) {
        if(m_isMove){       // 이동 버튼이 눌리고 있을 때만 좌표 변경
            m_vecPos.x += m_vecDir.x * m_moveSpeed;
            m_vecPos.y += m_vecDir.y * m_moveSpeed;
        }
        // 일정 시간마다만 공격되도록 함
        if(m_isAttack) {
           if(_gameTime > m_attackTimer + GAP_ATTACK) {
               m_attackTimer = _gameTime;
               m_isAttack = false;
           }
        }

        return super.Update(_gameTime);
    }

    @Override
    public void ChangeState(int _state) {
        // 이전 state와 변경할 state 같으면 변경 필요X 그래서 return 함
        if(m_curState == _state)
            return;

        if(m_objectState != null)
            m_objectState.Destroy();

        int rID = 0;
        int fps = 0;
        boolean isLoop = true;

        // state에 따라 사용할 비트맵(리소스 아이디), fps, 반복유무(isLoop) 지정
        switch (_state) {
            case IDLE_FRONT:
                rID = R.drawable.player_idle_front;
                fps = 1;
                break;
            case IDLE_BACK:
                rID = R.drawable.player_idle_back;
                fps = 1;
                break;
            case IDLE_LEFT:
                rID = R.drawable.player_idle_left;
                fps = 1;
                break;
            case IDLE_RIGHT:
                rID = R.drawable.player_idle_right;
                fps = 1;
                break;
            case WALK_FRONT:
                rID = R.drawable.player_walk_front;
                fps = 15;
                break;
            case WALK_BACK:
                rID = R.drawable.player_walk_back;
                fps = 15;
                break;
            case WALK_LEFT:
                rID = R.drawable.player_walk_left;
                fps = 15;
                break;
            case WALK_RIGHT:
                rID = R.drawable.player_walk_right;
                fps = 15;
                break;
        }

        // AppManager 로부터 비트맵 가져옴
        Bitmap bitmap = AppManager.getInstance().getBitmap(rID);

        // m_arrFrameCnt가 각 상태마다의 프레임 개수니까
        // 다음 코드는 (이미지 크기 / 프레임 개수) 와 같음
        int width = (AppManager.getInstance().getBitmapWidth(rID) * 4) / m_arrFrameCnt[_state];
        int height = AppManager.getInstance().getBitmapHeight(rID) * 4;

        // 오브젝트 스테이트 지정
        m_objectState = new GameObjectState(this, bitmap, width, height,
                fps, m_arrFrameCnt[_state], isLoop);
        m_objectState.Initialize();

        // 이건 단순히 오브젝트 스테이트를 숫자로 쓰는 용도
        m_curState = _state;
    }

    public void Move(Vector2D _vecDir, int iState) {
        ChangeState(iState);
        m_vecDir = _vecDir;
        m_isMove = true;
    }

    public void MoveStop() {
        m_vecDir.x = 0;
        m_vecDir.y = 0;

        m_isMove = false;

        // 현재 걷는 방향에 따라 대기모션 방향도 정해줌
        switch (m_curState) {
            case WALK_BACK:
                ChangeState(IDLE_BACK);
                break;
            case WALK_FRONT:
                ChangeState(IDLE_FRONT);
                break;
            case WALK_LEFT:
                ChangeState(IDLE_LEFT);
                break;
            case WALK_RIGHT:
                ChangeState(IDLE_RIGHT);
                break;
        }
    }

    public void Attack(int iType) {
        if(m_isAttack)
            return;

        GameObject obj = null;
        Vector2D vecDir;
        // 총알일 때
        if(iType == ATT_BULLET) {
            switch (m_curState){
                case IDLE_BACK:
                case WALK_BACK:
                    obj = new Bullet(true, m_vecPos.x - 66, m_vecPos.y - 140,
                            new Vector2D(0, -1));
                    break;
                case IDLE_FRONT:
                case WALK_FRONT:
                    obj = new Bullet(true, m_vecPos.x - 66, m_vecPos.y,
                            new Vector2D(0, 1));
                    break;
                case IDLE_LEFT:
                case WALK_LEFT:
                    obj = new Bullet(true, m_vecPos.x - 150, m_vecPos.y - 50,
                            new Vector2D(-1, 0));
                    break;
                case IDLE_RIGHT:
                case WALK_RIGHT:
                    obj = new Bullet(true, m_vecPos.x, m_vecPos.y - 50,
                            new Vector2D(1, 0));
                    break;
            }

            // 스테이지에 추가 해줌
            if(obj != null)
                AppManager.getInstance().getCurGameState().
                        m_lstObject[GameState.OBJ_BULLET_PLAYER].add(obj);
        }
        else if(iType == ATT_BOMB){     // 폭탄일때
            obj = new Bomb(m_vecPos.x + 20, m_vecPos.y + 50);
            // 스테이지에 추가 해줌
            if(obj != null)
                AppManager.getInstance().getCurGameState().
                        m_lstObject[GameState.OBJ_BULLET_PLAYER].add(obj);
        }

        m_isAttack = true;
    }
}
