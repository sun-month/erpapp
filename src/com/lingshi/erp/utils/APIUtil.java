package com.lingshi.erp.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.dom4j.Document;

import com.lingshi.erp.callback.RequestCallback;
import com.lingshi.erp.web.ServiceBus;

public class APIUtil {

	public static final String CONTENT_TYPE_ZIP_DATA = "zip/data";

	public static final String SERVER = "http://192.168.1.18:8080/lingshi/";

	public static void invoke(final ServiceBus bus,
			final RequestCallback callback) {
		final String action = SERVER + bus.getService() + ".action";
		new Thread(new Runnable() {

			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(action);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("POST");
					connection.setConnectTimeout(5000);
					connection.setReadTimeout(5000);
					connection.setDoInput(true);
					connection.setDoOutput(true);
					byte[] bs = ZipUtil.zipdata(bus.ToXML());
					// 声明式压缩数据
					connection.setRequestProperty("Content-Type",
							CONTENT_TYPE_ZIP_DATA);
					connection.setRequestProperty("Content-Length",
							String.valueOf(bs.length));

					OutputStream out = connection.getOutputStream();
					out.write(bs);

					int responseCode = connection.getResponseCode();
					if (responseCode == HttpURLConnection.HTTP_OK) {
						InputStream inputStream = connection.getInputStream();
						byte[] unzipdata = ZipUtil.unzipdata(inputStream);
						// 把返回结果转成servicebus
						ServiceBus rspBus = new ServiceBus();
						Document document = Dom4jUtil
								.readXmlToDocument(unzipdata);
						rspBus.FromXML(document.getRootElement());
						if (callback != null) {
							callback.success(rspBus);
						}
					}

				} catch (Exception e) {
					if (callback != null)
						callback.error(e.getMessage());
				} finally {
					if (connection != null)
						connection.disconnect();
				}
			}
		}).start();

	}
}
