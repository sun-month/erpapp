package com.lingshi.erp.activity;

import android.os.Bundle;
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

				// String url =
				// "http://192.168.1.18:8080/lingshi/loginAction_login.action";
				ServiceBus bus = new ServiceBus();
				bus.setService("loginAction_login");
				bus.getInMap().put("username", account);
				bus.getInMap().put("password", password);
				APIUtil.invoke(bus, new RequestCallback() {

					@Override
					public void success(ServiceBus bus) {
						// 处理返回结果，判断用户名密码是否正确
						Toast.makeText(LoginActivity.this,
								bus.getOutMap().toString(), Toast.LENGTH_SHORT)
								.show();
						Log.e("LoginActivity", bus.toString());
					}

					@Override
					public void error(String msg) {
						Toast.makeText(LoginActivity.this, "http连接错误",
								Toast.LENGTH_SHORT).show();
						Log.e("LoginActivity", msg);
					}
				});
				// HttpUtils http = new HttpUtils();
				// RequestParams params = new RequestParams();
				// params.addQueryStringParameter("user", account);
				// params.addQueryStringParameter("password", password);
				//
				// http.send(HttpRequest.HttpMethod.POST, url, params,
				// new RequestCallBack() {
				//
				// @Override
				// public void onFailure(HttpException error,
				// String msg) {
				// Toast.makeText(LoginActivity.this, "http链接异常！",
				// Toast.LENGTH_SHORT).show();
				// Log.e(getLocalClassName(), msg);
				// }
				//
				// @Override
				// public void onSuccess(ResponseInfo response) {
				// try {
				// Object result = response.result;
				// Toast.makeText(LoginActivity.this,
				// "result is " + result,
				// Toast.LENGTH_SHORT).show();
				// JSONObject object = new JSONObject(result
				// .toString());
				// // 待跳转
				// Intent intent = new Intent(
				// LoginActivity.this,
				// MainActivity.class);
				// Bundle extras = new Bundle();
				// extras.putString("username",
				// object.getString("name_f"));
				// extras.putString("usercode",
				// object.getString("code_f"));
				// extras.putInt("userid",
				// object.getInt("id_f"));
				// intent.putExtras(extras);
				// startActivity(intent);
				// finish();
				// } catch (JSONException e) {
				// e.printStackTrace();
				// }
				// }
				// });

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
