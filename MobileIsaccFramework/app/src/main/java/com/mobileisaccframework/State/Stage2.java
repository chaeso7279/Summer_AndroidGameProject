package com.mobileisaccframework.State;

import android.graphics.Canvas;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.GameObject.MapObject.BlockObject;
import com.mobileisaccframework.GameObject.MapObject.FireObject;
import com.mobileisaccframework.GameObject.enemy.Enemy_2;
import com.mobileisaccframework.GameObject.player.Player;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.R;

public class Stage2 extends GameState {
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

        for(int i = 0; i < OBJ_END; ++i) {          // 반복자 하나로 모든 오브젝트 접근 -> iterator 패턴사용
            for(GameObject obj : m_lstObject[i])
                obj.Render(canvas);
        }
    }

    @Override
    public void AddObject() {
        GameObject object = null;

        //배경
        object =  new GameObject(AppManager.getInstance().getBitmap(R.drawable.stage_background),
                AppManager.getInstance().getBitmapWidth(R.drawable.stage_background),
                AppManager.getInstance().getBitmapHeight(R.drawable.stage_background),
                166, 26);

        m_background = object;

        // 문
        object = new GameObject(AppManager.getInstance().getBitmap(R.drawable.golddoor_right),
                AppManager.getInstance().getBitmapWidth(R.drawable.golddoor_front),
                AppManager.getInstance().getBitmapHeight(R.drawable.golddoor_front),
                1520, 82, 1, 2, true);

        m_door = object;

        // 플레이어
        object = new Player(AppManager.getInstance().getBitmap(R.drawable.player_idle_front),
                AppManager.getInstance().getBitmapWidth(R.drawable.player_idle_front),
                AppManager.getInstance().getBitmapHeight(R.drawable.player_idle_front),
                1200, 700, 2, 2, true);

        m_lstObject[OBJ_PLAYER].add(object);

        //몬스터
        object = new Enemy_2(AppManager.getInstance().getBitmap(R.drawable.enemy2_front),
                AppManager.getInstance().getBitmapWidth(R.drawable.enemy2_front),
                AppManager.getInstance().getBitmapHeight(R.drawable.enemy2_front),
                1000,230,5,2,true);

        m_lstObject[OBJ_ENEMY].add(object);

        // 불꽃
        CreateFire(545, 682);
        CreateFire(920, 982);
        CreateFire(1220,382);
        CreateFire(1670,907);
        CreateFire(1745,382);

        // 블록
        CreateBlock(770,607);
        CreateBlock(1370,757);
        CreateBlock(1820,532);


    }

    @Override
    public boolean onKeyDown(int _keyCode, KeyEvent _event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public void Destroy() {
    }

    public GameObject CreateFire(int x, int y){
        GameObject fireposition = new FireObject(AppManager.getInstance().getBitmap(R.drawable.effect_fire),
                AppManager.getInstance().getBitmapWidth(R.drawable.effect_fire),
                AppManager.getInstance().getBitmapHeight(R.drawable.effect_fire),
                x, y, 20, 6, true);
        m_lstObject[OBJ_MAP].add(fireposition);
        return fireposition;
    }

    public GameObject CreateBlock(int x, int y){
        GameObject blockposition = new BlockObject(AppManager.getInstance().getBitmap(R.drawable.rocks_basement),
                AppManager.getInstance().getBitmapWidth(R.drawable.rocks_basement),
                AppManager.getInstance().getBitmapHeight(R.drawable.rocks_basement),
                x, y);
        m_lstObject[OBJ_MAP].add(blockposition);
        return blockposition;
    }


}
