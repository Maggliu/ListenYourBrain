package com.example.liudingming.listenyourbrain;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.HashMap;

/**
 * Created by 刘定铭 on 2017/7/5.
 */

public class SoundClass {
    private SoundPool pool;
    private HashMap<Integer, Integer> soundID = new HashMap<Integer, Integer>();
    private Context test;
    public SoundClass(Context contest){
        test=contest;
    }
    public SoundPool getPool() {//加载音乐并返回音乐池
        pool = new SoundPool(50, AudioManager.STREAM_SYSTEM, 0);
        //firstSound
        soundID.put(1, pool.load(test, R.raw.a10, 1));
        soundID.put(2, pool.load(test, R.raw.a11, 1));
        soundID.put(3, pool.load(test, R.raw.a12, 1));
        soundID.put(4, pool.load(test, R.raw.c10, 1));
        soundID.put(5, pool.load(test, R.raw.c11, 1));
        soundID.put(6, pool.load(test, R.raw.c12, 1));
        soundID.put(7, pool.load(test, R.raw.a1, 1));
        soundID.put(8, pool.load(test, R.raw.a2, 1));
        soundID.put(9, pool.load(test, R.raw.a3, 1));
        soundID.put(10, pool.load(test, R.raw.c1, 1));
        soundID.put(11, pool.load(test, R.raw.c2, 1));
        soundID.put(12, pool.load(test, R.raw.c3, 1));
        soundID.put(13, pool.load(test, R.raw.a4, 1));
        soundID.put(14, pool.load(test, R.raw.a5, 1));
        soundID.put(15, pool.load(test, R.raw.a6, 1));
        soundID.put(16, pool.load(test, R.raw.c4, 1));
        soundID.put(17, pool.load(test, R.raw.c5, 1));
        soundID.put(18, pool.load(test, R.raw.c6, 1));
        soundID.put(19, pool.load(test, R.raw.a7, 1));
        soundID.put(20, pool.load(test, R.raw.a8, 1));
        soundID.put(21, pool.load(test, R.raw.a9, 1));
        soundID.put(22, pool.load(test, R.raw.a13, 1));
        soundID.put(23, pool.load(test, R.raw.c7, 1));
        soundID.put(24, pool.load(test, R.raw.c8, 1));
        soundID.put(25, pool.load(test, R.raw.c9, 1));
        //secondSound 14D:\ListenYourBrain\app\src\main\res\music\instrument1\high\a6.wav
        soundID.put(26,pool.load(test,R.raw.v10,1));
        soundID.put(27,pool.load(test,R.raw.v11,1));
        soundID.put(28,pool.load(test,R.raw.v12,1));
        soundID.put(29,pool.load(test,R.raw.v5,1));
        soundID.put(30,pool.load(test,R.raw.v6,1));
        soundID.put(31,pool.load(test,R.raw.v7,1));
        soundID.put(32,pool.load(test,R.raw.v8,1));
        soundID.put(33,pool.load(test,R.raw.v1,1));
        soundID.put(34,pool.load(test,R.raw.v2,1));
        soundID.put(35,pool.load(test,R.raw.v3,1));
        soundID.put(36,pool.load(test,R.raw.v4,1));
        soundID.put(37,pool.load(test,R.raw.v9,1));
        soundID.put(38,pool.load(test,R.raw.v13,1));
        soundID.put(39,pool.load(test,R.raw.v14,1));
        //thirdSound 9
        soundID.put(40,pool.load(test,R.raw.i7,1));
        soundID.put(41,pool.load(test,R.raw.i8,1));
        soundID.put(42,pool.load(test,R.raw.i9,1));
        soundID.put(43,pool.load(test,R.raw.i4,1));
        soundID.put(44,pool.load(test,R.raw.i5,1));
        soundID.put(45,pool.load(test,R.raw.i6,1));
        soundID.put(46,pool.load(test,R.raw.i1,1));
        soundID.put(47,pool.load(test,R.raw.i2,1));
        soundID.put(48,pool.load(test,R.raw.i3,1));
        return pool;
    }
    public HashMap<Integer,Integer> getMap(){
        return soundID;
    }//返回音乐池加载顺序和对应加载id
}
