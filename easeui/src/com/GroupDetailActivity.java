package com;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.adapter.NoShowRec;
import com.hyphenate.easeui.widget.EaseTitleBar;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

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
    private ImageButton GroupMangers;
    private String toChatName;
    private LinearLayout ownerShow;
    private TextView groupName;
    private Switch Noshow;
    private Button dissolve;
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
                        TextView textview = (TextView) findViewById(R.id.textView1);
                        textview.setVisibility(View.INVISIBLE);
                         ownerShow.setVisibility(View.VISIBLE);
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Noshow.setVisibility(View.VISIBLE);
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


    public void initListenter() {
        dissolve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GroupDetailActivity.this);
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
                                            Toast.makeText(GroupDetailActivity.this, "您已删除该群！", Toast.LENGTH_SHORT).show();
                                            onBackPressed();
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
        GroupNotice.setOnClickListener(this);
        GroupFile.setOnClickListener(this);
        GroupShutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GroupDetailActivity.this, "该功能暂未开放！", Toast.LENGTH_SHORT).show();
            }
        });
        GroupMangers.setOnClickListener(this);
        Noshow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().groupManager().blockGroupMessage(toChatName);
                               search(false);
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }else{
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                EMClient.getInstance().groupManager().unblockGroupMessage(toChatName);
                                search(true);
                            } catch (HyphenateException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
    }

    private void showResult(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GroupDetailActivity.this,s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        EaseTitleBar titleBar = (EaseTitleBar) findViewById(R.id.add_group_title);
        titleBar.setTitle("群管理");
        titleBar.setLeftLayoutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Noshow = (Switch) findViewById(R.id.switch2);
        Noshow.setVisibility(View.INVISIBLE);
        groupName = (TextView) findViewById(R.id.group_name);
        toChatName=getIntent().getStringExtra("name");
        GroupNotice = (ImageButton) findViewById(R.id.btn_notice);
        GroupFile = (ImageButton) findViewById(R.id.btn_file);
        GroupShutup = (ImageButton) findViewById(R.id.btn_speak);
        GroupMangers = (ImageButton) findViewById(R.id.btn_manager);
        ownerShow = (LinearLayout) findViewById(R.id.ownershow);
        ownerShow.setVisibility(View.INVISIBLE);
        dissolve = (Button) findViewById(R.id.dissolve);

    }


    public void search(final boolean flag){
        final String username = EMClient.getInstance().getCurrentUser();
        BmobQuery<NoShowRec>query1=new BmobQuery<>();
        query1.addWhereEqualTo("groupId",toChatName);
        BmobQuery<NoShowRec>query2=new BmobQuery<>();
        query2.addWhereEqualTo("phone",username);
        List<BmobQuery<NoShowRec>> query = new ArrayList<BmobQuery<NoShowRec>>();
        query.add(query1);
        query.add(query2);
        BmobQuery<NoShowRec>query3 = new BmobQuery<>();
        query3.and(query);
        query3.findObjects(new FindListener<NoShowRec>() {
            @Override
            public void done(List<NoShowRec> list, BmobException e) {
                if(e==null && list.size()>0 && flag){
                   // Toast.makeText(GroupDetailActivity.this, String.valueOf(list.size())+String.valueOf(list.get(1).getPhone()), Toast.LENGTH_SHORT).show();
                   for(int i=0;i<list.size();i++){
                       NoShowRec mmm = new NoShowRec(toChatName,username);
                       mmm.setObjectId(list.get(i).getObjectId());
                       mmm.delete(new UpdateListener() {
                           @Override
                           public void done(BmobException e) {

                           }
                       });
                   }
                }
                else if(!flag){
                    NoShowRec msg = new NoShowRec(toChatName,EMClient.getInstance().getCurrentUser());
                    msg.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {

                        }
                    });
                }

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        String username = EMClient.getInstance().getCurrentUser();
        BmobQuery<NoShowRec>query1=new BmobQuery<>();
        query1.addWhereEqualTo("groupId",toChatName);
        BmobQuery<NoShowRec>query2=new BmobQuery<>();
        query2.addWhereEqualTo("phone",username);
        List<BmobQuery<NoShowRec>> query = new ArrayList<BmobQuery<NoShowRec>>();
        query.add(query1);
        query.add(query2);
        BmobQuery<NoShowRec>query3 = new BmobQuery<>();
        query3.and(query);
        query3.findObjects(new FindListener<NoShowRec>() {
            @Override
            public void done(List<NoShowRec> list, BmobException e) {
                if(e==null && list.size()>0){
                    Toast.makeText(GroupDetailActivity.this, "屏蔽中", Toast.LENGTH_SHORT).show();
                    Noshow.setChecked(true);
                }
                else
                    Noshow.setChecked(false);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        Intent intent = new Intent(GroupDetailActivity.this, GroupControl.class);
        intent.putExtra("name",toChatName);
       if(id==R.id.btn_notice){
           intent.putExtra("type",1);
       }else if(id == R.id.btn_file){
           intent.putExtra("type",2);
       }else if(id ==R.id.btn_manager){
           intent.putExtra("type",5);
       }
       startActivity(intent);
    }
}
