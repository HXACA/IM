package com.example.minions.im.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.minions.im.R;
import com.example.minions.im.activity.GameActivity;

public class DynamicFragment extends Fragment implements View.OnClickListener{

	private Button game_start;
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
							 @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=LayoutInflater.from(getActivity()).inflate(R.layout.fragment_dynamic, null);
		game_start = (Button) view.findViewById(R.id.btnGame);
		game_start.setOnClickListener(this);
		return view;
	}


	@Override
	public void onClick(View v) {
		int viewId = v.getId();
		if(viewId == R.id.btnGame){
			startGame();
		}
	}

	public void startGame(){
		Intent intent = new Intent(getActivity(), GameActivity.class);
		startActivity(intent);
	}

}
