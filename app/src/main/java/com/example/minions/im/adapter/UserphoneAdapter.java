package com.example.minions.im.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.minions.im.R;
import com.hyphenate.easeui.adapter.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;


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
public class UserphoneAdapter extends ArrayAdapter<String> {
    private int resourceId;
    private SQLiteDatabase db;

    public UserphoneAdapter(Context context, int textViewResourceId, List<String> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final String user_phone = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        final ImageView img = (ImageView) view.findViewById(R.id.call_user_img);
        final TextView nickname = (TextView) view.findViewById(R.id.call_user_nickname);
        ImageView phone = (ImageView) view.findViewById(R.id.call_phone);
        ImageView message = (ImageView) view.findViewById(R.id.call_message);
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("telephone", user_phone);
        query.setLimit(100);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null && list.size() > 0) {
                    User user = list.get(0);
                    String name = user.getNickName();
                    final BmobFile bmobFile = user.getAvatar();
                    if (bmobFile != null) {
                        bmobFile.download(new DownloadFileListener() {
                            @Override
                            public void done(String s, BmobException e) {
                                //Toast.makeText(MainActivity.this, "下载成功", Toast.LENGTH_SHORT).show();
                                Log.d("MainActivity", "图片路径：" + s);
                                Bitmap bm = BitmapFactory.decodeFile(s);
                                img.setImageBitmap(bm);
                            }

                            @Override
                            public void onProgress(Integer integer, long l) {

                            }
                        });
                    }
                    nickname.setText(name);
                }
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+user_phone));
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
               getContext().startActivity(intent);
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri2 = Uri.parse("smsto:"+user_phone);
                Intent intentMessage = new Intent(Intent.ACTION_VIEW,uri2);
                getContext().startActivity(intentMessage);
            }
        });
        return view;
    }

}
