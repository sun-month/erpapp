package com.lingshi.erp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.lingshi.erp.utils.ActivityCollector;

public class BaseActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("BaseActivity", getClass().getSimpleName());
		ActivityCollector.addActivity(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}

	@SuppressWarnings("rawtypes")
	protected void actionStart(Context origin, Class target,
			Bundle bundle) {
		Intent intent = new Intent(origin, target);
		if (!bundle.isEmpty())
			intent.putExtras(bundle);
		startActivity(intent);
		finish();
	}
}
