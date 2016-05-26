package com.lingshi.erp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.lingshi.erp.R;
import com.lingshi.erp.activity.MainActivity;
import com.lingshi.erp.activity.PersonalInfoActivity;

@SuppressLint("InflateParams")
public class MeFragment extends Fragment implements OnClickListener {

	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.me_fragment, null);
		view.findViewById(R.id.personal).setOnClickListener(this);
		return view;
	}

	public void refresh() {
	}

	@Override
	public void onClick(View v) {
		if (R.id.personal == v.getId()) {
			//跳转界面未实现，怎么获取 context？
		}
	}
}
