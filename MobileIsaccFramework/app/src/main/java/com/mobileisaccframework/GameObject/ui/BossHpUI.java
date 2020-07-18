package com.mobileisaccframework.GameObject.ui;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.mobileisaccframework.GameObject.GameObject;
import com.mobileisaccframework.Manager.AppManager;
import com.mobileisaccframework.R;
import com.mobileisaccframework.Vector2D;

public class BossHpUI extends GameObject {
    Bitmap m_bitmapfill;   // 보스 체력
    Bitmap m_bitmapEmpty;   // 보스 체력 뒷 배경

    int m_bossMaxHP = 0;
    int m_bossHP = 0;

    Rect m_imgRect;

    public BossHpUI(int _bossMaxHP, int _bossHP){
        m_bossMaxHP = _bossMaxHP;
        m_bossHP = _bossHP;

        m_bitmapfill = AppManager.getInstance().getBitmap(R.drawable.ui_bosshealth_full);
        m_bitmapEmpty = AppManager.getInstance().getBitmap(R.drawable.ui_bosshealth_back);

        m_imgWidth = AppManager.getInstance().getBitmapWidth(R.drawable.ui_bosshealth_full) * 4;
        m_imgHeight = AppManager.getInstance().getBitmapHeight(R.drawable.ui_bosshealth_full) * 4;

        m_vecPos = new Vector2D(900, 50);

        m_imgRect = new Rect(0, 0, m_imgWidth, m_imgHeight);
    }
    public void UpdateHP(int _hp) {  m_bossHP = _hp; }

    @Override
    public int Update(long _gameTime) {
        if(m_isDead)
            return DEAD_OBJ;

        return NO_EVENT;
    }

    @Override
    public void Render(Canvas canvas) {
        if(canvas == null)
            return;

        // 체력바 배경
        canvas.drawBitmap(m_bitmapEmpty, m_vecPos.x, m_vecPos.y, null);

        // 플레이어의 최대 체력으로 이미지를 나눈 것에 현재 플레이어의 체력을 곱하면
        // 플레이어의 체력만큼 하트 개수를 출력할 수 있음
        int imgRight = (m_imgWidth / m_bossMaxHP) * 2 + (((m_imgWidth / m_bossMaxHP) * 13 ) / m_bossMaxHP) *(m_bossHP);
        m_imgRect.right = imgRight;

        Rect dest = new Rect( m_vecPos.x, m_vecPos.y,
                m_vecPos.x + imgRight, m_vecPos.y + m_imgHeight);
        canvas.drawBitmap(m_bitmapfill, m_imgRect, dest , null);
    }

}
