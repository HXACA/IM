package com.example.minions.im.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.GroupDetailActivity;
import com.example.minions.im.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class GroupManegerActivity extends GroupDetailActivity implements View.OnClickListener{
    private Button dissolve;
    private String toChatName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_maneger);
        toChatName=getIntent().getStringExtra("name");
        dissolve = (Button) findViewById(com.hyphenate.easeui.R.id.dissolve);
    }

    @Override
    public void initListenter() {
        dissolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GroupManegerActivity.this);
                builder.setMessage("确定要删除该群吗？");
                builder.setTitle("删除群");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    EMClient.getInstance().groupManager().destroyGroup(toChatName);//需异步处理
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(GroupManegerActivity.this, "您已删除该群！", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } catch (HyphenateException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                });
                builder.show();
            }
        });
    }
}
