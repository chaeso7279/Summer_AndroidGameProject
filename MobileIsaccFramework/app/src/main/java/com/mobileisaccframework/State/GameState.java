package com.mobileisaccframework.State;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.GameObject.MapObject.BlockObject;
import com.mobileisaccframework.GameObject.MapObject.FireObject;
import com.mobileisaccframework.GameObject.enemy.Enemy_1;
import com.mobileisaccframework.GameObject.enemy.Enemy_2;
import com.mobileisaccframework.GameObject.player.Player;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.R;

import java.util.ArrayList;

public abstract class GameState {       // 교수님 코드에서의 IState
    // 오브젝트 구분 번호
    public static int OBJ_MAP = 0;
    public static int OBJ_PLAYER = 1;
    public static int OBJ_ENEMY = 2;
    public static int OBJ_BULLET_PLAYER = 3;
    public static int OBJ_BULLET_ENEMY = 4;
    public static int OBJ_EFFECT = 5;
    public static int OBJ_UI = 6;
    public static int OBJ_END = 7;


    public ArrayList<GameObject>[] m_lstObject;

    public ArrayList<GameObject> GetObjectList(int objectID) {
        // 리스트가 비었거나 리스트가 만들어지지 않았으면 null 리턴
        if(m_lstObject[objectID].isEmpty() || m_lstObject[objectID] == null)
            return null;

        return m_lstObject[objectID];
    }

    public void Initialize() {
        m_lstObject = new ArrayList[OBJ_END];
        for(int i = 0; i < OBJ_END; ++i)
            m_lstObject[i] = new ArrayList<GameObject>();
    }

    public abstract void Update(long _gameTime);
    public void Render(Canvas canvas) {
        if(canvas == null)
            return;
        canvas.drawColor(Color.BLACK);
    }

    public abstract void AddObject();
    public void CheckCollision() {}

    public abstract boolean onKeyDown(int _keyCode, KeyEvent _event);
    public abstract boolean onTouchEvent(MotionEvent event);

    public abstract void Destroy();

    //객체생성메소드
    //배경
    public GameObject CreateBackground(int _x, int _y){
        GameObject backgroundObject = new GameObject(AppManager.getInstance().getBitmap(R.drawable.stage_background),
                AppManager.getInstance().getBitmapWidth(R.drawable.stage_background),
                AppManager.getInstance().getBitmapHeight(R.drawable.stage_background),
                _x, _y);
        return backgroundObject;
    }

    //플레이어
    public GameObject CreatePlayer(int _x, int _y){
        GameObject playerObject = new Player(AppManager.getInstance().getBitmap(R.drawable.player_idle_front),
                AppManager.getInstance().getBitmapWidth(R.drawable.player_idle_front),
                AppManager.getInstance().getBitmapHeight(R.drawable.player_idle_front),
                _x, _y, 2, 1, true);
        return playerObject;
    }
    //몬스터
    public GameObject CreateEnemy_1(int _x, int _y){
        GameObject enemyObject = new Enemy_1(AppManager.getInstance().getBitmap(R.drawable.enemy1_front),
                AppManager.getInstance().getBitmapWidth(R.drawable.enemy1_front),
                AppManager.getInstance().getBitmapHeight(R.drawable.enemy1_front),
                _x,_y,5,4,true);
        return enemyObject;
    }
    public GameObject CreateEnemy_2(int _x, int _y){
        GameObject enemyObject = new Enemy_2(AppManager.getInstance().getBitmap(R.drawable.enemy1_front),
                AppManager.getInstance().getBitmapWidth(R.drawable.enemy1_front),
                AppManager.getInstance().getBitmapHeight(R.drawable.enemy1_front),
                _x,_y,5,4,true);
        return enemyObject;
    }
    public GameObject CreateBoss(int _x, int _y){
        GameObject enemyObject = new Enemy_2(AppManager.getInstance().getBitmap(R.drawable.enemy1_front),
                AppManager.getInstance().getBitmapWidth(R.drawable.enemy1_front),
                AppManager.getInstance().getBitmapHeight(R.drawable.enemy1_front),
                _x,_y,5,4,true);
        return enemyObject;
    }


    //문
    public GameObject CreateDoor(int _x, int _y){
        GameObject doorObject = new GameObject(AppManager.getInstance().getBitmap(R.drawable.golddoor_right),
                AppManager.getInstance().getBitmapWidth(R.drawable.golddoor_right),
                AppManager.getInstance().getBitmapHeight(R.drawable.golddoor_right),
                _x, _y, 1, 2, true);
        return doorObject;
    }
    //불꽃
    public GameObject CreateFire(int _x, int _y){
        GameObject fireObject = new FireObject(AppManager.getInstance().getBitmap(R.drawable.effect_fire),
                AppManager.getInstance().getBitmapWidth(R.drawable.effect_fire),
                AppManager.getInstance().getBitmapHeight(R.drawable.effect_fire),
                _x, _y, 20, 6, true);
        return fireObject;
    }
    //블록
    public GameObject CreateBlock(int _x, int _y){
        GameObject blockObject = new BlockObject(AppManager.getInstance().getBitmap(R.drawable.rocks_basement),
                AppManager.getInstance().getBitmapWidth(R.drawable.rocks_basement),
                AppManager.getInstance().getBitmapHeight(R.drawable.rocks_basement),
                _x, _y);
        return blockObject;
    }
}
