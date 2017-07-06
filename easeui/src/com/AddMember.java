package com;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.R;
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
public class AddMember extends Activity{
    private List<String> in = new ArrayList<>();
    private List<String> friend = new ArrayList<>();
    private ListView lv;
    private MyAdapter mAdapter;
    private List<String> list = new ArrayList<>();
    private Button bt_selectall;
    private Button bt_cancel;
    private Button bt_deselectall;
    private int checkNum; // 记录选中的条目数量
    private TextView tv_show;// 用于显示选中的条目数量
    private String groupId;
    private EMGroup group;
    private EaseTitleBar titlebar;
    private Button submit;
    private Boolean[] vis = new Boolean[105];
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        for(int i=0;i<105;i++)
            vis[i]=false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addmember);
        titlebar = (EaseTitleBar) findViewById(R.id.add_title_bar);
        submit = (Button) findViewById(R.id.submitButton);
        titlebar.setTitle("添加群好友");
        titlebar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        groupId = getIntent().getStringExtra("groupId");
        list = new ArrayList<String>();
        initDate();
        /* 实例化各个控件 */
        lv = (ListView) findViewById(R.id.lv);
        bt_selectall = (Button) findViewById(R.id.bt_selectall);
        bt_cancel = (Button) findViewById(R.id.bt_cancleselectall);
        bt_deselectall = (Button) findViewById(R.id.bt_deselectall);
        tv_show = (TextView) findViewById(R.id.tv);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mAdapter = new MyAdapter(list, this);
        lv.setAdapter(mAdapter);



        bt_selectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 遍历list的长度，将MyAdapter中的map值全部设为true
                for (int i = 0; i < list.size(); i++) {
                    MyAdapter.getIsSelected().put(i, true);
                }
                // 数量设为list的长度
                checkNum = list.size();
                // 刷新listview和TextView的显示
                dataChanged();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               final String[] newMembers = new String[105];
                int count=0;
                for(int i=0;i<list.size();i++){
                    if(vis[i]){
                        newMembers[count++]=list.get(i);
                    }
                }
                if(EMClient.getInstance().getCurrentUser().equals(group.getOwner())){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().groupManager().addUsersToGroup(groupId, newMembers);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AddMember.this, "发送邀请成功！", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), GroupDetailActivity.class);
                                        intent.putExtra("name",groupId);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            } catch (final HyphenateException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AddMember.this, e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().groupManager().inviteUser(groupId,newMembers,null);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AddMember.this, "发送邀请成功！", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), GroupDetailActivity.class);
                                        intent.putExtra("name",groupId);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            } catch (final HyphenateException e) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AddMember.this, e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }

            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 遍历list的长度，将已选的设为未选，未选的设为已选
                for (int i = 0; i < list.size(); i++) {
                    if (MyAdapter.getIsSelected().get(i)) {
                        MyAdapter.getIsSelected().put(i, false);
                        checkNum--;
                    } else {
                        MyAdapter.getIsSelected().put(i, true);
                        checkNum++;
                    }
                }
                // 刷新listview和TextView的显示
                dataChanged();
            }
        });


        bt_deselectall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 遍历list的长度，将已选的按钮设为未选
                for (int i = 0; i < list.size(); i++) {
                    if (MyAdapter.getIsSelected().get(i)) {
                        MyAdapter.getIsSelected().put(i, false);
                        checkNum--;// 数量减1
                    }
                }
                // 刷新listview和TextView的显示
                dataChanged();
            }
        });

        // 绑定listView的监听器
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // 取得ViewHolder对象，这样就省去了通过层层的findViewById去实例化我们需要的cb实例的步骤
                MyAdapter.ViewHolder holder = (MyAdapter.ViewHolder) arg1.getTag();
                // 改变CheckBox的状态
                holder.cb.toggle();
                // 将CheckBox的选中状况记录下来
                MyAdapter.getIsSelected().put(arg2, holder.cb.isChecked());
                // 调整选定条目
                if (holder.cb.isChecked() == true) {
                    vis[arg2]=true;
                    checkNum++;
                } else {
                    vis[arg2]=false;
                    checkNum--;
                }
                // 用TextView显示
                tv_show.setText("已选中" + checkNum + "项");
            }
        });
    }


    // 初始化数据
    private void initDate() {
        group = EMClient.getInstance().groupManager().getGroup(groupId);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    group = EMClient.getInstance().groupManager().getGroupFromServer(groupId);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        in = group.getMembers();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    friend = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    for(int i=0;i<friend.size();i++){
                        if(!in.contains(friend.get(i))){
                            list.add(friend.get(i));
                        }
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Log.d("AddMember",String.valueOf(in.size())+String.valueOf(friend.size()));

    }
    // 刷新listview和TextView的显示
    private void dataChanged() {
        // 通知listView刷新
        mAdapter.notifyDataSetChanged();
        // TextView显示最新的选中数目
        tv_show.setText("已选中" + checkNum + "项");
    };
}

