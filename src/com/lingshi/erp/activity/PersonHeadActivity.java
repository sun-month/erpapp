package com.lingshi.erp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

import com.lingshi.erp.R;
import com.lingshi.erp.utils.IntentUtil;

public class PersonHeadActivity extends Activity implements OnClickListener {
	private float x1 = 0f;
	private float x2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.personal_head_layout);
		findViewById(R.id.return_exe).setOnClickListener(this);
		findViewById(R.id.image_view_return).setOnClickListener(this);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (MotionEvent.ACTION_DOWN == event.getAction()) {
			x1 = event.getX();
		}
		if (MotionEvent.ACTION_UP == event.getAction()) {
			x2 = event.getX();
			if (x2 - x1 > 50) {
				IntentUtil.actionStart(this, PersonalInfoActivity.class);
				overridePendingTransition(R.anim.pull_in_left,
						R.anim.push_out_right);
			}
		}

		return super.onTouchEvent(event);
	}

	@Override
	public void onClick(View v) {
		IntentUtil.actionStart(this, PersonalInfoActivity.class);
		overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
	}

}
