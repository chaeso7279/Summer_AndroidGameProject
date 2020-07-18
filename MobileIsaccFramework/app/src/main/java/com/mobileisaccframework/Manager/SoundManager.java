package com.mobileisaccframework.Manager;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

public class SoundManager {

    private SoundPool m_SoundPool;  //안드로이드에서 지원하는 사운드풀
    private HashMap m_SoundPoolMap;  // 불러온 사운드의 아이디 값을 지정할 해시맵
    private AudioManager m_AudioManager;  //사운드 관리를 위한 오디오 매니저
    private Context m_Activity;  //애플리케이션의 컨텍스트 값
    private static SoundManager m_instance;

    public static SoundManager getInstance(){
        if(m_instance == null) m_instance = new SoundManager();
        return m_instance;
    }

    //초기화
    public void Initialize(Context _context){
        m_SoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        m_SoundPoolMap = new HashMap();
        m_AudioManager = (AudioManager)_context.getSystemService(Context.AUDIO_SERVICE);
        m_Activity = _context;
    }

    //음원을 추가하는 메소드
    public void addSound(int _index, int _soundID){
        int id = m_SoundPool.load(m_Activity, _soundID, 1); //사운드를 로드
        m_SoundPoolMap.put(_index, id); //해시맵에 아이디 값을 받아온 인덱스 저장
    }

    //음원 재생하는 메소드
    public void playSound(int _index){
        float streamVolume = m_AudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        streamVolume = streamVolume/m_AudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        m_SoundPool.play((Integer) m_SoundPoolMap.get(_index), streamVolume, streamVolume, 1, 0, 1f);
    }

    //음원 무한 재생하는 메소드
    public void playLooped(int _index){
        float streamVolume = m_AudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        streamVolume = streamVolume/m_AudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        m_SoundPool.play((Integer) m_SoundPoolMap.get(_index), streamVolume, streamVolume, 1, -1, 1f);
    }

    //효과음 정지
    public void stopSound(int _index){
        m_SoundPool.stop(_index);
    }

    //효과음 일시정지
    public void pauseSound(int _index){
        m_SoundPool.pause(_index);
    }

    //효과음 재시작
    public void resumeSound(int _index){
        m_SoundPool.resume(_index);
    }

}
