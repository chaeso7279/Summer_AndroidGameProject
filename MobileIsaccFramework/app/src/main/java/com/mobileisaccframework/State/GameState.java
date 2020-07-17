package com.mobileisaccframework.State;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.GameObject.MapObject.BlockObject;
import com.mobileisaccframework.GameObject.MapObject.FireObject;
import com.mobileisaccframework.GameObject.MapObject.MapObject;
import com.mobileisaccframework.GameObject.enemy.Enemy_1;
import com.mobileisaccframework.GameObject.enemy.Enemy_2;
import com.mobileisaccframework.GameObject.enemy.Enemy_Boss;
import com.mobileisaccframework.GameObject.player.Player;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.Manager.CollisionManager;
import com.mobileisaccframework.Pad;
import com.mobileisaccframework.R;

import java.util.ArrayList;

public abstract class GameState {       // 교수님 코드에서의 IState
    // 오브젝트 구분 번호
    public static int OBJ_MAP = 0;
    public static int OBJ_PLAYER = 1;
    public static int OBJ_ENEMY = 2;
    public static int OBJ_BULLET_PLAYER = 3;
    public static int OBJ_BOMB_PLAYER = 4;
    public static int OBJ_BULLET_ENEMY = 5;
    public static int OBJ_EFFECT = 6;
    public static int OBJ_UI = 7;
    public static int OBJ_END = 8;

    // 배경
    protected GameObject m_backGround;
    // 방향키 패드
    Pad m_pad;

    // 초기화 실행 여부
    protected boolean m_isInit = false;

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

        AddObject();
        m_isInit = true;
    }

    public abstract void Update(long _gameTime);
    public void Render(Canvas canvas) {
        if(canvas == null)
            return;
        canvas.drawColor(Color.BLACK);
    }

    public abstract void AddObject();
    public void CheckCollision() {
        // 예시 (플레이어 - 적)

        for(GameObject dstObj : m_lstObject[OBJ_ENEMY]) {
            if(CollisionManager.CheckCollision(AppManager.m_player.getBoundBox(), dstObj.getBoundBox())) {
                AppManager.m_player.OnCollision(dstObj, OBJ_ENEMY);
                dstObj.OnCollision(AppManager.m_player, OBJ_PLAYER);
            }
        }

        //플레이어 불릿 - 불꽃
        for(GameObject srcObj : m_lstObject[OBJ_BULLET_PLAYER]){
            for(GameObject dstObj : m_lstObject[OBJ_MAP]) {
                if(CollisionManager.CheckCollision(srcObj.getBoundBox(), dstObj.getBoundBox())) {
                    srcObj.OnCollision(dstObj, OBJ_MAP);
                    // 맵 오브젝트가 불꽃일때만 맵 오브젝트에 충돌 여부 전달
                    if(((MapObject)dstObj).GetMapObjectType() == MapObject.MAP_FIRE)
                        dstObj.OnCollision(srcObj, OBJ_BULLET_PLAYER);
                }
            }
        }

        //플레이어 폭탄 - 블럭
        for(GameObject srcObj : m_lstObject[OBJ_BOMB_PLAYER]){
            for(GameObject dstObj : m_lstObject[OBJ_MAP]) {
                if(CollisionManager.CheckCollision(srcObj.getBoundBox(), dstObj.getBoundBox())) {
                    // 맵 오브젝트가 블럭일때만 맵 오브젝트에 충돌 여부 전달
                    if(((MapObject)dstObj).GetMapObjectType() == MapObject.MAP_BLOCK)
                        dstObj.OnCollision(srcObj, OBJ_BOMB_PLAYER);
                }
            }
        }
    }

    //객체생성메소드
    //배경
    public void CreateBackground(int _x, int _y){
        m_backGround = new GameObject(AppManager.getInstance().getBitmap(R.drawable.stage_background),
                AppManager.getInstance().getBitmapWidth(R.drawable.stage_background),
                AppManager.getInstance().getBitmapHeight(R.drawable.stage_background),
                _x, _y);
    }

    //플레이어
    public GameObject CreatePlayer(int _x, int _y){
        GameObject playerObject = new Player(AppManager.getInstance().getBitmap(R.drawable.player_idle_front),
                AppManager.getInstance().getBitmapWidth(R.drawable.player_idle_front),
                AppManager.getInstance().getBitmapHeight(R.drawable.player_idle_front),
                _x, _y, 2, 1, true);

        m_lstObject[OBJ_PLAYER].add(playerObject);
        return playerObject;
    }
    //몬스터
    public GameObject CreateEnemy_1(int _x, int _y){
        GameObject enemyObject = new Enemy_1(AppManager.getInstance().getBitmap(R.drawable.enemy1_front),
                AppManager.getInstance().getBitmapWidth(R.drawable.enemy1_front),
                AppManager.getInstance().getBitmapHeight(R.drawable.enemy1_front),
                _x,_y,5,4,true);

        m_lstObject[OBJ_ENEMY].add(enemyObject);
        return enemyObject;
    }
    public GameObject CreateEnemy_2(int _x, int _y){
        GameObject enemyObject = new Enemy_2(AppManager.getInstance().getBitmap(R.drawable.enemy2_front),
                AppManager.getInstance().getBitmapWidth(R.drawable.enemy2_front),
                AppManager.getInstance().getBitmapHeight(R.drawable.enemy2_front),
                _x,_y,5,4,true);

        m_lstObject[OBJ_ENEMY].add(enemyObject);
        return enemyObject;
    }
    public GameObject CreateBoss(int _x, int _y){
        GameObject enemyObject = new Enemy_Boss(AppManager.getInstance().getBitmap(R.drawable.boss_idle),
                AppManager.getInstance().getBitmapWidth(R.drawable.boss_idle),
                AppManager.getInstance().getBitmapHeight(R.drawable.boss_idle),
                _x,_y,5,3,true);

        m_lstObject[OBJ_ENEMY].add(enemyObject);
        return enemyObject;
    }

    //문
    public GameObject CreateDoor(int _x, int _y, int _rID){
        GameObject doorObject = new GameObject(AppManager.getInstance().getBitmap(_rID),
                AppManager.getInstance().getBitmapWidth(_rID),
                AppManager.getInstance().getBitmapHeight(_rID),
                _x, _y, 1, 2, false);
        return doorObject;
    }
    //불꽃
    public GameObject CreateFire(int _x, int _y){
        GameObject fireObject = new FireObject(MapObject.MAP_FIRE,
                AppManager.getInstance().getBitmap(R.drawable.effect_fire),
                AppManager.getInstance().getBitmapWidth(R.drawable.effect_fire),
                AppManager.getInstance().getBitmapHeight(R.drawable.effect_fire),
                _x, _y, 20, 6, true);

        m_lstObject[OBJ_MAP].add(fireObject);
        return fireObject;
    }
    //블록
    public GameObject CreateBlock(int _x, int _y){
        GameObject blockObject = new BlockObject(MapObject.MAP_BLOCK,
                AppManager.getInstance().getBitmap(R.drawable.rocks_basement),
                AppManager.getInstance().getBitmapWidth(R.drawable.rocks_basement),
                AppManager.getInstance().getBitmapHeight(R.drawable.rocks_basement),
                _x, _y);

        m_lstObject[OBJ_MAP].add(blockObject);
        return blockObject;
    }

    public void CreateUI() {
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
    }

    public abstract boolean onKeyDown(int _keyCode, KeyEvent _event);
    public abstract boolean onTouchEvent(MotionEvent event);

    public abstract void Destroy();
}
