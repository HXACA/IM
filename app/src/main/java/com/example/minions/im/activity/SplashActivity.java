package com.example.minions.im.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.minions.im.R;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.NetUtils;

public class SplashActivity extends Activity {
    private SQLiteDatabase db;
    Boolean isshow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        createBase();
        if (!EMClient.getInstance().isLoggedInBefore()){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    startActivity(new Intent().setClass(SplashActivity.this, LoginActivty.class));
                    finish();
                }
            }, 1000);
        }else{
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    startActivity(new Intent().setClass(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }, 0);
        }
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());}

    private void createBase() {
        db = openOrCreateDatabase("User.db", Context.MODE_PRIVATE, null);
        String user_table = "create table if not exists FriendAsk("
                +"_id integer primary key autoincrement,"
                +"f_toName text,"
                +"f_fromName text,"
                +"f_type integer,"
                + "f_dec text)";
        db.execSQL(user_table);
    }


    private void showResult(final String res) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
            isshow=false;
        }
        @Override
        public void onDisconnected(final int error) {
            if(isshow==false){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(error == EMError.USER_REMOVED){
                            // 显示帐号已经被移除
                        }else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                            // 显示帐号在其他设备登录
                            showResult("您的账号在别处登录");
                            isshow = true;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    EMClient.getInstance().logout(true);
                                    Intent intent = new Intent(getApplicationContext(),LoginActivty.class);
                                    startActivity(intent);
                                }
                            }).start();
                        } else {
                            if (NetUtils.hasNetwork(SplashActivity.this)){
                                //连接不到聊天服务器
                            }
                            else{
                                //当前网络不可用，请检查网络设置
                                isshow=true;
                                showResult("无法连接网络！");
                            }
                        }
                    }
                });
            }

        }
    }
}

