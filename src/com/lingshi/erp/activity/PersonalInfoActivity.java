package com.lingshi.erp.activity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.lingshi.erp.R;
import com.lingshi.erp.utils.IntentUtil;

public class PersonalInfoActivity extends BaseActivity implements
		OnClickListener {
	private float x1 = 0f;
	private float x2 = 0f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.personal_info_layout);
		findViewById(R.id.return_exe).setOnClickListener(this);
		findViewById(R.id.image_view_return).setOnClickListener(this);
		findViewById(R.id.head).setOnClickListener(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			// 当手指按下的时候
			x1 = event.getX();
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			// 当手指离开的时候
			x2 = event.getX();
			if (x2 - x1 > 50) {
				IntentUtil.actionStart(this, MainActivity.class);
				overridePendingTransition(R.anim.pull_in_left,
						R.anim.push_out_right);
			}
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void onClick(View v) {
		if (R.id.return_exe == v.getId() || R.id.image_view_return == v.getId()) {
			IntentUtil.actionStart(this, MainActivity.class);
			overridePendingTransition(R.anim.pull_in_left,
					R.anim.push_out_right);
		} else if (R.id.head == v.getId()) {
			IntentUtil.actionStart(this, PersonHeadActivity.class);
			overridePendingTransition(R.anim.pull_in_right,
					R.anim.push_out_left);
		}
	}

}
