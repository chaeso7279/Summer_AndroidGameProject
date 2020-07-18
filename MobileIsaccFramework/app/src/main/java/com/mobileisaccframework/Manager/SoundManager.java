package com.mobileisaccframework.Manager;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

public class SoundManager {

    private SoundPool m_SoundPool;
    private HashMap m_SoundPoolMap;
    private AudioManager m_AudioManager;
    private Context m_context;

    //초기화
    public void Initialize(Context _context){
        m_context = _context;
        m_SoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        m_SoundPoolMap = new HashMap();
        m_AudioManager = (AudioManager)_context.getSystemService(Context.AUDIO_SERVICE);

    }

    //음원을 추가하는 메소드
    public void AddSound(int _index, int _soundID){
        int id = m_SoundPool.load(m_context, _soundID, 1); //사운드를 로드
        m_SoundPoolMap.put(_index, id); //해시맵에 아이디 값을 받아온 인덱스 저장
    }

    //음원 재생하는 메소드
    public void Play(int _index){
        float streamVolume = m_AudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        streamVolume = streamVolume / m_AudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        m_SoundPool.play((Integer) m_SoundPoolMap.get(_index), streamVolume, streamVolume, 1, 0, 1f);
    }

    //음원 무한 재생하는 메소드
    public void PlayLooped(int _index){
        float streamVolume = m_AudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        streamVolume = streamVolume/m_AudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        m_SoundPool.play((Integer) m_SoundPoolMap.get(_index), streamVolume, streamVolume, 1, -1, 1f);
    }

    // 싱글톤
    private static SoundManager m_instance;
    public static SoundManager getInstance() {
        if(m_instance == null)
            m_instance = new SoundManager();
        return m_instance;
    }

}
