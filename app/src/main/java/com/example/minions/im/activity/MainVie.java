package com.example.minions.im.activity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.example.minions.im.R;


public class MainVie extends AppCompatActivity {

    private CustomVideoView videoview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EditText register=(EditText)findViewById(R.id.user_password);

        if(register!=null)
        {
            register.bringToFront();
            register.setHintTextColor(Color.WHITE);
        }

        setContentView(R.layout.login);
        //加载数据
        initView();
    }

    private void initView() {
        //加载视频资源控件
        videoview = (CustomVideoView) findViewById(R.id.videoView);
        //设置播放加载路径
        videoview.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.f1));
        Log.i("info","xiix");
        //播放
        videoview.start();
        //循环播放
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoview.start();
            }
        });
    }

    //返回重启加载
    @Override
    protected void onRestart() {
        initView();
        super.onRestart();
    }

    //防止锁屏或者切出的时候，音乐在播放
    @Override
    protected void onStop() {
        videoview.stopPlayback();
        super.onStop();
    }

}
