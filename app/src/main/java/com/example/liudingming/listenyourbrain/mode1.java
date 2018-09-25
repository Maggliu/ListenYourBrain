package com.example.liudingming.listenyourbrain;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.media.SoundPool;

import com.neurosky.connection.ConnectionStates;
import com.neurosky.connection.DataType.MindDataType;
import com.neurosky.connection.EEGPower;
import com.neurosky.connection.TgStreamHandler;
import com.neurosky.connection.TgStreamReader;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

public class mode1 extends AppCompatActivity {

    final int FILE_SELECT_CODE=8;
    private BluetoothAdapter mBlue;
    private TgStreamReader tgStreamReader;
    private static final int MSG_UPDATE_BAD_PACKET = 1001;
    private static final int MSG_UPDATE_STATE = 1002;
    private static final String TAG = mode1.class.getSimpleName();
    private DrawWaveView waveView = null;
    private LinearLayout wave_layout;
    private Button start;
    private Button pause;
    private Button  stop;
    private Button read;
    private Button firstSound;
    private Button secondSound;
    private Button thirdSound;
    private Button fourth;
    private AlertDialog dialog;
    private TextView attvalu;
    private TextView medvalu;
    private InputStream input;
    private File file;
    private String path;
    private int rul;
    private int playSound=1;
    private boolean canToPlay=false;
    public Context mContext;
    private EditText filename;
    private SoundClass soound;
    private SoundPool pool;
    private HashMap<Integer, Integer> soundID;
    String filedetail="00";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode1);
        mContext = mode1.this;
        soound=new SoundClass(mContext);
        intView();
        new Thread(){
            @Override
            public void run() {//加载音乐线程，将所有的音乐资源加载进音乐池
                    pool=soound.getPool();
                    soundID=soound.getMap();
                    pool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                        @Override
                        public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                            canToPlay=true;
                        }
                    });

            }
        }.start();
        Intent int2=getIntent();
        if(int2.getIntExtra("from",222)==1){
            read.performClick();
        }
    }
    public void intView(){//绑定控件
        wave_layout=(LinearLayout)findViewById(R.id.wave);
        setUpDrawWaveView();
        attvalu=(TextView)findViewById(R.id.attvalu);
        medvalu=(TextView)findViewById(R.id.medvalu);
        start=(Button)findViewById(R.id.start);
        pause=(Button)findViewById(R.id.pause);
        stop=(Button)findViewById(R.id.stop);
        read=(Button)findViewById(R.id.read);
        fourth=(Button)findViewById(R.id.fourth);
        firstSound=(Button)findViewById(R.id.firstSound);
        secondSound=(Button)findViewById(R.id.secondSound);
        thirdSound=(Button)findViewById(R.id.thirdSound);
        firstSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSound=1;
            }
        });
        secondSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSound=2;
            }
        });
        thirdSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSound=3;
            }
        });
        fourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSound=4;
            }
        });
        filename=new EditText(mContext);
        dialog = new AlertDialog.Builder(mContext).setTitle("请输入保存的文件名").setView(filename).setPositiveButton(
                "确定", new DialogInterface.OnClickListener(){
                    @Override public void onClick(DialogInterface dialog, int which) {
                        if(file!=null) {
                            String toname = filename.getText().toString();
                            String rename = file.getParent();
                            file.renameTo(new File(rename + File.separator+toname));
                            Log.d(TAG,rename+toname);
                        }}}).setNegativeButton("取消", null).create();
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//开始按钮点击事件
                if(tgStreamReader != null && tgStreamReader.isBTConnected()){
                    tgStreamReader.stop();
                    tgStreamReader.close();
                }
                tgStreamReader=new TgStreamReader(mBlue,callback);
                rul=0;
                tgStreamReader.connect();
            }
        });
        read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//读取文件按钮点击事件
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"),
                            FILE_SELECT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(mContext, "请安装文件管理器", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pool.autoPause();
            }
        });//暂停
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//停止
                if(tgStreamReader!=null){
                    tgStreamReader.stopRecordRawData();
                    tgStreamReader.stop();
                    pool.release();
                    dialog.show();
                }

            }
        });
        mBlue= BluetoothAdapter.getDefaultAdapter();
        if (mBlue == null)
            Toast.makeText(this, "Please enable your Bluetooth and re-run this program !", Toast.LENGTH_SHORT).show();


    }
    public void setUpDrawWaveView() {//初始化曲线
        waveView = new DrawWaveView(getApplicationContext());
        wave_layout.addView(waveView, new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        waveView.setValue(100, 100, 0);
    }

    public void updateWaveView(int data) {//更新曲线
        if (waveView != null) {
            waveView.updateData(data);
        }
    }
    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {//用户选择文件后调用

        if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            try{
                input=getContentResolver().openInputStream(uri);}catch (Exception e){e.printStackTrace();}
            if(tgStreamReader != null && tgStreamReader.isBTConnected()){
                tgStreamReader.stop();
                tgStreamReader.close();
            }
            if(input!=null){
                tgStreamReader=new TgStreamReader(input,callback);
                rul=0;
                tgStreamReader.connect();}
            else Log.d(TAG,"input is null");
        }
    }

    private TgStreamHandler callback = new TgStreamHandler() {
        @Override
        public void onStatesChanged(int connectionStates) {
            // TODO Auto-generated method stub
            switch (connectionStates) {
                case ConnectionStates.STATE_CONNECTING:
                    // Do something when connecting
                    showToast("连接设备中", Toast.LENGTH_SHORT);
                    break;
                case ConnectionStates.STATE_CONNECTED:
                    // Do something when connected
                    showToast("连接完成", Toast.LENGTH_SHORT);
                    tgStreamReader.start();
                    break;
                case ConnectionStates.STATE_WORKING:
                    showToast("工作中,等待头戴发出滴声",Toast.LENGTH_SHORT);
                    // Do something when working
                    //(9) demo of recording raw data , stop() will call stopRecordRawData,
                    //or you can add a button to control it.
                    //You can change the save path by calling setRecordStreamFilePath(String filePath) before startRecordRawData

                    break;
                case ConnectionStates.STATE_GET_DATA_TIME_OUT:
                    // Do something when getting data timeout

                    //(9) demo of recording raw data, exception handling
                    tgStreamReader.stopRecordRawData();
                    break;
                case ConnectionStates.STATE_STOPPED:
                    // Do something when stopped
                    // We have to call tgStreamReader.stop() and tgStreamReader.close() much more than
                    // tgStreamReader.connectAndstart(), because we have to prepare for that.

                    break;
                case ConnectionStates.STATE_DISCONNECTED:
                    // Do something when disconnected
                    showToast("失去连接",Toast.LENGTH_SHORT);
                    break;
                case ConnectionStates.STATE_ERROR:
                    // Do something when you get error message
                    showToast("error",Toast.LENGTH_LONG);
                    break;
                case ConnectionStates.STATE_FAILED:
                    // Do something when you get failed message
                    // It always happens when open the BluetoothSocket error or timeout
                    // Maybe the device is not working normal.
                    // Maybe you have to try again
                    break;
            }
        }
        @Override
        public void onRecordFail(int flag) {
            // You can handle the record error message here

        }

        @Override
        public void onChecksumFail(byte[] payload, int length, int checksum) {
            // You can handle the bad packets here.

        }

        @Override
        public void onDataReceived(int datatype, int data, Object obj) {//对接收到的数据进行打包
            // You can handle the received data here
            // You can feed the raw data to algo sdk here if necessary.

            Message msg = LinkDetectedHandler.obtainMessage();
            msg.what = datatype;
            msg.arg1 = data;
            msg.obj = obj;
            LinkDetectedHandler.sendMessage(msg);

            //Log.i(TAG,"onDataReceived");
        }

    };
    private Handler LinkDetectedHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {//对打包的数据进行拆分处理
            switch (msg.what) {
                case MindDataType.CODE_RAW:
                    // updateWaveView(msg.arg1);
                    break;
                case MindDataType.CODE_MEDITATION://平静度
                    medvalu.setText(""+msg.arg1);
                    // Log.d(TAG, "HeadDataType.CODE_MEDITATION " + msg.arg1);
                    //tv_meditation.setText("" +msg.arg1 );
                    break;
                case MindDataType.CODE_ATTENTION://注意力
                    Log.d(TAG, "CODE_ATTENTION " + msg.arg1);
                    if(msg.arg1!=0) {
                        tgStreamReader.startRecordRawData();
                        attvalu.setText(""+msg.arg1);
                        updateWaveView(msg.arg1);
                        filedetail+=""+msg.arg1;
                        if(canToPlay){
                            switch (playSound){
                                case 1:
                                    pool.play(soundID.get(msg.arg1/4), 1, 1, 0, 0, 1);
                                    break;
                                case 2:
                                    if(msg.arg1/7>=14)
                                        pool.play(soundID.get(39),1,1,0,0,1);
                                    else
                                        pool.play(soundID.get(msg.arg1/7+26),1,1,0,0,1);
                                    break;
                                case 3:
                                    if (msg.arg1/11>9)
                                        pool.play(soundID.get(48),1,1,0,0,1);
                                    else
                                        pool.play(soundID.get(msg.arg1/11+40),1,1,0,0,1);
                                    break;
                                case 4:
                                    pool.play(soundID.get(msg.arg1/4), 1, 1, 0, 0, 1);
                                    if(msg.arg1/7>=14)
                                        pool.play(soundID.get(39),(float)0.6,(float)0.6,0,0,1);
                                    else
                                        pool.play(soundID.get(msg.arg1/7+26),(float)0.6,(float)0.6,0,0,1);
                                    if (msg.arg1/11>=9)
                                        pool.play(soundID.get(48),(float)0.8,(float)0.8,0,0,1);
                                    else
                                        pool.play(soundID.get(msg.arg1/11+40),(float)0.8,(float)0.8,0,0,1);
                                    break;
                            }
                        }
                        rul++;
                        Log.d(TAG, rul + "att valu" + filedetail);
                    }
                    break;
                case MindDataType.CODE_EEGPOWER:
                    EEGPower power = (EEGPower)msg.obj;
                  /*  if(power.isValidate()){
                        tv_delta.setText("" +power.delta);
                        tv_theta.setText("" +power.theta);
                        tv_lowalpha.setText("" +power.lowAlpha);
                        tv_highalpha.setText("" +power.highAlpha);
                        tv_lowbeta.setText("" +power.lowBeta);
                        tv_highbeta.setText("" +power.highBeta);
                        tv_lowgamma.setText("" +power.lowGamma);
                        tv_middlegamma.setText("" +power.middleGamma);
                    }*/
                    break;
                case MindDataType.CODE_POOR_SIGNAL://
                    int poorSignal = msg.arg1;
                    Log.d(TAG, "poorSignal:" + poorSignal);
                    //tv_ps.setText(""+msg.arg1);

                    break;
                case MSG_UPDATE_BAD_PACKET:
                    // tv_badpacket.setText("" + msg.arg1);

                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    public void showToast(final String msg,final int timeStyle){
        mode1.this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                Toast.makeText(getApplicationContext(), msg, timeStyle).show();
            }

        });
    }
}
