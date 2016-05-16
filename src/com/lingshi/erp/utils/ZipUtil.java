package com.lingshi.erp.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class ZipUtil {

	private static Logger logger = Logger.getLogger(ZipUtil.class);
	private static final int BUFFER = 1024;

	/**
	 * ��ѹzip/data
	 * 
	 * @param inputStream
	 * @return
	 */
	public static byte[] unzipdata(InputStream inputStream) {
		ZipEntry zipEntry = null;
		ZipInputStream zipIn = null;
		ByteArrayOutputStream output = null;

		byte[] bytes = null;
		try {
			zipIn = new ZipInputStream(inputStream);
			output = new ByteArrayOutputStream();
			while ((zipEntry = zipIn.getNextEntry()) != null) {
				if (!zipEntry.isDirectory()
						&& "data".equalsIgnoreCase(zipEntry.getName())) {
					byte[] b = new byte[1024];
					int read = zipIn.read(b);
					while (read != -1) {
						output.write(b, 0, read);
						read = zipIn.read(b);
					}
					bytes = output.toByteArray();

					break;
				}

			}
		} catch (Exception e) {
			logger.error("��ѹzip���̳����쳣", e);
		} finally {
			IOUtils.closeQuietly(output);
			IOUtils.closeQuietly(zipIn);
		}
		return bytes;
	}

	/**
	 * ѹ��zip/data
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] zipdata(String data) {
		ByteArrayOutputStream bout = null;
		ZipOutputStream gout = null;
		try {
			byte[] src = data.getBytes("UTF-8");
			ZipEntry zipEntry = new ZipEntry("data");
			bout = new ByteArrayOutputStream();
			gout = new ZipOutputStream(bout);
			gout.putNextEntry(zipEntry);
			gout.write(src);// д������
			gout.closeEntry();
			gout.finish();
			return bout.toByteArray();// ��ȡѹ���������
		} catch (Exception e) {
			logger.error("ѹ��zip���̳����쳣", e);
		} finally {
			IOUtils.closeQuietly(gout);
			IOUtils.closeQuietly(bout);
		}
		return null;
	}

	public static byte[] compress(String str) {
		try {
			byte[] btyes = str.getBytes("UTF-8");
			byte[] data = compress(btyes);
			return data;
		} catch (Exception ex) {
			return null;
		}
	}

	private static byte[] compress(byte[] data) throws Exception {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		// ѹ��
		compress(bais, baos);
		byte[] output = baos.toByteArray();
		baos.flush();
		baos.close();
		bais.close();
		return output;
	}

	private static void compress(InputStream is, OutputStream os)
			throws Exception {
		GZIPOutputStream gos = new GZIPOutputStream(os);
		int count;
		byte data[] = new byte[BUFFER];
		while ((count = is.read(data, 0, BUFFER)) != -1) {
			gos.write(data, 0, count);
		}
		gos.finish();
		gos.flush();
		gos.close();
	}

}
