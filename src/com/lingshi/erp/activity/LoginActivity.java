package com.lingshi.erp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lingshi.erp.R;

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
			String account = accountEdit.getText().toString();
			String password = passwordEdit.getText().toString();
			if (account == null || "".equals(account)) {
				Toast.makeText(this, "请输入账号！", Toast.LENGTH_SHORT).show();
				return;
			}
			if (password == null || "".equals(password)) {
				Toast.makeText(this, "请输入密码！", Toast.LENGTH_SHORT).show();
				return;
			}
			if (valida(account, password)) {
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
				finish();
			} else {
				Toast.makeText(this, "账号或密码错误！", Toast.LENGTH_SHORT).show();
			}
		}
	}

	private boolean valida(String account, String password) {
		// http连接后台获取用户名密码验证
		
		return true;
	}
}
