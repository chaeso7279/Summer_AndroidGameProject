package com.mobileisaccframework.Manager;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.mobileisaccframework.R;

import java.util.HashMap;

public class SoundManager {
    // BGM ID
    public static int BGM_INTRO = 0;
    public static int BGM_STAGE = 1;
    public static int BGM_BOSS = 2;
    public static int BGM_CLEAR = 3;
    public static int BGM_DEAD = 4;
    public static int BGM_END = 5;

    // 배경음악용 MediaPlayer;
    private MediaPlayer[] m_bgm = new MediaPlayer[BGM_END];
    private MediaPlayer m_curBGM;

    // 이펙트용 SoundPool
    private SoundPool m_SoundPool;
    private HashMap m_SoundPoolMap;
    private AudioManager m_AudioManager;
    private Context m_context;

    //초기화
    public void Initialize(Context _context){
        m_context = _context;

        // 이펙트용 SoundPool 초기화
        m_SoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        m_SoundPoolMap = new HashMap();
        m_AudioManager = (AudioManager)_context.getSystemService(Context.AUDIO_SERVICE);

        LoadBGMSound();
        LoadEffectSound();
    }

    // 이펙트 사운드 추가
    public void AddEffectSound(int _index, int _soundID){
        int id = m_SoundPool.load(m_context, _soundID, 1); //사운드를 로드
        m_SoundPoolMap.put(_index, id); //해시맵에 아이디 값을 받아온 인덱스 저장
    }

    // 이펙트 사운드 재생
    public void PlayEffectSound(int _index){
        float streamVolume = m_AudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        streamVolume = streamVolume / m_AudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        m_SoundPool.play((Integer) m_SoundPoolMap.get(_index), streamVolume, streamVolume, 1, 0, 1f);
    }

    // 배경음악 재생
    public void PlayBGM(int bgmID){
        if(m_curBGM != null) // 현재 재생중인 배경음악이 있으면 꺼줌
            m_curBGM.stop();

        m_bgm[bgmID].setLooping(true);
        m_bgm[bgmID].start();

        m_curBGM =  m_bgm[bgmID];
    }

    public void LoadBGMSound() {
        m_bgm[BGM_INTRO] = MediaPlayer.create(m_context, R.raw.intro_back);
        m_bgm[BGM_STAGE] = MediaPlayer.create(m_context, R.raw.stage_back);
        m_bgm[BGM_BOSS] = MediaPlayer.create(m_context, R.raw.bgm_boss);
        m_bgm[BGM_CLEAR] = MediaPlayer.create(m_context, R.raw.bgm_clear);
        m_bgm[BGM_DEAD] = MediaPlayer.create(m_context, R.raw.bgm_died);
    }

    public void LoadEffectSound() {

    }

    // 싱글톤
    private static SoundManager m_instance;
    public static SoundManager getInstance() {
        if(m_instance == null)
            m_instance = new SoundManager();
        return m_instance;
    }

}
