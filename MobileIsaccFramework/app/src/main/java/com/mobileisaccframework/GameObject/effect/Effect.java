package com.mobileisaccframework.GameObject.effect;

import android.graphics.Bitmap;

import com.mobileisaccframework.GameObject.GameObject;

public class Effect extends GameObject {
        public Effect() {}
        public Effect(Bitmap bitmap, int _imgWidth, int _imgHeight, int _posX, int _posY, int _fps, int _frameCnt, boolean _isLoop){
            super(bitmap, _imgWidth, _imgHeight, _posX, _posY, _fps, _frameCnt, _isLoop);
        }

        @Override
        public int Update(long _gameTime) {
            if(m_objectState.IsPlay() == false) // 애니메이션 재생이 멈췄을 경우
                m_isDead = true; // 객체 삭제

            return super.Update(_gameTime);
        }
}

