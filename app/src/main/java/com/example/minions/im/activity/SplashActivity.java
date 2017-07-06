package com.example.minions.im.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.minions.im.R;
import com.hyphenate.chat.EMClient;

import cn.bmob.v3.Bmob;

public class SplashActivity extends Activity {
    private SQLiteDatabase db;
    Boolean isshow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Bmob.initialize(this, "d47808a0512885e466e42f5ff8b0e981");
        createBase();
        Log.d("Splash","1"+EMClient.getInstance().getCurrentUser()+(EMClient.getInstance().isLoggedInBefore()?"1":"0"));
        if (!EMClient.getInstance().isLoggedInBefore() || EMClient.getInstance().getCurrentUser()==null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    startActivity(new Intent().setClass(SplashActivity.this, LoginActivty.class));
                    finish();
                }
            }, 3000);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    startActivity(new Intent().setClass(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }, 0);
        }
    }

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


    @Override
    protected void onRestart() {
        super.onRestart();
        onCreate(null);
    }
}


