package com.example.minions.im.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.minions.im.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

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
public class AddFriendActivity extends Activity{
    private Button add;
    private Button delete;
    private String toName;
    private TextView textView;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_message);
        toName = getIntent().getStringExtra("name");
        textView = (TextView) findViewById(R.id.friend_nickname);
        textView.setText(toName);
        add = (Button) findViewById(R.id.btn_add);
        delete = (Button)findViewById(R.id.button33);
        delete.setVisibility(View.INVISIBLE);
        add.setVisibility(View.INVISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    if(!usernames.contains(toName)){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                add.setVisibility(View.VISIBLE);
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                delete.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputTitleDialog();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().contactManager().deleteContact(toName);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AddFriendActivity.this, "您已删除"+toName+"好友！", Toast.LENGTH_SHORT).show();
                                    onCreate(null);
                                }
                            });
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    private void inputTitleDialog() {
        final EditText edit = new EditText(this);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
       builder.setTitle("请输入").setIcon(android.R.drawable.ic_dialog_info).setView(edit).
                setPositiveButton("发送", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    EMClient.getInstance().contactManager().addContact(toName,edit.getText().toString());
                                    showResult("请求:"+edit.getText().toString() +"已发送至"+String.valueOf(toName));
                                    Intent intent = new Intent(AddFriendActivity.this,MainActivity.class);
                                    intent.putExtra("name",1);
                                    startActivity(intent);
                                } catch (HyphenateException e) {
                                    e.printStackTrace();
                                    showResult("失败！");
                                }
                            }
                        }).start();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    private void showResult(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AddFriendActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
