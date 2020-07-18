package com.mobileisaccframework.GameObject.enemy;

import android.graphics.Bitmap;
import android.util.Log;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.GameObject.GameObjectState;
import com.mobileisaccframework.GameObject.bullet.Bullet;
import com.mobileisaccframework.GameObject.effect.Effect;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.R;
import com.mobileisaccframework.State.GameState;
import com.mobileisaccframework.Vector2D;

import java.util.Random;

import static com.mobileisaccframework.State.GameState.OBJ_EFFECT;

public class Enemy_1 extends GameObject {
    public static final int IDLE_FRONT = 0;
    public static final int WALK_FRONT = 1;
    public static final int WALK_LEFT = 2;
    public static final int WALK_RIGHT = 3;
    public static final int WALK_BACK = 4;
    public static final int STATE_END = 5;

    protected int m_speed;
    protected int m_hp;

    private long m_lastShoot = System.currentTimeMillis();
    private long m_lastStop = System.currentTimeMillis();

    private boolean m_isStop = true;


    public Enemy_1(Bitmap bitmap, int _imgWidth, int _imgHeight, int _fps, int _frameCnt, boolean _isLoop) {
        super(bitmap, _imgWidth, _imgHeight, _fps, _frameCnt, _isLoop);
    }

    public Enemy_1(Bitmap bitmap, int _imgWidth, int _imgHeight, int _posX, int _posY, int _fps, int _frameCnt, boolean _isLoop) {
        super(bitmap, _imgWidth, _imgHeight, _posX, _posY, _fps, _frameCnt, _isLoop);
    }

    //초기 데이터 설정
    @Override
    public void Initialize(){
        super.Initialize();

        //초기 state 설정
        m_curState = IDLE_FRONT;

        //프레임 개수 설정
        m_arrFrameCnt = new int[STATE_END];

        //모두 4임
        for(int i = IDLE_FRONT; i < STATE_END; ++i)
            m_arrFrameCnt[i] = 4;

        //이동 속도 설정
        m_speed = 2;

        //hp 설정
        m_hp = 3;
    }

    //매 프레임 실행
    @Override
    public int Update(long _gameTime){
        Move();
        Attack();
        return super.Update(_gameTime);
    }

    @Override
    public void ChangeState(int _state){
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
            case WALK_FRONT:
                rID = R.drawable.enemy1_front;
                fps = 5;
                break;
            case WALK_LEFT:
                rID = R.drawable.enemy1_left;
                fps = 5;
                break;
            case WALK_RIGHT:
                rID = R.drawable.enemy1_right;
                fps = 5;
                break;
            case WALK_BACK:
                rID = R.drawable.enemy1_back;
                fps = 5;
                break;
        }

        // AppManager 로부터 비트맵 가져옴
        Bitmap bitmap = AppManager.getInstance().getBitmap(rID);

        // m_arrFrameCnt가 각 상태마다의 프레임 개수니까
        // 다음 코드는 (이미지 크기 / 프레임 개수) 와 같음
        int width = (AppManager.getInstance().getBitmapWidth(rID) * 4) / m_arrFrameCnt[_state];
        int height = AppManager.getInstance().getBitmapHeight(rID) * 4;

        // 오브젝트 스테이트 지정
        m_objectState = new GameObjectState(this,bitmap, width, height,
                fps, m_arrFrameCnt[_state], isLoop);
        m_objectState.Initialize();

        // 이건 단순히 오브젝트 스테이트를 숫자로 쓰는 용도
        m_curState = _state;
    }

    public void Move(){
        //5초에 한 번 3초씩 멈춤
        //멈춤 시작
        if(System.currentTimeMillis() - m_lastStop >= 8000) {
            m_lastStop = System.currentTimeMillis();
            m_isStop = true;
        }
        //3초 동안 멈춤
        if(m_isStop && System.currentTimeMillis() - m_lastStop <= 3000){
            ChangeState(IDLE_FRONT);
            return;
        }
        else{
            m_isStop = false;
        }

        Vector2D playerPos = null;
        if(AppManager.getInstance().m_player!=null) {
            playerPos = new Vector2D(AppManager.getInstance().m_player.getPosition());
        }
        Vector2D dir = m_vecPos.getDirection(playerPos);

        int dist = m_vecPos.getDistance(playerPos);
        if(dist< 300){
            //플레이어와 일정 거리만큼 가까워지면 멈춤
            ChangeState(IDLE_FRONT);
            m_lastStop = System.currentTimeMillis();
            return;
        }
        if(dir.x>0){
            //오른쪽으로 이동
            ChangeState(WALK_RIGHT);
        }
        else if(dir.x<0){
            //왼쪽으로 이동
            ChangeState(WALK_LEFT);
        }
        else{/* dir.x == 0 */
             if(dir.y<0){
                 //위로 이동
                 ChangeState(WALK_BACK);
             }
             else if(dir.y>0){
                 //아래로 이동
                 ChangeState(WALK_FRONT);
             }
         }
        this.setPosition(this.getPosition().x + dir.x * m_speed,this.getPosition().y + dir.y * m_speed);
    }

    public void Attack(){
        //공격하는 로직

        //3~5초에 한번씩 공격
        Random rand = new Random();
        int randInt = rand.nextInt(2) + 3;

        if(System.currentTimeMillis() - m_lastShoot >= randInt * 1000){
            m_lastShoot = System.currentTimeMillis();

            //미사일 발사 로직 (enemy이므로 _isPlayer인자는 false)
            //플레이어 위치에 따라 방향벡터 다르게 처리
            Vector2D playerPos = null;
            if(AppManager.getInstance().m_player!=null) {
                playerPos = new Vector2D(AppManager.getInstance().m_player.getPosition());
            }
            Vector2D dir = m_vecPos.getDirection(playerPos);       //enemy에서 바라보는 player방향 단위벡터

            GameObject obj = new Bullet(false, m_vecPos.x, m_vecPos.y, dir);

            AppManager.getInstance().getCurGameState().m_lstObject[GameState.OBJ_BULLET_ENEMY].add(obj);
        }
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
                    //CreateDieEffect();
                }
                break;
        }
        Log.d("Enemy1 HP:",m_hp+"");
    }

}
