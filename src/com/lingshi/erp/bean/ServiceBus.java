package com.lingshi.erp.bean;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Element;

import android.annotation.SuppressLint;

public final class ServiceBus {
	
	private String service;
 
	private Map<String, Object> headerMap=new HashMap<String, Object>();
	private Map<String, Object> inMap=new HashMap<String, Object>();
	private Map<String, Object> outMap=new HashMap<String, Object>();

	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
 
	public Map<String, Object> getHeaderMap() {
		return headerMap;
	}
	public void setHeaderMap(Map<String, Object> headerMap) {
		this.headerMap = headerMap;
	}
	public Map<String, Object> getInMap() {
		return inMap;
	}
	public void setInMap(Map<String, Object> inMap) {
		this.inMap = inMap;
	}
	public Map<String, Object> getOutMap() {
		return outMap;
	}
	public void setOutMap(Map<String, Object> outMap) {
		this.outMap = outMap;
	}

	public String ToXML()
	{
		StringBuilder xml = new StringBuilder();
		xml.append("<serviceBus service=\"" + service + "\">");
	
		if(!headerMap.isEmpty())
		{
			xml.append("<header>");
	
			Set<Map.Entry<String, Object>> set = headerMap.entrySet();        
			for (Iterator<Map.Entry<String, Object>> it = set.iterator(); it.hasNext();) 
			{           
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();  
				
				xml.append(GetItemXML(entry.getKey(), entry.getValue()));
			}
	
			xml.append("</header>");
		}
		
		if(!inMap.isEmpty())
		{
			xml.append("<in>");
	
			Set<Map.Entry<String, Object>> set = inMap.entrySet();        
			for (Iterator<Map.Entry<String, Object>> it = set.iterator(); it.hasNext();) 
			{           
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();  
				
				xml.append(GetItemXML(entry.getKey(), entry.getValue()));
			}
	
			xml.append("</in>");
		}
		
		if(!outMap.isEmpty())
		{
			xml.append("<out>");
	
			Set<Map.Entry<String, Object>> set = outMap.entrySet();        
			for (Iterator<Map.Entry<String, Object>> it = set.iterator(); it.hasNext();) 
			{           
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();  
				
				xml.append(GetItemXML(entry.getKey(), entry.getValue()));
			}
	
			xml.append("</out>");
		}
	
		xml.append("</serviceBus>");
		return xml.toString();
	}
	/**
	 * 是否包含XML关键字符< ' " & >
	 */ 
	private static Boolean HasXmlChar(String value)
	{
		if(value == null || value.length()==0)
			return false;
		for(int i=0; i<value.length(); i++)
		{
			char c = value.charAt(i);
			switch(c)
			{
				case '<':
				case '>':
				case '&':
				case '\"':
				case '\'':
					return true;
			}
		}
		return false;
	}
 
	/**
	 * 替换< ' " & >字符
	 */
	public static String EscapeXML(String value)
	{
		if (!HasXmlChar(value))
		{
			return value;
		}
		
		return value.replaceAll("&", "&amp;").
				replaceAll("<", "&lt;").
				replaceAll(">", "&gt;").
				replaceAll("\'", "&apos;").
				replaceAll("\"", "&quot;");
	}
 
	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static String GetItemXML(String name, Object value)
	{
		StringBuilder xml = new StringBuilder();
		if(name!=null)
			xml.append("<set name=\"" + name + "\" ");
		else
			xml.append("<set ");
		if(value instanceof Integer)
		{
			xml.append("type=\"int\">").append(value.toString()).append("</set>");
		}
		else if(value instanceof Boolean)
		{
			xml.append("type=\"bool\">").append(value.toString()).append("</set>");
		}
		else if(value instanceof Double || value instanceof BigDecimal || value instanceof Long)
		{
			xml.append("type=\"number\">").append(value.toString()).append("</set>");
		}
		else if(value instanceof Date)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			xml.append("type=\"date\">").append(sdf.format((Date)value)).append("</set>");
		}
		else if(value instanceof Map)
		{
			xml.append("type=\"entity\">");
			xml.append("<entity>");
			
			Map map = (Map)value;
		 
			Set<Map.Entry<String, Object>> set = map.entrySet();        
			for (Iterator<Map.Entry<String, Object>> it = set.iterator(); it.hasNext();) 
			{           
				Map.Entry<String, Object> entry = (Map.Entry<String, Object>) it.next();  
				
				xml.append(GetItemXML(entry.getKey(), entry.getValue()));
			}
			
			xml.append("</entity>");
			xml.append("</set>");
		}
		else if(value instanceof List)
		{
			xml.append("type=\"array\">");
			List list = (List)value;
			for(Object item : list)
			{
				xml.append(GetItemXML(null,item));
			}
 
			xml.append("</set>");
		}
		else if(value!=null)
		{
			xml.append("type=\"string\">").append(EscapeXML(value.toString())).append("</set>");
			//xml.append("type=\"string\"><![CDATA[").append(value.toString()).append("]]></set>");
		}
		else
		{
			xml.append("type=\"null\"/>");
		}
 
		return xml.toString();

	}
 
	@SuppressWarnings("rawtypes")
	public void FromXML(Element root)
	{
		service = root.attributeValue("service");
		
		Element child = root.element("header");
		if(child!=null && child.elements().size()>0)
		{
			for(
			Iterator it=child.elementIterator();it.hasNext();)
			{
				Element e = (Element) it.next();
				headerMap.put(e.attributeValue("name"), Create(e));
			}
		}
		
		child = root.element("in");
		if(child!=null && child.elements().size()>0)
		{
			for(
			Iterator it=child.elementIterator();it.hasNext();)
			{
				Element e = (Element) it.next();
				inMap.put(e.attributeValue("name"), Create(e));
			}
		}
 
		child = root.element("out");
		if(child!=null && child.elements().size()>0)
		{
			for(
			Iterator it=child.elementIterator();it.hasNext();)
			{
				Element e = (Element) it.next();
				outMap.put(e.attributeValue("name"), Create(e));
			}
		}
	}
	
	@SuppressLint("SimpleDateFormat")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Object Create(Element e)
	{
		String type = e.attributeValue("type");
		if(type==null || type.equalsIgnoreCase(""))
			type = "string";
		
		if(type.equalsIgnoreCase("string"))
		{
			return e.getText();
		}
		else if(type.equalsIgnoreCase("int"))
		{
			return Integer.valueOf(e.getText());
		}
		else if(type.equalsIgnoreCase("bool"))
		{
			return Boolean.parseBoolean(e.getText());
		}
		else if(type.equalsIgnoreCase("number"))
		{
			return Double.valueOf(e.getText());
		}
		else if(type.equalsIgnoreCase("date"))
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				return sdf.parse(e.getText());
			} catch (ParseException e1) {
				e1.printStackTrace();
				return null;
			}
		}
		else if(type.equalsIgnoreCase("entity"))
		{
			Map map = new HashMap();
			Element entity = e.element("entity");
			if(entity !=null && entity.elements().size()>0)
			{
				for(
				Iterator it=entity.elementIterator();it.hasNext();)
				{
					Element child = (Element) it.next();
					map.put(child.attributeValue("name"), Create(child));
				}
			}
			
			return map;
		}
		else if(type.equalsIgnoreCase("array"))
		{
			List list = new ArrayList();
			if(e.elements().size()>0)
			{
				for(
				Iterator it=e.elementIterator();it.hasNext();)
				{
					Element child = (Element) it.next();
					list.add(Create(child));
				}
			}
			
			return list;
		}
		else if(type.equalsIgnoreCase("null"))
		{
			return null;
		}
		
		throw new RuntimeException("ServiceBus.Create不支持类型" + type + "！");
	}
	
}
