package com.example.liudingming.listenyourbrain;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.neurosky.connection.ConnectionStates;
import com.neurosky.connection.DataType.MindDataType;
import com.neurosky.connection.EEGPower;
import com.neurosky.connection.TgStreamHandler;
import com.neurosky.connection.TgStreamReader;

public class mode2 extends AppCompatActivity { private final static String TAG="mode2";
    private static final int MSG_UPDATE_BAD_PACKET = 1001;
    private static final int MSG_UPDATE_STATE = 1002;
    final int FILE_SELECT_CODE=8;
    private Button select;
    private Button play;
    private Button pause;
    private Button stop;
    private Button connect;
    private Button disconnect;
    private TgStreamReader mode2Tg;
    private BluetoothAdapter modeBlue;
    private Context mode2Context;
    private MediaPlayer music;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode2);
        mode2Context=getApplicationContext();
        intView();
        showToast("请先选择音乐", Toast.LENGTH_SHORT);
    }
    public void  intView(){
        select=(Button)findViewById(R.id.select);
        play=(Button)findViewById(R.id.play);
        pause=(Button)findViewById(R.id.pause);
        stop=(Button)findViewById(R.id.stop);
        connect=(Button)findViewById(R.id.coneect);
        disconnect=(Button)findViewById(R.id.stopconnect);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                music=new MediaPlayer();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(Intent.createChooser(intent, "请选择一个要上传的文件"),5);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(mode2Context, "请安装文件管理器", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(music!=null)
                {
                    music.start();}
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(music!=null)
                    music.pause();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(music!=null){
                    music.stop();
                    music.release();}
                music=null;
            }
        });
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modeBlue= BluetoothAdapter.getDefaultAdapter();
                if (modeBlue == null || !modeBlue.isEnabled())
                    Toast.makeText(mode2Context, "Please enable your Bluetooth and re-run this program !", Toast.LENGTH_SHORT).show();
                if(modeBlue!=null)
                    Toast.makeText(mode2Context,"成功获取蓝牙",Toast.LENGTH_SHORT).show();
                mode2Tg=new TgStreamReader(modeBlue,mode2Handle);
                if(mode2Tg != null && mode2Tg.isBTConnected()){
                    mode2Tg.stop();
                    mode2Tg.close();
                }
                mode2Tg.connect();
            }
        });
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode2Tg!=null){
                    mode2Tg.stopRecordRawData();
                    mode2Tg.stop();
                }
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
            uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            try{
                music.setDataSource(mode2Context,uri);
                music.prepare();
            }catch (Exception e){e.printStackTrace();}
        }
    }

    private TgStreamHandler mode2Handle = new TgStreamHandler() {
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
                    mode2Tg.start();
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
                    mode2Tg.stopRecordRawData();
                    break;
                case ConnectionStates.STATE_STOPPED:
                    // Do something when stopped
                    // We have to call tgStreamReader.stop() and tgStreamReader.close() much more than
                    // tgStreamReader.connectAndstart(), because we have to prepare for that.
                    //music.setPlaybackParams(music.getPlaybackParams().setSpeed((float)1));
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
        public void onDataReceived(int datatype, int data, Object obj) {
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
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MindDataType.CODE_RAW:
                    // updateWaveView(msg.arg1);
                    break;
                case MindDataType.CODE_MEDITATION:
                    // Log.d(TAG, "HeadDataType.CODE_MEDITATION " + msg.arg1);
                    //tv_meditation.setText("" +msg.arg1 );
                    break;
                case MindDataType.CODE_ATTENTION:
                    Log.d(TAG, "CODE_ATTENTION " + msg.arg1);
                    if(msg.arg1!=0&&music.isPlaying()) {
                        float dd=(float)msg.arg1/(float)100+(float)0.5;
                         music.setPlaybackParams(music.getPlaybackParams().setSpeed(dd));//更改音乐播放速度
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
                        music/instrument1/high/a4.wav


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
        mode2.this.runOnUiThread(new Runnable()
        {
            public void run()
            {
                Toast.makeText(getApplicationContext(), msg, timeStyle).show();
            }

        });
    }
}
