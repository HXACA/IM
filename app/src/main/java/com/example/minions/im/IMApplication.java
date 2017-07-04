package com.example.minions.im;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.controller.EaseUI;


public class IMApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("util","oncreate");
        //初始化环信的EaseUI
        EMOptions options = new EMOptions();
        options.setAcceptInvitationAlways(false);//设置需要同意后才能接受邀请
        options.setAutoAcceptGroupInvitation(false);//设置需要同意后才能接受群聊
        EaseUI.getInstance().init(this,options);
        EMClient.getInstance().init(getApplicationContext(),options);
        //初始化全局上下文
        mContext = this;

    }
    //获取全局上下文对象
    public static Context getGolbalApplication(){
        return mContext;
    }
}
