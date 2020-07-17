package com.mobileisaccframework.State;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.Pad;
import com.mobileisaccframework.GameObject.MapObject.BlockObject;
import com.mobileisaccframework.GameObject.MapObject.FireObject;
import com.mobileisaccframework.GameObject.MapObject.MapObject;
import com.mobileisaccframework.GameObject.player.Player;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.Manager.CollisionManager;
import com.mobileisaccframework.R;

public class StageTestState extends GameState {
    GameObject m_backGround;

    // 방향키 패드
    Pad m_pad;

    @Override
    public void Initialize() {
        super.Initialize();

        AddObject();
    }

    @Override
    public void Update(long _gameTime) {
        // 배경 업데이트
        m_backGround.Update(_gameTime);

        for(int i = 0; i < OBJ_END; ++i) {          // 반복자 하나로 모든 오브젝트 접근 -> iterator 패턴사용
            for(GameObject obj : m_lstObject[i])
                obj.Update(_gameTime);
        }
    }

    @Override
    public void Render(Canvas canvas) {
        super.Render(canvas);

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

    // 오브젝트 추가 함수
    @Override
    public void AddObject() {
        GameObject object = null;

        // 백그라운드
        object = new GameObject(AppManager.getInstance().getBitmap(R.drawable.stage_background),
                AppManager.getInstance().getBitmapWidth(R.drawable.stage_background),
                AppManager.getInstance().getBitmapHeight(R.drawable.stage_background), 166, 26);

        m_backGround = object;

        // 플레이어
        object = new Player(AppManager.getInstance().getBitmap(R.drawable.player_idle_front),
                AppManager.getInstance().getBitmapWidth(R.drawable.player_idle_front),
                AppManager.getInstance().getBitmapHeight(R.drawable.player_idle_front),
                400, 230, 2, 2, true);

        m_lstObject[OBJ_PLAYER].add(object);

        // 불꽃 1
        object = new FireObject(AppManager.getInstance().getBitmap(R.drawable.effect_fire),
                AppManager.getInstance().getBitmapWidth(R.drawable.effect_fire),
                AppManager.getInstance().getBitmapHeight(R.drawable.effect_fire),
                 620, 532, 10, 6, true);
        m_lstObject[OBJ_MAP].add(object);

        // 불꽃 2
        object = new FireObject(AppManager.getInstance().getBitmap(R.drawable.effect_fire),
                AppManager.getInstance().getBitmapWidth(R.drawable.effect_fire),
                AppManager.getInstance().getBitmapHeight(R.drawable.effect_fire),
                995, 682, 10, 6, true);
        m_lstObject[OBJ_MAP].add(object);

        // 불꽃 3
        object = new FireObject(AppManager.getInstance().getBitmap(R.drawable.effect_fire),
                AppManager.getInstance().getBitmapWidth(R.drawable.effect_fire),
                AppManager.getInstance().getBitmapHeight(R.drawable.effect_fire),
                1520, 457, 10, 6, true);
        m_lstObject[OBJ_MAP].add(object);

        // 불꽃 4
        object = new FireObject(AppManager.getInstance().getBitmap(R.drawable.effect_fire),
                AppManager.getInstance().getBitmapWidth(R.drawable.effect_fire),
                AppManager.getInstance().getBitmapHeight(R.drawable.effect_fire),
                1595, 982, 10, 6, true);
        m_lstObject[OBJ_MAP].add(object);

        // 블록
        object = new BlockObject(AppManager.getInstance().getBitmap(R.drawable.rocks_basement),
                AppManager.getInstance().getBitmapWidth(R.drawable.rocks_basement),
                AppManager.getInstance().getBitmapHeight(R.drawable.rocks_basement),
                1970, 532);
        
        m_lstObject[OBJ_MAP].add(object);

        // 패드
        m_pad = new Pad(85, AppManager.HEIGHT - 500);
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

    @Override
    public boolean onKeyDown(int _keyCode, KeyEvent _event) {
        if(_event.getAction() != KeyEvent.ACTION_DOWN)
            return true;

        if(_keyCode == KeyEvent.KEYCODE_F1)
            AppManager.getInstance().m_bRenderRect = !AppManager.getInstance().m_bRenderRect;

        if(_keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
            m_lstObject[OBJ_PLAYER].get(0).ChangeState(Player.WALK_FRONT);
        if(_keyCode == KeyEvent.KEYCODE_DPAD_UP)
            m_lstObject[OBJ_PLAYER].get(0).ChangeState(Player.WALK_BACK);
        if(_keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
            m_lstObject[OBJ_PLAYER].get(0).ChangeState(Player.WALK_LEFT);
        if(_keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
            m_lstObject[OBJ_PLAYER].get(0).ChangeState(Player.WALK_RIGHT);

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
}
