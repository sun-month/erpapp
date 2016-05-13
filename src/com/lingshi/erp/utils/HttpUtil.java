package com.lingshi.erp.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

/**
 * Http������
 */
public class HttpUtil {
	// ����HttpClient����
	public static HttpClient httpClient = new DefaultHttpClient();
	public static final String BASE_URL = "";

	public interface HttpCallBackListener {

		void onFinish(String response);

		void onError(Exception e);
	}

	/**
	 * get����
	 * 
	 * @param url
	 *            ���������URL
	 * @return ��������Ӧ�ַ���
	 * @throws Exception
	 */
	public static void doGet(final String url,
			final HttpCallBackListener callback) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// ����HttpGet����
					HttpGet get = new HttpGet(url);
					// ����GET����
					HttpResponse httpResponse = httpClient.execute(get);
					// ����������ɹ��ط�����Ӧ
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						// ��ȡ��������Ӧ�ַ���
						HttpEntity entity = httpResponse.getEntity();
						InputStream content = entity.getContent();
						String convertStreamToString = convertStreamToString(content);
						callback.onFinish(convertStreamToString);
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * post����
	 * 
	 * @param url
	 *            ���������URL
	 * @param params
	 *            �������
	 * @return ��������Ӧ�ַ���
	 * @throws Exception
	 */
	public static void doPost(final String url,
			final Map<String, String> rawParams,
			final HttpCallBackListener callback) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// ����HttpPost����
					HttpPost post = new HttpPost(url);
					// ������ݲ��������Ƚ϶�Ļ����ԶԴ��ݵĲ������з�װ
					List<NameValuePair> params = new ArrayList<NameValuePair>();
					for (String key : rawParams.keySet()) {
						// ��װ�������
						params.add(new BasicNameValuePair(key, rawParams
								.get(key)));
					}
					// �����������
					post.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
					// ����POST����
					HttpResponse httpResponse = httpClient.execute(post);
					// ����������ɹ��ط�����Ӧ
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						// ��ȡ��������Ӧ�ַ���
						HttpEntity entity = httpResponse.getEntity();
						InputStream content = entity.getContent();
						String convertStreamToString = convertStreamToString(content);
						callback.onFinish(convertStreamToString);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * ��ȡ����������Ӧ��ת��Ϊ�ַ���
	 */
	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}