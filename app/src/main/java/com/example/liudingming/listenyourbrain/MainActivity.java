package com.example.liudingming.listenyourbrain;
import com.example.liudingming.listenyourbrain.Fragment.*;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    final int FILE_SELECT_CODE=8;
    private Button homePage;
    private Button selec;
    private Button share;
    private Button login;
    private final int MY_PERMISSIONS_REQUEST_READ_CONTACTS=5;
    Context thisContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        thisContext=this;
        bindView();
       // getCheckPremission();
    }
    private void getCheckPremission(){
        if (checkSelfPermission(Manifest.permission_group.STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission_group.STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==MY_PERMISSIONS_REQUEST_READ_CONTACTS){
            int grantResult=grantResults[0];
            boolean grant=grantResult==PackageManager.PERMISSION_GRANTED;
            if(!grant)
                finish();
        }
    }

    public void bindView(){//绑定控件
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        homePage=(Button)findViewById(R.id.homePage);
        selec=(Button)findViewById(R.id.selectToPlay);
        share=(Button)findViewById(R.id.share);
        login=(Button)findViewById(R.id.login);
        homePage fragment = new homePage();
        fragmentTransaction.add(R.id.fram, fragment);
        fragmentTransaction.commit();
        homePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//主页按钮点击事件
                FragmentManager fragmentManager=getFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                homePage homePagefragment = new homePage();
                fragmentTransaction.replace(R.id.fram, homePagefragment);
                fragmentTransaction.addToBackStack(null);fragmentTransaction.commit();
            }
        });
        selec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//从文件播放事件
                Intent intent=new Intent(thisContext,mode1.class);
                intent.putExtra("from",1);
                startActivity(intent);
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//分享按钮事件
                FragmentManager fragmentManager=getFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                share shareFragment=new share();
                fragmentTransaction.replace(R.id.fram,shareFragment);
                fragmentTransaction.addToBackStack(null);fragmentTransaction.commit();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//登陆按钮事件
                FragmentManager fragmentManager=getFragmentManager();
                FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                login loginFrafment=new login();
                fragmentTransaction.replace(R.id.fram,loginFrafment);
                fragmentTransaction.addToBackStack(null);fragmentTransaction.commit();
            }
        });
    }

    @Override
    protected void onDestroy() {
        //(6) use close() to release resource
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
