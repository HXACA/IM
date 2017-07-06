package com.example.minions.im.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.minions.im.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.exceptions.HyphenateException;

/*
                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
         佛祖保佑       永无BUG
*/
public class BuildActivity extends Activity{
    private EditText name;
    private EditText des;
    private EditText rea;
    private Button submit;
    private Switch group_type;
    private Switch group_invite;
    private int openType = 0;
    private int inviteType=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build);
        name = (EditText) findViewById(R.id.groupName);
        des = (EditText) findViewById(R.id.groupDes);
        rea = (EditText) findViewById(R.id.groupRea);
        submit = (Button)findViewById(R.id.groupCreate);
        group_invite = (Switch) findViewById(R.id.group_invite);
        group_invite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               inviteType = isChecked?1:0;
            }
        });
        group_type = (Switch)findViewById(R.id.group_type);
        group_type.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                openType = isChecked?1:0;
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String groupName = name.getText().toString();
                final String desc = des.getText().toString();
                final String reason = rea.getText().toString();
                final EMGroupManager.EMGroupOptions option = new EMGroupManager.EMGroupOptions();
                option.maxUsers = 200;
                if(openType == 0 && inviteType==1)
                    option.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateMemberCanInvite;
                else if(openType==0 && inviteType==0)
                    option.style = EMGroupManager.EMGroupStyle.EMGroupStylePrivateOnlyOwnerInvite;
                else if(openType==1 && inviteType==0)
                    option.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval;
                else if(openType==1 && inviteType==1)
                    option.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicOpenJoin;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().groupManager().createGroup(groupName, desc,new String[]{}, reason, option);
                            showResult("创建成功！");
                            Intent intent = new Intent(BuildActivity.this,GroupActivity.class);
                            startActivity(intent);
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                            showResult("创建失败！");
                        }
                    }
                }).start();

            }
        });
    }

    private void showResult(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BuildActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
