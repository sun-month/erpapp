package com.lingshi.erp.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public abstract class Dom4jUtil {
	
	private static Logger logger=Logger.getLogger(Dom4jUtil.class);
	
	/**
	 * ����xml�����������ȡdom����
	 * @param input ��xml����������
	 * @return
	 */
	public static Document readXmlToDocument(InputStream input){
		SAXReader reader=new SAXReader();
		Document document =null;
		try {
			document = reader.read(input);
		} catch (DocumentException e) {
			logger.error("��ȡxml�ṹ���̳����쳣", e);
		}
		return document;
	}
	
	/**
	 * ����xml�ַ�����ȡxml dom����
	 * @param xmlStr xml�ַ���
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static Document readXmlToDocument(byte[] b) throws UnsupportedEncodingException{
		ByteArrayInputStream input = new ByteArrayInputStream(b);
		return readXmlToDocument(input);
	}
	
	/**
	 * ����reader��ȡxml dom����
	 * @param re
	 * @return
	 */
	public static Document readXmlToDocument(Reader re){
		SAXReader reader=new SAXReader();
		Document document =null;
		try {
			document = reader.read(re);
		} catch (DocumentException e) {
			logger.error("��ȡxml�ṹ���̳����쳣", e);
		}
		return document;
	}
	
	/**
	 * ��ȡĳ�ڵ��ĳ���Ե�ֵ
	 * @param element �ڵ����
	 * @param attributeName ��������
	 * @return
	 */
	public static String getElementAttribute(Element element,String attributeName){
		Attribute attribute=element.attribute(attributeName);
		return attribute!=null?attribute.getText():"";
	}

}
