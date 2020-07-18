package com.mobileisaccframework.GameObject.player;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.GameObject.GameObjectState;
import com.mobileisaccframework.GameObject.bullet.Bomb;
import com.mobileisaccframework.GameObject.bullet.Bullet;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.Manager.CollisionManager;
import com.mobileisaccframework.R;
import com.mobileisaccframework.State.GameState;
import com.mobileisaccframework.Vector2D;

import java.util.ArrayList;

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

    public static final int GAP_ATTACK = 800;   // 공격 시 시간 갭
    public static final int GAP_HIT = 1000; // Hit 시 시간 갭(맞고 나서 1초동안 무적상태)

    public static final int MAX_HP = 10;    // 최대 체력

    static final int ATT_BULLET = 0;    // 공격 종류
    static final int ATT_BOMB = 1;

    private int m_moveSpeed; // 움직이는 속도
    private int m_hp = MAX_HP;

    // 일정시간마다 공격하기 위한 타이머
    private long m_attackTimer = System.currentTimeMillis();
    // 공격 당한 후(맞은 후) 일정시간동안 공격받지 않기 위한 타이머
    private long m_hitTimer = System.currentTimeMillis();

    boolean m_isMove = false;
    boolean m_isAttack = false;
    boolean m_isHit = false;

    public Player(Bitmap bitmap, int _imgWidth, int _imgHeight, int _posX, int _posY, int _fps, int _frameCnt, boolean _isLoop) {
        super(bitmap, _imgWidth, _imgHeight, _posX, _posY, _fps, _frameCnt, _isLoop);
    }

    public int GetPlayerHP() { return m_hp; }
    public void SetPlayerHP(int _hp) { m_hp = _hp; }

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
        // 움직일 수 있는 곳 인지 체크
        MoveCheck();

        // 일정 시간마다만 공격되도록 함
        if(m_isAttack) {
           if(_gameTime - m_attackTimer >  GAP_ATTACK) {    // 시간차가 정한 갭보다 크면 공격
               m_attackTimer = _gameTime;
               m_isAttack = false;
           }
        }

        // 일정 시간마다만 공격 받을 수 있도록함
        if (m_isHit) {
            if(_gameTime - m_hitTimer > GAP_HIT){ // 시간차가 정한 갭보다 크면 맞을 수 있음
                m_hitTimer = _gameTime;
                m_isHit = false;
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

    @Override
    public void OnCollision(GameObject object, int objID) {
        if(m_isHit) // 무적 시간(맞은 후 일정 시간(1초)이 지나지 않음) 유지중이면 데미지 X
            return;

        switch (objID) {
            case GameState.OBJ_ENEMY:
            case GameState.OBJ_BULLET_ENEMY:

                break;
        }
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

    private void MoveCheck() {
        if(!m_isMove) // 이동 버튼이 눌리고 있을 때만 좌표 변경
            return;

        int posX = m_vecPos.x + m_vecDir.x * m_moveSpeed;
        int posY = m_vecPos.y + m_vecDir.y * m_moveSpeed;

        // 현재 GameState 로부터 맵 오브젝트 리스트 받아옴
        ArrayList<GameObject> lstMapObject = AppManager.getInstance().
                getCurGameState().GetObjectList(GameState.OBJ_MAP);

        if(lstMapObject != null) {
            Rect testBox = new Rect();
            testBox.set(posX, posY, posX + m_imgWidth, posY + m_imgHeight);

            // 맵 오브젝트와 플레이어 겹침 검사
            for(int i = 0; i < lstMapObject.size(); ++i){
                GameObject mapObject = lstMapObject.get(i);
                if(CollisionManager.CheckCollision(testBox, mapObject.getBoundBox())){
                    // 이동한 위치가 맵오브젝트(불,블럭)과 겹치면 이동 X
                    return;
                }
            }
        }

        // 이동한 위치가 벽을 넘어가지 않을 때만 이동 적용
        if(posX > AppManager.MIN_X + 40 && posX < AppManager.MAX_X)
            m_vecPos.x += m_vecDir.x * m_moveSpeed;
        if(posY > AppManager.MIN_Y && posY < AppManager.MAX_Y)
            m_vecPos.y += m_vecDir.y * m_moveSpeed;
    }

    public void Attack(int iType) {
        if(m_isAttack)
            return;

        GameObject obj = null;
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
                        m_lstObject[GameState.OBJ_BOMB_PLAYER].add(obj);
        }

        m_isAttack = true;
    }

    private void Hit(){
        m_isHit = true;
        --m_hp;
        if(AppManager.getInstance().m_isNoDead){
            if(m_hp <= 1)     // 시연용으로 HP가 1이 되면 더이상 HP가 줄어들지 않도록 바꿈
                ++m_hp;
        }
        else {
            if(m_hp <= 0)   // HP가 0이 되면 죽음
                m_isDead = true;
        }
    }
}
