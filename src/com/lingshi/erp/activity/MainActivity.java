package com.lingshi.erp.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.lingshi.erp.R;
import com.lingshi.erp.fragment.ContactsFragment;
import com.lingshi.erp.fragment.MeFragment;
import com.lingshi.erp.fragment.MessageFragment;

@SuppressLint("InflateParams")
public class MainActivity extends FragmentActivity {

	private FragmentTabHost mTabHost;

	private LayoutInflater mlayoutInflater;

	@SuppressWarnings("rawtypes")
	private Class[] mFragmentArray = { MessageFragment.class,
			ContactsFragment.class, MeFragment.class };

	private int[] mImageArray = { R.drawable.tab_message_btn,
			R.drawable.tab_contacts_btn, R.drawable.tab_more_btn };

	private String[] mTextArray = { "消息", "联系人", "我" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {
		mlayoutInflater = LayoutInflater.from(this);

		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabconten);
		for (int i = 0; i < mFragmentArray.length; i++) {// 把fragment放进主界面中
			TabSpec tabSpec = mTabHost.newTabSpec(mTextArray[i]).setIndicator(
					getTabContent(i));
			mTabHost.addTab(tabSpec, mFragmentArray[i], null);
			mTabHost.getTabWidget().getChildAt(i)
					.setBackgroundResource(R.drawable.selector_tab_background);
		}
	}

	private View getTabContent(int index) {
		View view = mlayoutInflater.inflate(R.layout.tabcontent, null);
		ImageView image = (ImageView) view.findViewById(R.id.image);
		image.setImageResource(mImageArray[index]);
		TextView text = (TextView) view.findViewById(R.id.text);
		text.setText(mTextArray[index]);
		return view;
	}
}
