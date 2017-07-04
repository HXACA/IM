package com.example.minions.im.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.minions.im.R;

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
public class friendAskAdapter extends ArrayAdapter<friendAsk> {
    private int resourceId;
    private SQLiteDatabase db;
    public friendAskAdapter(Context context, int textViewResourceId, List<friendAsk> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final friendAsk user = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        final TextView userName = (TextView)view.findViewById(R.id.fromName);
        final TextView userDescription = (TextView)view.findViewById(R.id.fromdes);
        userName.setText("昵称:"+user.getFromName());
        if(user.getDec()==null || user.getDec().length()<=0)
            userDescription.setText("无验证信息");
        else
        userDescription.setText("验证信息"+user.getDec());
        if(user.getType()==1){
            userDescription.setText("您已拒绝此申请");
        }else if(user.getType()==2){
            userDescription.setText("您已接受此申请");
        }
        return view;
    }
}
