package com.example.minions.im.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minions.im.R;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;


public class LoginActivty extends Activity implements View.OnClickListener {

    private static String password;
    private CustomVideoView videoview;
    private TextView toregister;
    private LineEditText name;
    private LineEditText pass;
    private Button button;
    private String objectId;

    public static String getPassword() {
        return password;
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        MainVie mainVie=new MainVie();
        setContentView(R.layout.login);
        init();
        toregister.setOnClickListener(this);
        button.setOnClickListener(this);
        initView();
    }

    private void init() {
        toregister = (TextView) findViewById(R.id.user_register);
        name = (LineEditText) findViewById(R.id.user_name);
        pass = (LineEditText)findViewById(R.id.user_password);
        button = (Button) findViewById(R.id.button);
    }

    private void initView() {
        //加载视频资源控件
        videoview = (CustomVideoView) findViewById(R.id.videoView);
        //设置播放加载路径
        videoview.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.f1));
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_register:
                Intent intent = new Intent(LoginActivty.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.button:
                final String user_name = name.getText().toString();
                final String user_pass = pass.getText().toString();
                if (TextUtils.isEmpty(user_name) || TextUtils.isEmpty(user_pass)) {
                    showResult("用户名或密码不能为空！");
                    return;
                }
                /*if (!Utils.isMobile(user_name)) {
                    showResult("无效的手机号码！");
                    return;
                }*/
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        EMClient.getInstance().login(user_name,user_pass,new EMCallBack() {//回调
                            @Override
                            public void onSuccess() {
                                password = user_pass;
                                EMClient.getInstance().groupManager().loadAllGroups();
                                EMClient.getInstance().chatManager().loadAllConversations();
                                showResult("登陆成功！");

                                //更改用户状态
                                Intent intent = new Intent(LoginActivty.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            @Override
                            public void onProgress(int progress, String status) {

                            }

                            @Override
                            public void onError(int code, String message) {
                                showResult("登陆失败！");
                            }
                        });
                    }
                }).start();
                break;

        }
    }


    private void showResult(final String res) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivty.this, res, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
