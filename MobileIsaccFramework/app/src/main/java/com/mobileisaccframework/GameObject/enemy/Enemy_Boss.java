package com.mobileisaccframework.GameObject.enemy;

import android.graphics.Bitmap;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.GameObject.GameObjectState;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.R;
import com.mobileisaccframework.Vector2D;

public class Enemy_Boss extends GameObject {
    public static final int STATE_IDLE = 0;
    public static final int STATE_ATTACK = 1;
    public static final int STATE_JUMP = 2;
    public static final int STATE_END = 3;

    protected int m_speedX;
    protected int m_speedY;
    protected int hp;

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

        //모두 3임
        for(int i = STATE_IDLE; i < STATE_END; ++i)
            m_arrFrameCnt[i] = 3;

        //이동 속도 설정
        m_speedX = 5;
        m_speedY = -20;
    }

    //매 프레임 실행
    @Override
    public int Update(long _gameTime){
        if(m_curState == STATE_JUMP)
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
            case STATE_IDLE:
                rID = R.drawable.boss_idle;
                fps = 5;
                break;
            case STATE_ATTACK:
                rID = R.drawable.boss_attack;
                fps = 5;
                isLoop = false;
                break;
            case STATE_JUMP:
                rID = R.drawable.boss_jump_start;
                fps = 5;
                isLoop = false;
                break;
        }

        // AppManager 로부터 비트맵 가져옴
        Bitmap bitmap = AppManager.getInstance().getBitmap(rID);

        // m_arrFrameCnt가 각 상태마다의 프레임 개수니까
        // 다음 코드는 (이미지 크기 / 프레임 개수) 와 같음
        int width = (AppManager.getInstance().getBitmapWidth(rID)*4) / m_arrFrameCnt[_state];
        int height = AppManager.getInstance().getBitmapHeight(rID)*4;

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

        int afterX = enemyPos.x;
        int afterY = enemyPos.y;

        if(Math.abs(enemyPos.y - playerPos.y) > 10){
            afterX+=m_speedX*dir.x;
            afterY+=m_speedY*dir.y;

            ++m_speedY;
        }else{
            ChangeState(STATE_IDLE);
        }
        this.setPosition(afterX, afterY);

    }
}
