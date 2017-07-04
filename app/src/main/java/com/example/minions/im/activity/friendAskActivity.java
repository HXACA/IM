package com.example.minions.im.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.minions.im.R;
import com.example.minions.im.adapter.friendAsk;
import com.example.minions.im.adapter.friendAskAdapter;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
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
public class friendAskActivity extends Activity {
    private List<friendAsk> userList = new ArrayList<>();
    private ListView askFriend;
    private SQLiteDatabase db;
    private EaseTitleBar titleBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_askfriend);
        titleBar = (EaseTitleBar) findViewById(R.id.ask_title_bar);
        titleBar.setTitle("好友申请列表");
        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        askFriend = (ListView) findViewById(R.id.askFriendList);
        db = openOrCreateDatabase("User.db", Context.MODE_PRIVATE, null);
        getAsks();
        friendAskAdapter adapter = new friendAskAdapter(friendAskActivity.this,R.layout.friendask_item,userList);
        askFriend.setAdapter(adapter);
       askFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               final String fromname = userList.get(position).getFromName();
               final String toname = userList.get(position).getToName();
               String dec = userList.get(position).getDec();
               int type = userList.get(position).getType();
               if(type!=0){
                   Toast.makeText(friendAskActivity.this, "此申请你已处理！", Toast.LENGTH_SHORT).show();
                   return;
               }
               AlertDialog.Builder builder = new AlertDialog.Builder(friendAskActivity.this);
               builder.setMessage("来自"+fromname+"的好友申请\n验证信息:"+dec);
               builder.setTitle("好友申请");
               builder.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    EMClient.getInstance().contactManager().declineInvitation(fromname);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(friendAskActivity.this, "您拒绝了"+fromname+"的好友请求！", Toast.LENGTH_SHORT).show();
                                            changeType(fromname,toname,1);
                                        }
                                    });
                                } catch (HyphenateException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                   }
               });
               builder.setPositiveButton("接受", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       new Thread(new Runnable() {
                           @Override
                           public void run() {
                               try {
                                   EMClient.getInstance().contactManager().acceptInvitation(fromname);
                                   runOnUiThread(new Runnable() {
                                       @Override
                                       public void run() {
                                           Toast.makeText(friendAskActivity.this, "您接受了"+fromname+"的好友请求！", Toast.LENGTH_SHORT).show();
                                           changeType(fromname,toname,2);
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

    private void changeType(String fromname, String toname,int res) {
        Cursor cursor = db.rawQuery("select * from FriendAsk where f_toName=? and f_fromName=?",new String[]{toname,fromname});
        ContentValues cv = new ContentValues();
        cv.clear();
        cv.put("f_type",res);
        cursor.moveToFirst();
        long rowid = db.update("FriendAsk", cv, "f_toName=" + toname+" and f_fromName="+fromname,null);
        userList.clear();
        onCreate(null);
    }

    public void getAsks() {
        String toName = EMClient.getInstance().getCurrentUser();
        Cursor cursor = db.rawQuery("select * from FriendAsk",new String[]{});
        if(cursor.getCount()==0){
            Toast.makeText(this, "暂无好友请求！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(friendAskActivity.this,MainActivity.class);
            intent.putExtra("name",1);
            startActivity(intent);
            finish();
        }
        cursor.moveToFirst();
        int count=0;
        for(int i=0;i<cursor.getCount();i++){
            if(cursor.getString(cursor.getColumnIndex("f_toName")).equals(toName)){
                String Name = cursor.getString(cursor.getColumnIndex("f_toName"));
                String fromName = cursor.getString(cursor.getColumnIndex("f_fromName"));
                String des = cursor.getString(cursor.getColumnIndex("f_dec"));
               Integer type =  cursor.getInt(cursor.getColumnIndex("f_type"));
               friendAsk t = new friendAsk(Name,fromName,des,type);
               if(type==0 || count<5){
                   if(type!=0)
                       count++;
                   userList.add(t);
               }
            }
            cursor.moveToNext();
        }
    }
}
