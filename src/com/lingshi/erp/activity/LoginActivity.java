package com.lingshi.erp.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lingshi.erp.R;
import com.lingshi.erp.utils.HttpUtil;
import com.lingshi.erp.utils.HttpUtil.HttpCallBackListener;
import com.lingshi.erp.utils.MD5Util;

public class LoginActivity extends BaseActivity implements OnClickListener {

	private EditText accountEdit;
	private EditText passwordEdit;
	private Button loginButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		accountEdit = (EditText) findViewById(R.id.account);
		passwordEdit = (EditText) findViewById(R.id.password);
		loginButton = (Button) findViewById(R.id.login);
		loginButton.setOnClickListener(this);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void onClick(View v) {
		if (R.id.login == v.getId()) {
			try {
				String account = accountEdit.getText().toString();
				String password = passwordEdit.getText().toString();
				password = MD5Util.md5Encode(password);
				if (account == null || "".equals(account)) {
					Toast.makeText(this, "«Î ‰»Î’À∫≈£°", Toast.LENGTH_SHORT).show();
					return;
				}
				String url = "http://192.168.1.18:8080/lingshi/loginAction_login.action";
				Map<String, String> map = new HashMap<String, String>();
				String md5Password = MD5Util.md5Encode(password);
				map.put("user", account);
				map.put("password", md5Password);
				HttpUtils http = new HttpUtils();
				http.send(HttpRequest.HttpMethod.GET, url,new RequestCallBack() {

					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(LoginActivity.this, "’À∫≈ªÚ√‹¬Î¥ÌŒÛ£°",
								Toast.LENGTH_SHORT).show();
						Log.e(getLocalClassName(), msg);
					}

					@Override
					public void onSuccess(ResponseInfo response) {
						Log.e(getLocalClassName(), response.toString());
						Intent intent = new Intent(LoginActivity.this,
								MainActivity.class);
						startActivity(intent);
						finish();
					}
				});
//				HttpUtil.doPost(url, map, new HttpCallBackListener() {
//
//					@Override
//					public void onFinish(String response) {
//						Log.e(getLocalClassName(), response);
//						Intent intent = new Intent(LoginActivity.this,
//								MainActivity.class);
//						startActivity(intent);
//						finish();
//					}
//
//					@Override
//					public void onError(Exception e) {
//						Toast.makeText(LoginActivity.this, "’À∫≈ªÚ√‹¬Î¥ÌŒÛ£°",
//								Toast.LENGTH_SHORT).show();
//						Log.e(getLocalClassName(), e.getMessage());
//					}
//				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
