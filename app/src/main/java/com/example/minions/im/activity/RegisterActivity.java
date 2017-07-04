package com.example.minions.im.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minions.im.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


public class RegisterActivity extends Activity implements OnClickListener{

    private CustomVideoView videoview;
    private EditText edit_phone;
    private EditText edit_cord;
    private EditText edit_pass;
    private EditText re_sedit_pass;
    private TextView now;
    private Button btn_getCord;
    private Button btn_register;
    private String phone_number;
    private String cord_number;
    EventHandler eventHandler;
    private int time=60;
    private boolean flag=true;
    private ImageButton see;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        MobSDK.init(RegisterActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        backgroudView();
        getId();
        eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                Message msg=new Message();
                msg.arg1=event;
                msg.arg2=result;
                msg.obj=data;
                handler.sendMessage(msg);
            }
        };

        SMSSDK.registerEventHandler(eventHandler);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    /**
     * 使用Handler来分发Message对象到主线程中，处理事件
     */
    Handler handler=new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event=msg.arg1;
            int result=msg.arg2;
            Object data=msg.obj;
            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                if(result == SMSSDK.RESULT_COMPLETE) {
                    boolean smart = (Boolean)data;
                    if(smart) {
                        Toast.makeText(getApplicationContext(),"该手机号已经注册过，请重新输入",
                                Toast.LENGTH_LONG).show();
                        edit_phone.requestFocus();
                        return;
                    }
                }
            }
            if(result==SMSSDK.RESULT_COMPLETE)
            {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    final String phone = edit_phone.getText().toString().trim();
                    final String pass = edit_pass.getText().toString().trim();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().createAccount(phone, pass);//同步方法
                                showResult("注册成功！");
                                Intent intent = new Intent(RegisterActivity.this,LoginActivty.class);
                                startActivity(intent);
                            } catch (HyphenateException e) {
                                showResult("注册失败！");
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }
            }
            else
            {
                if(flag)
                {
                    btn_getCord.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"验证码获取失败请重新获取", Toast.LENGTH_LONG).show();
                        edit_phone.requestFocus();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"验证码输入错误", Toast.LENGTH_LONG).show();
                }
            }

        }

    };

    private void showResult(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 获取id
     */
    private void getId()
    {
        edit_phone= (EditText) findViewById(R.id.edit_phone);
        edit_cord= (EditText) findViewById(R.id.edit_code);
        edit_pass = (EditText) findViewById(R.id.edit_pass);
        see = (ImageButton) findViewById(R.id.see);
        re_sedit_pass= (EditText) findViewById(R.id.re_edit_pass);
        btn_getCord= (Button) findViewById(R.id.btn_getcord);
        btn_register= (Button) findViewById(R.id.btn_register);
        btn_getCord.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        see.setOnClickListener(this);
    }

    /**
     * 按钮点击事件
     */
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_getcord:
                if(judPhone())//去掉左右空格获取字符串
                {
                    SMSSDK.getVerificationCode("86",phone_number);
                    edit_cord.requestFocus();
                }
                break;
            case R.id.btn_register:
                String phone = edit_phone.getText().toString().trim();
                String pass = edit_pass.getText().toString().trim();
                String repass = re_sedit_pass.getText().toString().trim();
                if(!judPhone()){
                   return;
                }
                if(pass==null || pass.length()<=0){
                    Toast.makeText(this, "密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!pass.equals(repass)){
                    Toast.makeText(this, "两次密码不一致！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(judCord())
                SMSSDK.submitVerificationCode("86",phone_number,cord_number);
                flag=false;
                break;
            case R.id.see:
                if(edit_pass.getInputType() == 129){
                    edit_pass.setInputType(InputType.TYPE_CLASS_TEXT);
                    re_sedit_pass.setInputType(InputType.TYPE_CLASS_TEXT);
                } else{
                    edit_pass.setInputType(129);
                    re_sedit_pass.setInputType(129);
                }
                break;
            default:
                break;
        }
    }

    private boolean judPhone()
    {
        if(TextUtils.isEmpty(edit_phone.getText().toString().trim()))
        {
            Toast.makeText(RegisterActivity.this,"请输入您的电话号码",Toast.LENGTH_LONG).show();
            edit_phone.requestFocus();
            return false;
        }
        else if(edit_phone.getText().toString().trim().length()!=11)
        {
            Toast.makeText(RegisterActivity.this,"您的电话号码位数不正确",Toast.LENGTH_LONG).show();
            edit_phone.requestFocus();
            return false;
        }
        else
        {
            phone_number=edit_phone.getText().toString().trim();
            if(phone_number.length()==11)
                return true;
            else
            {
                Toast.makeText(RegisterActivity.this,"请输入正确的手机号码",Toast.LENGTH_LONG).show();
                return false;
            }
        }
    }

    private boolean judCord()
    {
        judPhone();
        if(TextUtils.isEmpty(edit_cord.getText().toString().trim()))
        {
            Toast.makeText(RegisterActivity.this,"请输入您的验证码",Toast.LENGTH_LONG).show();
            edit_cord.requestFocus();
            return false;
        }
        else if(edit_cord.getText().toString().trim().length()!=4)
        {
            Toast.makeText(RegisterActivity.this,"您的验证码位数不正确",Toast.LENGTH_LONG).show();
            edit_cord.requestFocus();
            return false;
        }
        else
        {
            cord_number=edit_cord.getText().toString().trim();
            return true;
        }

    }



    /**
     * 背景视频播放
     */
    private void backgroudView() {
        videoview = (CustomVideoView) findViewById(R.id.videoView);
        videoview.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.f1));
        videoview.start();
        videoview.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                videoview.start();
            }
        });
    }
    @Override
    protected void onRestart() {
        backgroudView();
        super.onRestart();
    }
    @Override
    protected void onStop() {
        videoview.stopPlayback();
        super.onStop();
    }
}
