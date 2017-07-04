package com.example.minions.im.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.minions.im.R;
import com.example.minions.im.activity.GroupActivity;
import com.example.minions.im.activity.MainActivity;
import com.example.minions.im.activity.friendAskActivity;
import com.example.minions.im.adapter.PinnedHeaderExpandableAdapter;
import com.example.minions.im.view.PinnedHeaderExpandableListView;
import com.example.minions.im.view.RefreshableView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.security.acl.Group;
import java.util.List;

public class ContactFragment extends Fragment implements RefreshableView.RefreshListener {
    private ImageButton newFriend;
    private ImageButton group;
    private RefreshableView mRefreshableView;
    private PinnedHeaderExpandableListView explistview;
    private String[][] childrenData = new String[5][10];
    private String[] groupData = {"我的好友", "朋友", "家人", "同学", "同事"};
    private int expandFlag = -1;//控制列表的展开
    private List<String> usernames;
    private PinnedHeaderExpandableAdapter adapter;
    private ImageView redpoint;
    Handler handler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            mRefreshableView.finishRefresh();
            Toast.makeText(getActivity(), R.string.toast_text, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
                    for (int i = 0; i < 5; i++) {
                        for (int j = 0; j < 10; j++) {
                            childrenData[i][j] = "";
                        }
                    }
                    for (int i = 0; i < 5; i++) {
                        for (int j = 0; j < usernames.size(); j++) {
                            childrenData[i][j] = usernames.get(j);
                        }
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_contact, null);
        redpoint = (ImageView) view.findViewById(R.id.redpoint);
       redpoint.setVisibility(View.INVISIBLE);
        database();
        newFriend = (ImageButton) view.findViewById(R.id.newFriend);
        newFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),friendAskActivity.class);
                startActivity(intent);
            }
        });
        group = (ImageButton) view.findViewById(R.id.group);
        group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),GroupActivity.class);
                startActivity(intent);
            }
        });
        initView(view);
        return view;
    }

    private void database() {
        SQLiteDatabase db;
        db = getActivity().openOrCreateDatabase("User.db", Context.MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("select * from FriendAsk",new String[]{});
       cursor.moveToFirst();
        for(int i=0;i<cursor.getCount();i++){
            Integer type =  cursor.getInt(cursor.getColumnIndex("f_type"));
            if(type==0){
                redpoint.setVisibility(View.VISIBLE);
            }
            cursor.moveToNext();
        }
    }

    private void initView(View view) {
        // TODO Auto-generated method stub
        mRefreshableView = (RefreshableView) view.findViewById(R.id.refresh_contact);
        explistview = (PinnedHeaderExpandableListView) view.findViewById(R.id.explistview);
        initData();
    }

    private void initData() {
        mRefreshableView.setRefreshListener(this);


        //设置悬浮头部VIEW
        explistview.setHeaderView(LayoutInflater.from(getActivity()).inflate(R.layout.group_head,
                explistview, false));
        adapter = new PinnedHeaderExpandableAdapter(childrenData, groupData, getActivity(), explistview);
        explistview.setAdapter(adapter);

        //设置单个分组展开
        explistview.setOnGroupClickListener(new GroupClickListener());
        setListViewHeightBasedOnChildren(explistview);
    }

    //实现刷新RefreshListener 中方法
    public void onRefresh(RefreshableView view) {
        //伪处理
        handler.sendEmptyMessageDelayed(1, 1000);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);


    }

    class GroupClickListener implements OnGroupClickListener {
        @Override
        public boolean onGroupClick(ExpandableListView parent, View v,
                                    int groupPosition, long id) {
            if (expandFlag == -1) {
                // 展开被选的group
                explistview.expandGroup(groupPosition);
                // 设置被选中的group置于顶端
                explistview.setSelectedGroup(groupPosition);
                expandFlag = groupPosition;
                setListViewHeightBasedOnChildren(explistview);
            } else if (expandFlag == groupPosition) {
                explistview.collapseGroup(expandFlag);
                expandFlag = -1;
                setListViewHeightBasedOnChildren(explistview);
            } else {
                explistview.collapseGroup(expandFlag);
                // 展开被选的group
                explistview.expandGroup(groupPosition);
                // 设置被选中的group置于顶端
                explistview.setSelectedGroup(groupPosition);
                expandFlag = groupPosition;
                setListViewHeightBasedOnChildren(explistview);
            }
            return true;
        }
    }

    // 设置listview的高度 否则在scrollview中只显示一行
    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
}
