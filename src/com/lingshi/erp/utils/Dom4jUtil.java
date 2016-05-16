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
	 * 根据xml输入流对象获取dom对象
	 * @param input 据xml输入流对象
	 * @return
	 */
	public static Document readXmlToDocument(InputStream input){
		SAXReader reader=new SAXReader();
		Document document =null;
		try {
			document = reader.read(input);
		} catch (DocumentException e) {
			logger.error("读取xml结构过程出现异常", e);
		}
		return document;
	}
	
	/**
	 * 根据xml字符串获取xml dom对象
	 * @param xmlStr xml字符串
	 * @return
	 * @throws UnsupportedEncodingException 
	 */
	public static Document readXmlToDocument(byte[] b) throws UnsupportedEncodingException{
		ByteArrayInputStream input = new ByteArrayInputStream(b);
		return readXmlToDocument(input);
	}
	
	/**
	 * 根据reader获取xml dom对象
	 * @param re
	 * @return
	 */
	public static Document readXmlToDocument(Reader re){
		SAXReader reader=new SAXReader();
		Document document =null;
		try {
			document = reader.read(re);
		} catch (DocumentException e) {
			logger.error("读取xml结构过程出现异常", e);
		}
		return document;
	}
	
	/**
	 * 获取某节点的某属性的值
	 * @param element 节点对象
	 * @param attributeName 属性名称
	 * @return
	 */
	public static String getElementAttribute(Element element,String attributeName){
		Attribute attribute=element.attribute(attributeName);
		return attribute!=null?attribute.getText():"";
	}

}
