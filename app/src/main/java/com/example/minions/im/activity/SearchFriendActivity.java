package com.example.minions.im.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
public class SearchFriendActivity extends Activity{
    private EditText num;
    private Button button;
    private Button button2;
    private String fnum = null;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_friend);
        num = (EditText) findViewById(R.id.findNum);
        button = (Button) findViewById(R.id.searchFriend);
      button2 = (Button) findViewById(R.id.searchGroup);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               fnum = num.getText().toString().trim();
                if(fnum==null || fnum.length()<=0){
                    Toast.makeText(SearchFriendActivity.this, "号码不得为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(fnum.equals(EMClient.getInstance().getCurrentUser())){
                    Toast.makeText(SearchFriendActivity.this, "您不能加自己为好友！", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean flag=false;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int flag=0;
                            List<String> usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                            for(int i=0;i<usernames.size();i++){
                                if(usernames.get(i).equals(fnum)){
                                    flag=1;
                                    break;
                                }
                            }
                            if(flag==0){
                                Intent intent = new Intent(SearchFriendActivity.this,AddFriendActivity.class);
                                intent.putExtra("name",fnum);
                                startActivity(intent);
                            }else{
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(SearchFriendActivity.this, "您已经是他的好友！", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (HyphenateException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnum = num.getText().toString().trim();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            EMClient.getInstance().groupManager().applyJoinToGroup(fnum, "求加入");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SearchFriendActivity.this, "已向"+fnum+"发送了加群申请！", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                            });
                        } catch (final HyphenateException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(SearchFriendActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
}
