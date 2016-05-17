package com.lingshi.erp.activity;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lingshi.erp.R;
import com.lingshi.erp.callback.RequestCallback;
import com.lingshi.erp.utils.APIUtil;
import com.lingshi.erp.utils.MD5Util;
import com.lingshi.erp.web.ServiceBus;

@SuppressLint("HandlerLeak")
public class LoginActivity extends BaseActivity implements OnClickListener {

	public static final int SUCCESS_MSG = 1;
	public static final int ERROR_MSG = 0;

	private EditText accountEdit;
	private EditText passwordEdit;
	private Button loginButton;

	private Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SUCCESS_MSG:
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
				finish();
				break;
			case ERROR_MSG:
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
		loginButton.setOnClickListener(this);
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
						msg.what = SUCCESS_MSG;
						msg.obj = bus.getOutMap();
						handler.sendMessage(msg);
					}

					@Override
					public void error(String msg) {
						Log.e(getPackageName(), msg);

						Message message = new Message();
						message.what = ERROR_MSG;
						handler.sendMessage(message);
					}
				});

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
