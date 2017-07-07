package com.example.minions.im.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.minions.im.R;
import com.example.minions.im.activity.DressUpActivity;
import com.example.minions.im.activity.LoginActivty;
import com.example.minions.im.activity.MyFileActivity;
import com.example.minions.im.activity.MyPhotoActivity;
import com.example.minions.im.activity.QQMoneyActivity;
import com.example.minions.im.activity.SaveActivity;
import com.example.minions.im.activity.VIPActivity;
import com.example.minions.im.adapter.SlideMenuAdapter;
import com.example.minions.im.view.CircleImageView;
import com.example.minions.im.view.XXListView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.adapter.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by LinYong on 2017/6/30.
 */

public class SlideFragment extends Fragment implements View.OnClickListener {
    private  String TIANQI_DAILY_WEATHER_URL = "https://api.seniverse.com/v3/weather/now.json";
    private URL u = null;
    private StringBuffer sb = null;
    private String TIANQI_API_SECRET_KEY = "trtovqcfjuw3xndc"; //

    private String TIANQI_API_USER_ID = "U563B16578"; //
    private XXListView lst_slide;
    private RelativeLayout rel_night;
    private LinearLayout lin_slide_wendu_location;
    private RelativeLayout rel_slide_set;
    private TextView name;
    private TextView locationText;
    private TextView temperatureText;
    private String city_name;
    private String city_temperature;
    private CircleImageView userAvatar;
    private TextView user_state;

    private String objectId;
    @Override
    @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.fragment_slide, null);
        initView(view);
        lst_slide.setAdapter(new SlideMenuAdapter(getActivity(), getData()));
        user_state = (TextView) view.findViewById(R.id.user_state);
        TextView exit = (TextView) view.findViewById(R.id.exit);
        BmobQuery<User>query = new BmobQuery<>();
        query.addWhereEqualTo("telephone",EMClient.getInstance().getCurrentUser());
        query.setLimit(50);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e==null && list.size()>0){
                    user_state.setText(list.get(0).getState()==1?"在线":"离线");
                }
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        EMClient.getInstance().logout(true);
                        BmobQuery<User> query = new BmobQuery<>();
                        query.addWhereEqualTo("telephone", EMClient.getInstance().getCurrentUser());
                        query.findObjects(new FindListener<User>() {
                            @Override
                            public void done(List<User> list, BmobException e) {
                                if (e == null) {
                                    User user = list.get(0);
                                    objectId = user.getObjectId();
                                    Log.d("MainActivity", "用户id：" + objectId);
                                    user.setState(0);
                                    user.update(objectId, new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                Log.d("Login", "用户已下线");
                                            }
                                        }
                                    });
                                }
                            }
                        });
                        Intent intent = new Intent(getActivity(),LoginActivty.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }).start();
            }
        });
        lst_slide.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                switch (position) {
                    case 0:// 激活会员
                        startActivity(new Intent().setClass(getActivity(),
                                VIPActivity.class));
                        break;
                    case 1:// QQ钱包
                        startActivity(new Intent().setClass(getActivity(),
                                QQMoneyActivity.class));
                        break;
                    case 2:// 个性装扮
                        startActivity(new Intent().setClass(getActivity(),
                                DressUpActivity.class));
                        break;
                    case 3:// 我的收藏
                        startActivity(new Intent().setClass(getActivity(),
                                SaveActivity.class));
                        break;
                    case 4:// 我的相册
                        startActivity(new Intent().setClass(getActivity(),
                                MyPhotoActivity.class));
                        break;
                    case 5:// 我的文件
                        startActivity(new Intent().setClass(getActivity(),
                                MyFileActivity.class));
                        break;
                }
            }
        });
        return view;
    }


    private void getWeather(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url = generateGetDiaryWeatherURL("Xiamen", "zh-Hans", "c", "1", "1");
                    u = new URL(url);
                    sb = new StringBuffer();
                    String line = null;
                    BufferedReader buffer = null;
                    HttpURLConnection urlConn = (HttpURLConnection) u.openConnection();
                    urlConn.setRequestMethod("GET");
                    urlConn.setConnectTimeout(8000);
                    urlConn.setReadTimeout(8000);
                    buffer = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                    while ((line = buffer.readLine()) != null) {
                        sb.append(line);
                    }
                    Log.d("URL:",url);
                } catch (SignatureException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String res =sb.toString();
                try {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray retdata = jsonObject.getJSONArray("results");
                    JSONObject location = retdata.getJSONObject(0).optJSONObject("location");
                    JSONObject daily = retdata.getJSONObject(0).optJSONObject("daily");
                    city_name = retdata.getJSONObject(0).optJSONObject("location").optString("name");
                    city_temperature = retdata.getJSONObject(0).optJSONObject("now").optString("temperature");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            locationText.setText(city_name);
                            temperatureText.setText(city_temperature);
                        }
                    });

                    Log.d("URL:",retdata.getJSONObject(0).optJSONObject("location").optString("name"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("URL:",e.toString());
                }
            }
        }).start();

    }

    public String generateGetDiaryWeatherURL(
            String location,
            String language,
            String unit,
            String start,
            String days
    )  throws SignatureException, UnsupportedEncodingException {
        String timestamp = String.valueOf(new Date().getTime());
        String params = "ts=" + timestamp + "&ttl=30&uid=" + TIANQI_API_USER_ID;
        String signature = URLEncoder.encode(generateSignature(params, TIANQI_API_SECRET_KEY), "UTF-8");
        return TIANQI_DAILY_WEATHER_URL + "?" + params + "&sig=" + signature + "&location=" + location + "&language=" + language + "&unit=" + unit + "&start=" + start + "&days=" + days;
    }


    private String generateSignature(String data, String key) throws SignatureException {
        String result;
        try {
            // get an hmac_sha1 key from the raw key bytes
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA1");
            // get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            // compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(data.getBytes("UTF-8"));
            result = new sun.misc.BASE64Encoder().encode(rawHmac);
        }
        catch (Exception e) {
            throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }


    private void initView(View view) {
        lst_slide = (XXListView) view.findViewById(R.id.lst_slide);
        // 夜间
        rel_night = (RelativeLayout) view.findViewById(R.id.lin_night);
        rel_night.setOnClickListener(this);
        // 温度和位置
        lin_slide_wendu_location = (LinearLayout) view
                .findViewById(R.id.lin_slide_wendu_location);
        lin_slide_wendu_location.setOnClickListener(this);
        // 设置
        rel_slide_set = (RelativeLayout) view.findViewById(R.id.rel_slide_set);
        rel_slide_set.setOnClickListener(this);
        locationText = (TextView) view.findViewById(R.id.txt_location);
        temperatureText = (TextView) view.findViewById(R.id.txt_wendu);
        //姓名
        name = (TextView) view.findViewById(R.id.txt_slide_nick);
        userAvatar = (CircleImageView) view.findViewById(R.id.civ_user_icon);
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("telephone", EMClient.getInstance().getCurrentUser());
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    User user = list.get(0);
                    name.setText(user.getNickName());
                    final BmobFile bmobFile = user.getAvatar();
                    bmobFile.download(new DownloadFileListener() {
                        @Override
                        public void done(String s, BmobException e) {
                            Bitmap bm = BitmapFactory.decodeFile(s);
                            userAvatar.setImageBitmap(bm);
                        }

                        @Override
                        public void onProgress(Integer integer, long l) {

                        }
                    });
                }
            }
        });

        getWeather();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    // listview的数据源
    public List<Map<String, Object>> getData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("icon", R.drawable.ic_tara);
        map.put("title", "激活会员");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("icon", R.drawable.ic_wallet);
        map.put("title", "OO钱包");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("icon", R.drawable.ic_slide_paint);
        map.put("title", "个性装扮");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("icon", R.drawable.ic_slide_save);
        map.put("title", "我的收藏");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("icon", R.drawable.ic_slide_photo);
        map.put("title", "我的相册");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("icon", R.drawable.ic_slide_file);
        map.put("title", "我的文件");
        list.add(map);

        return list;
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
