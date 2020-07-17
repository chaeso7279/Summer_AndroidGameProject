package com.mobileisaccframework.State;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.GameObject.enemy.Enemy_2;
import com.mobileisaccframework.GameObject.enemy.Enemy_Boss;
import com.mobileisaccframework.GameObject.player.Player;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.Manager.CollisionManager;
import com.mobileisaccframework.Pad;
import com.mobileisaccframework.R;

public class Stage_Boss extends GameState{
    GameObject m_background;

    // 방향키 패드
    Pad m_pad;

    @Override
    public void Initialize() {
        super.Initialize();

        AddObject();
    }
    @Override
    public void Update(long _gameTime) {
        m_background.Update(_gameTime);

        for(int i = 0; i < OBJ_END; ++i) {          // 반복자 하나로 모든 오브젝트 접근 -> iterator 패턴사용
            for (int j = 0; j < m_lstObject[i].size(); ++j) {
                GameObject obj = m_lstObject[i].get(j);

                int iEvent = obj.Update(_gameTime);
                if (iEvent == GameObject.DEAD_OBJ) { // 오브젝트가 죽었을 때
                    m_lstObject[i].remove(j);
                }
            }
        }
    }

    @Override
    public void Render(Canvas canvas) {
        super.Render(canvas);

        if(canvas == null)
            return;

        // 배경 출력
        m_background.Render(canvas);

        for(int i = 0; i < OBJ_END; ++i) {          // 반복자 하나로 모든 오브젝트 접근 -> iterator 패턴사용
            for(GameObject obj : m_lstObject[i])
                obj.Render(canvas);
        }
    }

    @Override
    public void AddObject() {
        // 패드
        m_pad = new Pad(85, AppManager.HEIGHT - 500);

        GameObject object = null;
        // 공격 버튼 UI
        object = new GameObject(AppManager.getInstance().getBitmap(R.drawable.ui_attack),
                AppManager.getInstance().getBitmapWidth(R.drawable.ui_attack),
                AppManager.getInstance().getBitmapHeight(R.drawable.ui_attack),
                2440 - (350), 1440 - (450));

        m_lstObject[OBJ_UI].add(object);
        m_pad.SetAttackUIRect(0, object.getBoundBox());

        // 폭탄 버튼 UI
        object = new GameObject(AppManager.getInstance().getBitmap(R.drawable.ui_bomb),
                AppManager.getInstance().getBitmapWidth(R.drawable.ui_bomb),
                AppManager.getInstance().getBitmapHeight(R.drawable.ui_bomb),
                2440 - (500), 1440 - (620));

        m_lstObject[OBJ_UI].add(object);
        m_pad.SetAttackUIRect(1, object.getBoundBox());

        //배경
        m_background = CreateBackground(166,26);

        // 플레이어
        CreatePlayer(1200,500);

        //몬스터
        CreateBoss(1200,500);
    }

    @Override
    public boolean onKeyDown(int _keyCode, KeyEvent _event) {
        if(_keyCode == KeyEvent.KEYCODE_1){
            m_lstObject[OBJ_ENEMY].get(0).ChangeState(Enemy_Boss.STATE_IDLE);
        }
        if(_keyCode == KeyEvent.KEYCODE_2){
            m_lstObject[OBJ_ENEMY].get(0).ChangeState(Enemy_Boss.STATE_ATTACK);
        }
        if(_keyCode == KeyEvent.KEYCODE_3){
            m_lstObject[OBJ_ENEMY].get(0).ChangeState(Enemy_Boss.STATE_JUMP);
        }
        if(_keyCode == KeyEvent.KEYCODE_4){
            m_lstObject[OBJ_ENEMY].get(0).setPosition(400,700);
        }
        if(_keyCode == KeyEvent.KEYCODE_5){
            m_lstObject[OBJ_ENEMY].get(0).setPosition(400,230);
        }
        if(_keyCode == KeyEvent.KEYCODE_6){
            m_lstObject[OBJ_ENEMY].get(0).setPosition(1400,230);
        }
        if(_event.getAction() != KeyEvent.ACTION_DOWN)
            return true;

        if(_keyCode == KeyEvent.KEYCODE_SPACE)
            AppManager.getInstance().m_bRenderRect = !AppManager.getInstance().m_bRenderRect;

        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        m_pad.OnTouchEvent(event);
        // m_lstObject[OBJ_MAP].get(0).ChangeState(FireObject.STATE_START);
        return true;
    }

    @Override
    public void Destroy() {

    }
    @Override
    public void CheckCollision() {
        // 예시 (플레이어 - 적)
        for(GameObject srcObj : m_lstObject[OBJ_PLAYER]){
            for(GameObject dstObj : m_lstObject[OBJ_ENEMY]) {
                if(CollisionManager.CheckCollision(srcObj.getBoundBox(), dstObj.getBoundBox())) {
                    srcObj.OnCollision(dstObj, OBJ_ENEMY);
                    dstObj.OnCollision(srcObj, OBJ_PLAYER);
                }
            }
        }
        // 플레이어 - 불꽃
        for(GameObject srcObj : m_lstObject[OBJ_PLAYER]){
            for(GameObject dstObj : m_lstObject[OBJ_FIRE]) {
                if(CollisionManager.CheckCollision(srcObj.getBoundBox(), dstObj.getBoundBox())) {
                    srcObj.OnCollision(dstObj, OBJ_FIRE);
                    dstObj.OnCollision(srcObj, OBJ_PLAYER);
                }
            }
        }

        // 플레이어 - 블록
        for(GameObject srcObj : m_lstObject[OBJ_PLAYER]){
            for(GameObject dstObj : m_lstObject[OBJ_BLOCK]) {
                if(CollisionManager.CheckCollision(srcObj.getBoundBox(), dstObj.getBoundBox())) {
                    srcObj.OnCollision(dstObj, OBJ_BLOCK);
                    dstObj.OnCollision(srcObj, OBJ_PLAYER);
                }
            }
        }

        //플레이어 불릿 - 불꽃
        for(GameObject srcObj : m_lstObject[OBJ_BULLET_PLAYER]){
            for(GameObject dstObj : m_lstObject[OBJ_FIRE]) {
                if(CollisionManager.CheckCollision(srcObj.getBoundBox(), dstObj.getBoundBox())) {
                    srcObj.OnCollision(dstObj, OBJ_FIRE);
                    dstObj.OnCollision(srcObj, OBJ_BULLET_PLAYER);
                }
            }
        }

        //폭탄 - 블록

    }
}
