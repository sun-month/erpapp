package com.lingshi.erp.activity;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.lingshi.erp.R;
import com.lingshi.erp.callback.RequestCallback;
import com.lingshi.erp.utils.APIUtil;
import com.lingshi.erp.utils.DBUtil;
import com.lingshi.erp.utils.MD5Util;
import com.lingshi.erp.web.ServiceBus;

@SuppressLint("HandlerLeak")
public class LoginActivity extends BaseActivity implements OnClickListener {

	private DBUtil dbUtil;
	private CheckBox checkBox;

	private EditText accountEdit;
	private EditText passwordEdit;
	private Button loginButton;

	private Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case APIUtil.SUCCESS_MSG:
				Map<String, Object> map = (Map<String, Object>) msg.obj;
				if (map.isEmpty()) {
					Toast.makeText(LoginActivity.this, "用户名或密码错误！",
							Toast.LENGTH_SHORT).show();
					return;
				}
				Bundle bundle = new Bundle();
				Iterator<Entry<String, Object>> it = map.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<String, Object> entry = it.next();
					bundle.putString(entry.getKey(), entry.getValue()
							.toString());
				}
				actionStart(LoginActivity.this, MainActivity.class, bundle);

				if (checkBox.isChecked()) {
					checkBox.setChecked(false);
					map.put("isremember_f", 1);
				} else {
					map.put("isremember_f", 0);
				}
				Editable text = passwordEdit.getText();
				map.put("password_f", text);
				saveOrUpdate(map);// 在前端保存或者更新用户相关信息

				finish();
				break;
			case APIUtil.ERROR_MSG:
				Toast.makeText(LoginActivity.this, "网络连接异常", Toast.LENGTH_SHORT)
						.show();
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		accountEdit = (EditText) findViewById(R.id.account);
		passwordEdit = (EditText) findViewById(R.id.password);
		loginButton = (Button) findViewById(R.id.login);
		checkBox = (CheckBox) findViewById(R.id.check_box);
		loginButton.setOnClickListener(this);
		load();
	}

	@Override
	public void onClick(View v) {
		if (R.id.login == v.getId()) {
			try {
				String account = accountEdit.getText().toString();
				String password = passwordEdit.getText().toString();
				password = MD5Util.md5Encode(password);
				if (TextUtils.isEmpty(account)) {
					Toast.makeText(this, "请输入账号！", Toast.LENGTH_SHORT).show();
					return;
				}

				ServiceBus bus = new ServiceBus();
				bus.setService("loginAction_login");
				bus.getInMap().put("username", account);
				bus.getInMap().put("password", password);
				APIUtil.invoke(bus, new RequestCallback() {
					// 返回值发送到handler处理
					@Override
					public void success(ServiceBus bus) {
						Log.e(getPackageName(), bus.getInMap().toString());
						Message msg = new Message();
						msg.what = APIUtil.SUCCESS_MSG;
						msg.obj = bus.getOutMap();
						handler.sendMessage(msg);
					}

					@Override
					public void error(String msg) {
						Log.e(getPackageName(), msg);

						Message message = new Message();
						message.what = APIUtil.ERROR_MSG;
						handler.sendMessage(message);
					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void saveOrUpdate(Map<String, Object> map) {
		if (map.isEmpty())
			return;

		dbUtil = DBUtil.getInstance(this, "erpapp.db", 2);
		Iterator<Entry<String, Object>> it = map.entrySet().iterator();
		ContentValues values = new ContentValues();
		values.put("createtime_f", System.currentTimeMillis());// 记录用户登录时间
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			values.put(entry.getKey(), entry.getValue().toString());
		}
		dbUtil.saveOrUpdate("t_user", null, values);
	}

	private void load() {
		dbUtil = DBUtil.getInstance(this, "erpapp.db", 2);
		// 查询前端sqlite，以createtime_f作为最先登录时间，最大的就是上次登录用户。
		Map<String, Object> map = dbUtil.queryForMap("t_user", new String[] {
				"code_f", "password_f", "isremember_f" }, null, null, null,
				null, "createtime_f desc", "1");
		if (map.isEmpty()) {
			return;
		}
		String isremember_f = null;
		String password_f = null;
		Iterator<Entry<String, Object>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, Object> entry = it.next();
			if ("code_f".equals(entry.getKey())) {
				String value = entry.getValue().toString();
				accountEdit.setText(value);
			}
			if ("isremember_f".equals(entry.getKey())) {
				isremember_f = entry.getValue().toString();
			}
			if ("password_f".equals(entry.getKey())) {
				password_f = entry.getValue().toString();
			}
		}
		if ("1".equals(isremember_f)) {
			passwordEdit.setText(password_f);
			checkBox.setChecked(true);
		}
	}
}
