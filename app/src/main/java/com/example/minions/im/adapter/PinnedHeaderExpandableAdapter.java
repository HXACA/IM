package com.example.minions.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.minions.im.R;
import com.example.minions.im.activity.AddFriendActivity;
import com.example.minions.im.activity.ChatActivity;
import com.example.minions.im.view.CircleImageView;
import com.example.minions.im.view.PinnedHeaderExpandableListView;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.adapter.User;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;

public class PinnedHeaderExpandableAdapter extends  BaseExpandableListAdapter implements PinnedHeaderExpandableListView.HeaderAdapter {
	private String[][] childrenData;
	private String[] groupData;
	private Context context;
	private PinnedHeaderExpandableListView listView;
	private LayoutInflater inflater;
	
	public PinnedHeaderExpandableAdapter(String[][] childrenData, String[] groupData
			, final Context context, PinnedHeaderExpandableListView listView){
		this.groupData = groupData; 
		this.childrenData = childrenData;
		this.context = context;
		this.listView = listView;
		inflater = LayoutInflater.from(this.context);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childrenData[groupPosition][childPosition];
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
							 boolean isLastChild, View convertView, ViewGroup parent) {
		View view = null;  
        if (convertView != null) {  
            view = convertView;  
        } else {  
            view = createChildrenView();  
        }
		final CircleImageView img = (CircleImageView) view.findViewById(R.id.groupIcon);
        final TextView text1 = (TextView)view.findViewById(R.id.childto);
		BmobQuery<User> query = new BmobQuery<>();
		Log.d("FriendActivity", "friendId :" + childrenData[groupPosition][childPosition]);
		query.addWhereEqualTo("telephone", childrenData[groupPosition][childPosition]);
		query.findObjects(new FindListener<User>() {
			@Override
			public void done(List<User> list, BmobException e) {
				if (e == null) {
					final User user = list.get(0);
					final BmobFile bmobFile = user.getAvatar();
					bmobFile.download(new DownloadFileListener() {
						@Override
						public void done(String s, BmobException e) {
							Log.d("FriendActivity", "下载成功");
							Log.d("FriendActivity", "图片路径：" + s);
							Bitmap bm = BitmapFactory.decodeFile(s);
							img.setImageBitmap(bm);
							int state = user.getState();
							String string;
							if(state==1)
								string="在线";
							else
								string="离线";
							text1.setText(user.getNickName() + "(" + string + ")");
						}

						@Override
						public void onProgress(Integer integer, long l) {

						}
					});
				}
			}
		});
        //text1.setText(childrenData[groupPosition][childPosition]);
		text1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("adapter","haha");
				Intent intent = new Intent(context, ChatActivity.class);
				//传递参数
				EMConversation conversation;
				intent.putExtra(EaseConstant.EXTRA_USER_ID,childrenData[groupPosition][childPosition]);

				context.startActivity(intent);
			}
		});
		img.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, AddFriendActivity.class);
				intent.putExtra("name",childrenData[groupPosition][childPosition]);
				context.startActivity(intent);
			}
		});
        return view;    
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		for(int i=0;i<childrenData[groupPosition].length;i++){
			if(childrenData[groupPosition][i]==null || childrenData[groupPosition][i].equals(""))
				return i;
		}
		return childrenData[groupPosition].length;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groupData[groupPosition];
	}

	@Override
	public int getGroupCount() {
		return groupData.length;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View view = null;  
        if (convertView != null) {  
            view = convertView;  
        } else {  
            view = createGroupView();  
        } 
        
        ImageView iv=(ImageView) view.findViewById(R.id.groupIcon);
		if (isExpanded) {
			iv.setImageResource(R.drawable.btn_browser2);
		}
		else{
			iv.setImageResource(R.drawable.btn_browser);
		}
        
        TextView text = (TextView)view.findViewById(R.id.groupto);
        text.setText(groupData[groupPosition]);  
        return view;  
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	private View createChildrenView() {
		return inflater.inflate(R.layout.child, null);
	}
	
	private View createGroupView() {
		return inflater.inflate(R.layout.group, null);
	}

	@Override
	public int getHeaderState(int groupPosition, int childPosition) {
		final int childCount = getChildrenCount(groupPosition);
		if (childPosition == childCount - 1) {
			return PINNED_HEADER_PUSHED_UP;
		} else if (childPosition == -1
				&& !listView.isGroupExpanded(groupPosition)) {
			return PINNED_HEADER_GONE;
		} else {
			return PINNED_HEADER_VISIBLE;
		}
	}

	@Override
	public void configureHeader(View header, int groupPosition,
			int childPosition, int alpha) {
		String groupData =  this.groupData[groupPosition];
		((TextView) header.findViewById(R.id.groupto)).setText(groupData);
		
	}
	
	private SparseIntArray groupStatusMap = new SparseIntArray(); 
	
	@Override
	public void setGroupClickStatus(int groupPosition, int status) {
		groupStatusMap.put(groupPosition, status);
	}

	@Override
	public int getGroupClickStatus(int groupPosition) {
		if (groupStatusMap.keyAt(groupPosition)>=0) {
			return groupStatusMap.get(groupPosition);
		} else {
			return 0;
		}
	}


}

