package com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.R;
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
public class GroupDetailActivity extends Activity implements View.OnClickListener {
    private ImageButton GroupNotice;
    private ImageButton GroupFile;
    private ImageButton GroupShutup;
    private ImageButton GroupDesc;
    private ImageButton GroupMangers;
    private String toChatName;
    private TextView groupName;
    private Switch Noshow;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_group_owner);
        initView();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //根据群组ID从服务器获取群组基本信息
                try {
                    EMGroup group = EMClient.getInstance().groupManager().getGroupFromServer(toChatName);
                  //获取群主
                    Log.d("Group",group.getOwner());
                    if(EMClient.getInstance().getCurrentUser().equals(group.getOwner())){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                GroupShutup.setVisibility(View.VISIBLE);
                                GroupDesc.setVisibility(View.VISIBLE);
                                GroupMangers.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }

            }
        }).start();
        initListenter();
        EMGroup now = EMClient.getInstance().groupManager().getGroup(toChatName);
        groupName.setText(now.getGroupName());
    }

    private void initListenter() {
        GroupNotice.setOnClickListener(this);
        GroupFile.setOnClickListener(this);
        GroupShutup.setOnClickListener(this);
        GroupDesc.setOnClickListener(this);
        GroupMangers.setOnClickListener(this);
        Noshow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(GroupDetailActivity.this, "xixi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        Noshow = (Switch) findViewById(R.id.switch2);
        groupName = (TextView) findViewById(R.id.group_name);
        toChatName=getIntent().getStringExtra("name");
        GroupNotice = (ImageButton) findViewById(R.id.btn_notice);
        GroupFile = (ImageButton) findViewById(R.id.btn_file);
        GroupShutup = (ImageButton) findViewById(R.id.btn_speak);
        GroupDesc = (ImageButton) findViewById(R.id.btn_describe);
        GroupMangers = (ImageButton) findViewById(R.id.btn_manager);
        GroupShutup.setVisibility(View.INVISIBLE);
        GroupDesc.setVisibility(View.INVISIBLE);
        GroupMangers.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = new Intent(GroupDetailActivity.this, GroupControl.class);
        intent.putExtra("name",toChatName);
       if(id==R.id.btn_notice){
           intent.putExtra("type",1);
       }else if(id == R.id.btn_file){
           intent.putExtra("type",1);
       }else if(id == R.id.btn_speak){
           intent.putExtra("type",1);
       }else if(id == R.id.btn_describe){
           intent.putExtra("type",1);
       }else if(id ==R.id.btn_manager){
           intent.putExtra("type",1);
       }
       startActivity(intent);
    }
}
