package com.mobileisaccframework.State;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.GameObject.MapObject.BlockObject;
import com.mobileisaccframework.GameObject.MapObject.FireObject;
import com.mobileisaccframework.GameObject.enemy.Enemy_1;
import com.mobileisaccframework.GameObject.player.Player;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.R;

public class Stage1 extends GameState {


    GameObject m_background;
    GameObject m_door;

    @Override
    public void Initialize() {
        super.Initialize();

        AddObject();
    }

    @Override
    public void Update(long _gameTime) {
        m_background.Update(_gameTime);
        m_door.Update(_gameTime);

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
        m_background.Render(canvas);
        m_door.Render(canvas);

        for(int i = 0; i < OBJ_END; ++i) {          // 반복자 하나로 모든 오브젝트 접근 -> iterator 패턴사용
            for(GameObject obj : m_lstObject[i])
                obj.Render(canvas);
        }
    }

    @Override
    public void AddObject() {
        GameObject object = null;

        //배경
        object =  new GameObject(AppManager.getInstance().getBitmap(R.drawable.mapbackground),
                AppManager.getInstance().getBitmapWidth(R.drawable.mapbackground),
                AppManager.getInstance().getBitmapHeight(R.drawable.mapbackground),
                170, 82);
        m_background = object;

        // 문
        object = new GameObject(AppManager.getInstance().getBitmap(R.drawable.golddoor_right),
                AppManager.getInstance().getBitmapWidth(R.drawable.golddoor_right),
                AppManager.getInstance().getBitmapHeight(R.drawable.golddoor_right),
                2045, 532, 1, 2, true);
        m_door = object;

        // 플레이어
        object = new Player(AppManager.getInstance().getBitmap(R.drawable.player_idle_front),
                AppManager.getInstance().getBitmapWidth(R.drawable.player_idle_front),
                AppManager.getInstance().getBitmapHeight(R.drawable.player_idle_front),
                1500, 800, 2, 2, true);

        m_lstObject[OBJ_PLAYER].add(object);

        //몬스터
        object = new Enemy_1(AppManager.getInstance().getBitmap(R.drawable.enemy1_front),
                AppManager.getInstance().getBitmapWidth(R.drawable.enemy1_front),
                AppManager.getInstance().getBitmapHeight(R.drawable.enemy1_front),
                1500,800,5,4,true);

        m_lstObject[OBJ_ENEMY].add(object);

        // 불꽃
        FirePosition(620, 532);
        FirePosition(995, 682);
        FirePosition(1520,457);
        FirePosition(1595,982);

        // 블록
        BlockPosition(1970,532);

    }

    @Override
    public boolean onKeyDown(int _keyCode, KeyEvent _event) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public void Destroy() {

    }

    public GameObject FirePosition(int x, int y){
        GameObject fireposition = new FireObject(AppManager.getInstance().getBitmap(R.drawable.effect_fire),
                AppManager.getInstance().getBitmapWidth(R.drawable.effect_fire),
                AppManager.getInstance().getBitmapHeight(R.drawable.effect_fire),
                x, y, 10, 6, true);
        m_lstObject[OBJ_MAP].add(fireposition);
        return fireposition;
    }

    public GameObject BlockPosition(int x, int y){
        GameObject blockposition = new BlockObject(AppManager.getInstance().getBitmap(R.drawable.rocks_basement),
                AppManager.getInstance().getBitmapWidth(R.drawable.rocks_basement),
                AppManager.getInstance().getBitmapHeight(R.drawable.rocks_basement),
                x, y);
        m_lstObject[OBJ_MAP].add(blockposition);
        return blockposition;
    }
}
