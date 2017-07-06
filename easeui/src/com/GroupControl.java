package com;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.adapter.Deleteadapter;
import com.hyphenate.easeui.adapter.Notice;
import com.hyphenate.easeui.adapter.NoticeAdapter;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

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
public class GroupControl extends Activity{
    private int actionId;
    private String groupId;
    private EMGroup group = null;
    private List<String>all = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionId = getIntent().getIntExtra("type",-1);
        groupId = getIntent().getStringExtra("name");
        show(actionId);
    }

    private void show(int actionId) {
        switch (actionId){
            case 1://公告
                Notice();
                break;
            case 2://文件
                groupFile();
                break;
            case 3://禁言
                Nospeak();
                break;
            case 5://删除
                deleteMember();
                break;
            case 6:
                showAll();
                break;
        }
    }

    private void showAll() {
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
        setContentView(R.layout.delete);
        EaseTitleBar titleBar = (EaseTitleBar) findViewById(R.id.delete_titleBar);
        initial(titleBar,"群成员列表");
        titleBar.setRightImageResource(R.drawable.plus);
        titleBar.setRightLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupControl.this,AddMember.class);
                intent.putExtra("groupId",groupId);
                startActivity(intent);
            }
        });
        final ListView lv = (ListView) findViewById(R.id.deleteList);
        all = group.getMembers();
        for(int i=0;i<all.size();i++){
            if(all.get(i).equals(group.getOwner())){
                all.set(i,group.getOwner()+"(群主大人)");
                break;
            }
        }
        Deleteadapter adapter = new Deleteadapter(GroupControl.this,R.layout.delete_item,all);
        lv.setAdapter(adapter);
    }

    private void deleteMember() {
        Log.d("GroupControl","1"+String.valueOf(groupId));
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
        setContentView(R.layout.delete);
        EaseTitleBar titleBar = (EaseTitleBar) findViewById(R.id.delete_titleBar);
        initial(titleBar,"群成员列表");
        final ListView lv = (ListView) findViewById(R.id.deleteList);
        all = group.getMembers();
        Deleteadapter adapter = new Deleteadapter(GroupControl.this,R.layout.delete_item,all);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GroupControl.this);
                final String name = all.get(position);
                if(name.equals(group.getOwner())){
                    Toast.makeText(GroupControl.this, "不能删除自己哦~~", Toast.LENGTH_SHORT).show();
                    return;
                }
                builder.setMessage("确定要删除群成员："+name+"吗？");
                builder.setTitle("好友申请");
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
                                    EMClient.getInstance().groupManager().removeUserFromGroup(groupId, name);//需异步处理
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(GroupControl.this, "您已移除成员："+name, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } catch (HyphenateException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                        Intent intent = new Intent(GroupControl.this, GroupDetailActivity.class);
                        intent.putExtra("name",groupId);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.show();
            }
        });
    }


    private void Nospeak() {
        Toast.makeText(this, "该功能暂未开放，敬请期待！", Toast.LENGTH_SHORT).show();
    }


    private void groupFile() {

    }

    private void Notice() {
        setContentView(R.layout.notice);
        EaseTitleBar titleBar = (EaseTitleBar) findViewById(R.id.notice_title_bar);
        Button submit = (Button) findViewById(R.id.notice_submit);
        group = EMClient.getInstance().groupManager().getGroup(groupId);
        final EditText text = (EditText) findViewById(R.id.noticeEdit);
        submit.setVisibility(View.INVISIBLE);
        text.setVisibility(View.INVISIBLE);
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
        if(EMClient.getInstance().getCurrentUser().equals(group.getOwner())){
            submit.setVisibility(View.VISIBLE);
            text.setVisibility(View.VISIBLE);
        }
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = text.getText().toString();
                if(msg==null || msg.trim().length()<=0){
                    Toast.makeText(GroupControl.this, "公告不得为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                Notice notice = new Notice(groupId,msg);
                notice.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        try {
                            Toast.makeText(GroupControl.this, "正在提交！", Toast.LENGTH_SHORT).show();
                            Thread.sleep(1000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        onCreate(null);
                    }
                });
            }
        });
        initial(titleBar,"公告栏");
        final ListView lv = (ListView) findViewById(R.id.notictList);
        BmobQuery<Notice>query = new BmobQuery<Notice>();
        query.addWhereEqualTo("groupId",groupId);
        Log.d("groupId",groupId);
        query.setLimit(50);
        query.findObjects(new FindListener<Notice>() {
            @Override
            public void done(List<Notice> list, BmobException e) {
                if(e==null){
                    NoticeAdapter adapter = new NoticeAdapter(GroupControl.this,R.layout.notice_item,list);
                    lv.setAdapter(adapter);
                }
            }
        });
    }

    private void initial(EaseTitleBar titleBar, String s) {
        titleBar.setTitle(s);
        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    
}
