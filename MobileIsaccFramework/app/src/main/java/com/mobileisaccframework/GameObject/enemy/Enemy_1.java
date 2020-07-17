package com.mobileisaccframework.GameObject.enemy;

import android.graphics.Bitmap;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.GameObject.GameObjectState;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.R;
import com.mobileisaccframework.Vector2D;

public class Enemy_1 extends GameObject {
    public static final int IDLE_FRONT = 0;
    public static final int WALK_FRONT = 1;
    public static final int WALK_LEFT = 2;
    public static final int WALK_RIGHT = 3;
    public static final int WALK_BACK = 4;
    public static final int STATE_END = 5;

    protected int m_speed;
    protected int m_hp;

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
    }

    //매 프레임 실행
    @Override
    public int Update(long _gameTime){
        move();
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

    public void move(){
        Vector2D enemyPos = new Vector2D(this.getPosition());
        Vector2D playerPos = new Vector2D(AppManager.getInstance().m_player.getPosition());
        Vector2D dir = enemyPos.getDirection(playerPos);

        int dist = enemyPos.getDistance(playerPos);
        if(dist< 300 ){
            //플레이어와 일정 거리만큼 가까워지면 멈춤
            ChangeState(IDLE_FRONT);
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
        else if(dir.x == 0){
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

}
