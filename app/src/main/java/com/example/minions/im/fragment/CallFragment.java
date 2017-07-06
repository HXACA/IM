package com.example.minions.im.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.minions.im.R;
import com.example.minions.im.adapter.UserphoneAdapter;
import com.example.minions.im.view.RefreshableView;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

public class CallFragment extends Fragment implements
		RefreshableView.RefreshListener {
	private ListView lv;
	private RefreshableView mRefreshableView;
	private List<String> usernames;
	Handler handler = new Handler() {
		public void handleMessage(Message message) {
			super.handleMessage(message);
			mRefreshableView.finishRefresh();
			Toast.makeText(getActivity(), R.string.toast_text, Toast.LENGTH_SHORT).show();
		};
	};
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
							 @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_call, null);
		initView(view);
		return view;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		mRefreshableView = (RefreshableView) view.findViewById(R.id.refresh_call);
		lv = (ListView) view.findViewById(R.id.user_num);
		initData();
	}
	private void initData() {
		mRefreshableView.setRefreshListener(this);
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					usernames = EMClient.getInstance().contactManager().getAllContactsFromServer();
					final UserphoneAdapter adapter = new UserphoneAdapter(getContext(),R.layout.num_item,usernames);
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							lv.setAdapter(adapter);
						}
					});
				} catch (HyphenateException e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

	//实现刷新RefreshListener 中方法
	public void onRefresh(RefreshableView view) {
		//伪处理
		handler.sendEmptyMessageDelayed(1, 2000);

	}
}
