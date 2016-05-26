package com.lingshi.erp.activity;

import com.lingshi.erp.R;

import android.os.Bundle;
import android.view.Window;

public class PersonalInfoActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.personal_info_layout);
	}
}
