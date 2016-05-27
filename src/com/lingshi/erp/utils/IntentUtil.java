package com.lingshi.erp.utils;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.content.Intent;

public class IntentUtil {
	public static void actionStart(Context context, Class<?> targetActivity,
			BasicNameValuePair... nameValuePairs) {
		Intent intent = new Intent(context, targetActivity);
		for (int i = 0; nameValuePairs != null && nameValuePairs.length > 0
				&& i < nameValuePairs.length; i++) {
			intent.putExtra(nameValuePairs[i].getName(),
					nameValuePairs[i].getValue());
		}
		context.startActivity(intent);
	}
}
