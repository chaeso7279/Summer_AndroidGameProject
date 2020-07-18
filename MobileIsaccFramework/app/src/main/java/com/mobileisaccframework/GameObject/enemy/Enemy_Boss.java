package com.mobileisaccframework.GameObject.enemy;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.GameObject.GameObjectState;
import com.mobileisaccframework.GameObject.bullet.Bullet;
import com.mobileisaccframework.GameObject.effect.Effect;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.Manager.CollisionManager;
import com.mobileisaccframework.R;
import com.mobileisaccframework.State.GameState;
import com.mobileisaccframework.Vector2D;

import java.util.ArrayList;
import java.util.Random;

import static com.mobileisaccframework.State.GameState.OBJ_BACK_EFFECT;
import static com.mobileisaccframework.State.GameState.OBJ_BOMB_PLAYER;
import static com.mobileisaccframework.State.GameState.OBJ_EFFECT;

public class Enemy_Boss extends GameObject {
    public static final int STATE_IDLE = 0;
    public static final int STATE_ATTACK = 1;
    public static final int STATE_JUMP = 2;
    public static final int STATE_END = 3;

    public static final int ATTACK_IDLE = 0;
    public static final int ATTACK_JUMP = 1;
    public static final int ATTACK_CIRCLE = 2;
    public static final int ATTACK_PLAYER = 3;

    public static final int GAP_ATTACK = 4000;

    protected int m_curAttackPattern = ATTACK_CIRCLE;

    protected int m_speedX;
    protected int m_speedY;
    protected int m_hp;

    boolean m_isAttack = false;
    private long m_attackTimer = System.currentTimeMillis();

    protected boolean m_isJump = false;
    protected Vector2D m_jumpDest;
    Vector2D m_jumpDir;


    public Enemy_Boss(Bitmap bitmap, int _imgWidth, int _imgHeight, int _fps, int _frameCnt, boolean _isLoop) {
        super(bitmap, _imgWidth, _imgHeight, _fps, _frameCnt, _isLoop);
    }

    public Enemy_Boss(Bitmap bitmap, int _imgWidth, int _imgHeight, int _posX, int _posY, int _fps, int _frameCnt, boolean _isLoop) {
        super(bitmap, _imgWidth, _imgHeight, _posX, _posY, _fps, _frameCnt, _isLoop);
    }

    //초기 데이터 설정
    @Override
    public void Initialize(){
        super.Initialize();

        //초기 state 설정
        m_curState = STATE_IDLE;

        //프레임 개수 설정
        m_arrFrameCnt = new int[STATE_END];

        //ATTACK 빼고 모두 3임
        for(int i = STATE_IDLE; i < STATE_END; ++i)
            m_arrFrameCnt[i] = 3;
        m_arrFrameCnt[STATE_ATTACK] = 18;

        //이동 속도 설정
        m_speedX = 20;
        m_speedY = -100;

        //hp
        m_hp = 3;
    }

    //매 프레임 실행
    @Override
    public int Update(long _gameTime){

        Attack();

        // 일정 시간마다만 공격되도록 함
        if(m_isAttack) {
            if(_gameTime > m_attackTimer + GAP_ATTACK) {
                m_attackTimer = _gameTime;
                m_isAttack = false;
            }
        }

        //점프 상태일 때만 Move 실행
        if(m_curState == STATE_JUMP ) {
            Move();
        }

        return super.Update(_gameTime);
    }

    @Override
    public void ChangeState(int _state) {

        if (m_objectState != null)
            m_objectState.Destroy();

        int rID = 0;
        int fps = 0;
        boolean isLoop = true;

        // state에 따라 사용할 비트맵(리소스 아이디), fps, 반복유무(isLoop) 지정
        switch (_state) {
            case STATE_IDLE:
                rID = R.drawable.boss_idle;
                fps = 5;
                break;
            case STATE_ATTACK:
                rID = R.drawable.boss_attack;
                fps = 5;
                isLoop = true;
                break;
            case STATE_JUMP:
                rID = R.drawable.boss_jump_start;
                fps = 5;
                isLoop = false;
                m_speedY = -20;
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

    //점프 시작 시 목적지, 방향벡터 설정해주는 메소드
    public void SetJump(){
        m_isJump = true;
        //점프할 떄 플레이어의 위치
        if(AppManager.getInstance().m_player!=null){
            m_jumpDest = new Vector2D(AppManager.getInstance().m_player.getPosition());
        }
         //점프할 방향
        m_jumpDir = m_vecPos.getDirection(m_jumpDest);
    }

    public void Move(){
        //점프 첫 시작이면 JumpStart 실행
        if (!m_isJump) {
            SetJump();
        }
        MoveCheck();
    }

    //이동할 수 있는 위치인지 체크하여 이동하는 함수
    private void MoveCheck(){
        //이동하려는 위치
        int posX = m_vecPos.x;
        int posY = m_vecPos.y;

        //화면 위로 올라가면 바로 이동 방향 아래로 바꿈
        if(m_speedY < 0 && posY < -100){
            m_speedY = 0;
        }

        posX += m_speedX * m_jumpDir.x;
        posY += m_speedY;
        ++m_speedY;

        // 이동할 위치가 벽을 넘어가면 멈춤
        if(posX < AppManager.MIN_X + 40 || posX > AppManager.MAX_X
                || posY > AppManager.MAX_Y){
            ChangeState(STATE_IDLE);
            m_isJump = false;
            // 이펙트 생성
            CreateJumpEffect();
            return;
        }

        //boss가 내려오는 상태고, 점프 목적지의 y좌표와 50이하로 차이나면 멈춤
        if( m_speedY > 0 && Math.abs(m_vecPos.y - m_jumpDest.y) < 50){
            ChangeState(STATE_IDLE);
            m_isJump = false;
            CreateJumpEffect();
            return;
        }
        //멈춤 조건에 모두 해당하지 않을 때만 좌표 이동
        setPosition(posX, posY);
    }

    public void Attack(){
        //공격패턴 1~3 랜덤발생
        if(!m_isAttack){
            Random rand = new Random();
            int randInt = rand.nextInt(3) + 1;

            switch(randInt){
                case ATTACK_JUMP:
                    ChangeState(STATE_JUMP);
                    Log.d("attack:", "jump");
                    //m_curAttackPattern = ATTACK_JUMP;
                    break;
                case ATTACK_CIRCLE:
                    ChangeState(STATE_ATTACK);
                    Attack_Circle();
                    //m_curAttackPattern = ATTACK_CIRCLE;
                    break;
                case ATTACK_PLAYER:
                    ChangeState(STATE_ATTACK);
                    Attack_Player();
                   // m_curAttackPattern = ATTACK_PLAYER;
                    break;
            }
        }

        m_isAttack = true;
        m_curAttackPattern = ATTACK_IDLE;

    }


    public void Attack_Circle(){
        Log.d("attack:", "circle");
        //미사일 발사 로직 (enemy이므로 _isPlayer인자는 false)
        GameObject obj = new Bullet(false, m_vecPos.x, m_vecPos.y, new Vector2D(0,1));
        AppManager.getInstance().getCurGameState().m_lstObject[GameState.OBJ_BULLET_ENEMY].add(obj);

        obj = new Bullet(false, m_vecPos.x, m_vecPos.y, new Vector2D(1,1));
        AppManager.getInstance().getCurGameState().m_lstObject[GameState.OBJ_BULLET_ENEMY].add(obj);

        obj = new Bullet(false, m_vecPos.x, m_vecPos.y, new Vector2D(1,0));
        AppManager.getInstance().getCurGameState().m_lstObject[GameState.OBJ_BULLET_ENEMY].add(obj);

        obj = new Bullet(false, m_vecPos.x, m_vecPos.y, new Vector2D(1,-1));
        AppManager.getInstance().getCurGameState().m_lstObject[GameState.OBJ_BULLET_ENEMY].add(obj);

        obj = new Bullet(false, m_vecPos.x, m_vecPos.y, new Vector2D(0,-1));
        AppManager.getInstance().getCurGameState().m_lstObject[GameState.OBJ_BULLET_ENEMY].add(obj);

        obj = new Bullet(false, m_vecPos.x, m_vecPos.y, new Vector2D(-1,-1));
        AppManager.getInstance().getCurGameState().m_lstObject[GameState.OBJ_BULLET_ENEMY].add(obj);

        obj = new Bullet(false, m_vecPos.x, m_vecPos.y, new Vector2D(-1,0));
        AppManager.getInstance().getCurGameState().m_lstObject[GameState.OBJ_BULLET_ENEMY].add(obj);

        obj = new Bullet(false, m_vecPos.x, m_vecPos.y, new Vector2D(-1,1));
        AppManager.getInstance().getCurGameState().m_lstObject[GameState.OBJ_BULLET_ENEMY].add(obj);

    }
    public void Attack_Player(){
        Log.d("attack:", "player");

        //미사일 발사 로직 (enemy이므로 _isPlayer인자는 false)
        //플레이어 위치에 따라 방향벡터 다르게 처리
        Vector2D playerPos = null;
        if(AppManager.getInstance().m_player!=null) {
            playerPos = new Vector2D(AppManager.getInstance().m_player.getPosition());
        }
        Vector2D dir = m_vecPos.getDirection(playerPos);       //enemy에서 바라보는 player방향 단위벡터

        GameObject obj = new Bullet(false, m_vecPos.x, m_vecPos.y, dir);
        AppManager.getInstance().getCurGameState().m_lstObject[GameState.OBJ_BULLET_ENEMY].add(obj);

        obj = new Bullet(false, m_vecPos.x-50, m_vecPos.y-50, dir);
        AppManager.getInstance().getCurGameState().m_lstObject[GameState.OBJ_BULLET_ENEMY].add(obj);

        obj = new Bullet(false, m_vecPos.x+50, m_vecPos.y+50, dir);
        AppManager.getInstance().getCurGameState().m_lstObject[GameState.OBJ_BULLET_ENEMY].add(obj);

        obj = new Bullet(false, m_vecPos.x+50, m_vecPos.y+50, new Vector2D(dir.x, 0));
        AppManager.getInstance().getCurGameState().m_lstObject[GameState.OBJ_BULLET_ENEMY].add(obj);

        obj = new Bullet(false, m_vecPos.x+50, m_vecPos.y+50, new Vector2D(0, dir.y));
        AppManager.getInstance().getCurGameState().m_lstObject[GameState.OBJ_BULLET_ENEMY].add(obj);
    }

    private void CreateJumpEffect() {
        // 점프 -> 바닥 착지후 -> 이펙트 출력
        GameObject object = new Effect(AppManager.getInstance().getBitmap(R.drawable.effect_boss_jump),
                AppManager.getInstance().getBitmapWidth(R.drawable.effect_boss_jump),
                AppManager.getInstance().getBitmapHeight(R.drawable.effect_boss_jump),
                m_vecPos.x - 20, m_vecPos.y - 70, 20, 16, false);

        // Object 뒤에 렌더링 되도록 OBJ_BACK_EFFECT 에 추가함(OBJ_EFFECT 렌더링 순서가 다름)
        AppManager.getInstance().getCurGameState().m_lstObject[OBJ_BACK_EFFECT].add(object);
    }
    private void CreateDieEffect(){
        //hp<=0이 되어 죽을 경우 이펙트 출력
        GameObject object = new Effect(AppManager.getInstance().getBitmap(R.drawable.effect_boss_die),
                AppManager.getInstance().getBitmapWidth(R.drawable.effect_boss_die),
                AppManager.getInstance().getBitmapHeight(R.drawable.effect_boss_die),
                m_vecPos.x - 20, m_vecPos.y - 70, 20, 16, false);

        // Object 뒤에 렌더링 되도록 OBJ_BACK_EFFECT 에 추가함(OBJ_EFFECT 렌더링 순서가 다름)
        AppManager.getInstance().getCurGameState().m_lstObject[OBJ_EFFECT].add(object);

    }

    @Override
    public void OnCollision(GameObject object, int objID) {
        switch (objID) {
            //플레이어 공격과 충돌 시 체력 감소
            case GameState.OBJ_BOMB_PLAYER:
                m_hp-=3;    //폭탄일 경우 3 감소
                if(m_hp <=0)
                    m_isDead = true;
                break;
            case GameState.OBJ_BULLET_PLAYER:
                --m_hp;     //총알일 경우 1 감소
                if(m_hp <= 0){
                    m_isDead = true;
                    CreateDieEffect();
                }

                break;
        }
        Log.d("Boss HP:",m_hp+"");
    }
}
