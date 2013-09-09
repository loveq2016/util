package com.util.xml;

import java.io.*;
import java.util.*;

import org.dom4j.*;
import org.dom4j.tree.*;
import org.dom4j.io.*;

import com.util.string.StringUtil;

public final class Dom4jUtil {

	/**  
	 *  XML文档组装和解析工具类  
	 *  @author lrq  
	 * TODO To change the template for this generated type comment go to  
	 * Window - Preferences - Java - Code Style - Code Templates  
    */

	
	private static final String CHARSET_UTF8 = "UTF-8";//缺省字符集

	/**  
	 * 私有构造函数，阻止非法调用构造函数  
	 */
	private Dom4jUtil() {

	}

	/**   
	 * Return the child element with the given name.  The element must be in  
	 * the same name space as the parent element.  
	 * @param element The parent element  
	 * @param name The child element name  
	 * @return The child element  
	 */
	public static Element child(Element element, String name) {
		return element.element(new QName(name, element.getNamespace()));
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(String xml) {
		Map<String, String> returnDataMap = new HashMap<String, String>();

		Document document = null;
		try {
			document = DocumentHelper.parseText(xml);
		} catch (DocumentException e) {
			e.printStackTrace();
			return null;
		}
		Element root = document.getRootElement();

		for (Iterator it = root.attributeIterator(); it.hasNext();) {
			Attribute attribute = (Attribute) it.next();
			returnDataMap.put(attribute.getName(), attribute.getText());
		}

		for (Iterator it = root.elementIterator(); it.hasNext();) {
			Element element = (Element) it.next();
			returnDataMap.put(element.getName(), element.getText());
		}     
		return returnDataMap;
	}
	
	/**
	 * 
	 * 得到给定结点下的孩子节点
	 * 
	 * @param element
	 *            节点
	 * @param name
	 *            子节点名称
	 * @param optional
	 *            是否是可选的
	 * @return 子节点
	 * @throws Exception
	 */

	public static Element child(Element element, String name, boolean optional)
			throws Exception {

		Element child = element.element(new QName(name, element.getNamespace()));

		if (child == null && !optional) {
			throw new Exception(name + " element expected as child of " +
			element.getName() + ".");
		}
		return child;

	}

	/** 
	 * Return the child elements with the given name.  The elements must be in  

	    the same name space as the parent element.  

	    @param element The parent element  

	    @param name The child element name  

	    @return The child elements  

	 */

	public static List children(Element element, String name) {
		return element.elements(new QName(name, element.getNamespace()));

	}

	/**  

	 * 得到某个节点下的属性信息  

	 * @param element 节点  

	 * @param name 属性名  

	 * @param optional 是否是可选的  

	 * @return 值  

	 * @throws Exception  

	 */

	public static String getAttribute(Element element,

	String name,

	boolean optional)
	throws Exception {

		Attribute attr = null;

		if (element != null)

			attr = element.attribute(name);

		if (attr == null && !optional) {

			if (element != null)

				throw new Exception("Attribute " + name + " of " +

				element.getName() + " expected.");

			else

				return null;

		} else if (attr != null) {

			return attr.getValue();

		}

		else {

			return null;

		}

	}


	/**  
	 * 得到某个节点下的属性信息，值以字符串的形式返回  
	 * @param element 节点  
	 * @param name 属性名  
	 * @param optional 是否是可选的  
	 * @return 值  
	 * @throws Exception  
	 */

	public static String getAttributeAsString(Element element,String name,boolean optional)

	throws Exception {

		return getAttribute(element, name, optional);

	}

	/**  

	 * 得到某个节点下的属性信息，值以整数的形式返回。  

	 * 如果没有值或是转化为整形，那么抛出异常。  

	 * @param element 节点  

	 * @param name 属性名  

	 * @param optional 是否是可选的  

	 * @return 值  

	 * @throws Exception  

	 */

	public static int getAttributeAsInt(Element element,

	String name,

	boolean optional)

	throws Exception {

		try {

			return Integer.parseInt(getAttribute(element, name, optional));

		}

		catch (NumberFormatException exception) {

			throw new Exception(element.getName() + "/@" + name +

			" attribute: value format error.",

			exception);

		}

	}

	/**  

	 * 得到某个节点下的属性信息，值以整数的形式返回。  

	 * 如果该值是可选的，并且没有该值的话，就返回调用者提供缺省值。  

	 * @param element 节点  

	 * @param name 属性名  

	 * @param defaultValue 缺省值  

	 * @param optional 是否是可选的  

	 * @return 值  

	 * @throws Exception  

	 */

	public static int getAttributeAsInt(Element element,

	String name,

	int defaultValue,

	boolean optional)

	throws Exception {

		String value = getAttribute(element, name, optional);

		if ((optional) && ((value == null) || (value.equals("")))) {

			return defaultValue;

		}

		else {

			try {

				return Integer.parseInt(value);

			}

			catch (NumberFormatException exception) {

				throw new Exception(element.getName() + "/@" + name +

				" attribute: value format error.",

				exception);

			}

		}

	}

	/**  

	 * 得到某个节点下的属性信息，值以float的形式返回。  

	 * 如果没有值或是转化为float，那么抛出异常。  

	 * @param element 节点  

	 * @param name 属性名  

	 * @param optional 是否是可选的  

	 * @return 值  

	 * @throws Exception  

	 */

	public static float getAttributeAsFloat(Element element,

	String name,

	boolean optional)

	throws Exception {

		try {

			return Float.parseFloat(getAttribute(element, name, optional));

		}

		catch (NumberFormatException exception) {

			throw new Exception(element.getName() + "/@" + name +

			" attribute: value format error.",

			exception);

		}

	}

	/**  

	 * 得到某个节点下的属性信息，值以float的形式返回。  

	 * 如果没有值,返回缺省值；如果有，那么转化为float，如果不能转化那么抛出异常。  

	 * @param element 节点  

	 * @param name 属性名  

	 * @param defaultValue 缺省值  

	 * @param optional 是否是可选的  

	 * @return 值  

	 * @throws Exception  

	 */

	public static float getAttributeAsFloat(Element element,

	String name,

	float defaultValue,

	boolean optional)

	throws Exception {

		String value = getAttribute(element, name, optional);

		if ((optional) && ((value == null) || (value.equals("")))) {

			return defaultValue;

		}

		else {

			try {

				return Float.parseFloat(value);

			}

			catch (NumberFormatException exception) {

				throw new Exception(element.getName() + "/@" + name +

				" attribute: value format error.",

				exception);

			}

		}

	}

	/**  

	 * 得到某个节点下的属性信息，值以长整数的形式返回。  

	 * 如果没有值或是转化为整形，那么抛出异常。  

	 * @param element 节点  

	 * @param name 属性名  

	 * @param optional 是否是可选的  

	 * @return 值  

	 * @throws Exception  

	 */

	public static long getAttributeAsLong(Element element,

	String name,

	boolean optional)

	throws Exception {

		try {

			return Long.parseLong(getAttribute(element, name, optional));

		}

		catch (NumberFormatException exception) {

			throw new Exception(element.getName() + "/@" + name +

			" attribute: value format error.",

			exception);

		}

	}

	/**  

	 * 得到某个节点下的属性信息，值以整数的形式返回。  

	 * 如果该值是可选的，并且没有该值的话，就返回调用者提供缺省值。  

	 * @param element 节点  

	 * @param name 属性名  

	 * @param defaultValue 缺省值  

	 * @param optional 是否是可选的  

	 * @return 值  

	 * @throws Exception  

	 */

	public static long getAttributeAsLong(Element element,

	String name,

	long defaultValue,

	boolean optional)

	throws Exception {

		String value = getAttribute(element, name, optional);

		if ((optional) && ((value == null) || (value.equals("")))) {

			return defaultValue;

		}

		else {

			try {

				return Long.parseLong(value);

			}

			catch (NumberFormatException exception) {

				throw new Exception(element.getName() + "/@" + name +

				" attribute: value format error.",

				exception);

			}

		}

	}

	/**  

	 * 得到某个节点下的某名字的第一个孩子节点  

	 * @param element 节点  

	 * @param name 子节点名称  

	 * @param optional 是否是可选的  

	 * @return 值  

	 * @throws Exception  

	 */

	public static Element getFirstChild(Element element,

	String name,

	boolean optional)

	throws Exception {

		java.util.List list = element.elements(new QName(name, element
				.getNamespace()));

		//如果数目大于0，那么直接取第一个就可以了  

		if (list.size() > 0) {

			return (Element) list.get(0);

		}

		else {

			if (!optional) {

				throw new Exception(name +

				" element expected as first child of " +

				element.getName() + ".");

			}

			else {

				return null;

			}

		}

	}

	/**  

	 * 得到同名兄弟节点,同名的第一个节点，可以是自己  

	 * @param element 节点  

	 * @param optional 是否是可选的  

	 * @return 节点  

	 * @throws Exception  

	 */

	public static Element getSibling(Element element, boolean optional)

	throws Exception {

		return getSibling(element, element.getName(), optional);

	}

	/**  

	 * 按名称得到兄弟节点  

	 * @param element 节点  

	 * @param name 子节点名称  

	 * @param optional 是否是可选的  

	 * @return 节点  

	 * @throws Exception  

	 */

	public static Element getSibling(Element element,

	String name,

	boolean optional)

	throws Exception {

		java.util.List list = element.getParent().elements(name);

		if (list.size() > 0) {

			return (Element) list.get(0);

		}

		else {

			if (!optional) {

				throw new Exception(name + " element expected after " +

				element.getName() + ".");

			}

			else {

				return null;

			}

		}
	}

	/**  

	 * 得到给定节点的值,以字符串返回  

	 * @param element 节点  

	 * @param optional 是否是可选的  

	 * @return 值  

	 * @throws Exception  

	 */

	public static String getContent(Element element, boolean optional)

	throws Exception {

		String content = null;

		if (element != null)

			content = element.getText();

		if (content == null && !optional) {

			if (element != null)

				throw new Exception(element.getName() +

				" element: content expected.");

			else

				return null;

		} else {

			return content;

		}

	}

	/**  

	 * 得到给定节点的值,以字符串返回  

	 * @param element 节点  

	 * @param optional 是否是可选的  

	 * @return 值  

	 * @throws Exception  

	 */

	public static String getContentAsString(Element element, boolean optional)

	throws Exception {

		return getContent(element, optional);

	}

	/**  

	 * 得到给定节点的值,以整数类型返回  

	 * @param element 节点  

	 * @param optional 是否是可选的  

	 * @return 值  

	 * @throws Exception  

	 */

	public static int getContentAsInt(Element element, boolean optional)

	throws Exception {

		try {

			return Integer.parseInt(getContent(element, optional));

		}

		catch (NumberFormatException exception) {

			throw new Exception(element.getName() +

			" element: content format error.",

			exception);

		}

	}

	/**  

	 * 得到给定节点的值,以整数类型返回  

	 * @param element 节点  

	 * @param defaultValue 缺省值  

	 * @param optional 是否是可选的  

	 * @return 值  

	 * @throws Exception  

	 */

	public static int getContentAsInt(Element element,

	int defaultValue,

	boolean optional)

	throws Exception {

		String value = getContent(element, optional);

		if ((optional) && (value == null || value.equals(""))) {

			return defaultValue;

		}

		else {

			try {

				return Integer.parseInt(value);

			}

			catch (NumberFormatException exception) {

				throw new Exception(element.getName() +

				" element: content format error.",

				exception);

			}

		}

	}

	/**  

	 * 得到给定节点的值,以长整类型返回  

	 * @param element 节点  

	 * @param optional 是否是可选的  

	 * @return 值  

	 * @throws Exception  

	 */

	public static long getContentAsLong(Element element, boolean optional)

	throws Exception {

		try {

			return Long.parseLong(getContent(element, optional));

		}

		catch (NumberFormatException exception) {

			throw new Exception(element.getName() +

			" element: content format error.",

			exception);

		}

	}

	/**  

	 * 得到给定节点的值,以整数类型返回  

	 * @param element 节点  

	 * @param defaultValue 缺省值  

	 * @param optional 是否是可选的  

	 * @return 值  

	 * @throws Exception  

	 */

	public static long getContentAsLong(Element element,

	long defaultValue,

	boolean optional)

	throws Exception {

		String value = getContent(element, optional);

		if ((optional) && (value == null || value.equals(""))) {

			return defaultValue;

		}

		else {

			try {

				return Long.parseLong(value);

			}

			catch (NumberFormatException exception) {

				throw new Exception(element.getName() +

				" element: content format error.",

				exception);

			}

		}

	}

	/**  

	 * 得到给定节点的值,以浮点类型返回  

	 * @param element 节点  

	 * @param optional 是否是可选的  

	 * @return 值  

	 * @throws Exception  

	 */

	public static float getContentAsFloat(Element element, boolean optional)

	throws Exception {

		try {

			return Float.parseFloat(getContent(element, optional));

		}

		catch (NumberFormatException exception) {

			throw new Exception(element.getName() +

			" element: content format error.", exception);

		}

	}

	/**  

	 * 得到给定节点的值,以浮点类型返回  

	 * @param element 节点  

	 * @param defaultValue 缺省值  

	 * @param optional 是否是可选的  

	 * @return 值  

	 * @throws Exception  

	 */

	public static float getContentAsFloat(Element element,

	float defaultValue,

	boolean optional)

	throws Exception {

		String value = getContent(element, optional);

		if ((optional) && (value == null || value.equals(""))) {

			return defaultValue;

		}

		else {

			try {

				return Float.parseFloat(value);

			}

			catch (NumberFormatException exception) {

				throw new Exception(element.getName() +

				" element: content format error.",

				exception);

			}

		}

	}

	
	/**  

	 * 给定父节点和子节点名称，得到子节点值  

	 * @param root 父节点  

	 * @param subTagName 子节点  

	 * @return 值  

	 */

	public static String getSubTagValue(Element root, String subTagName) {

		String returnString = root.elementText(subTagName);

		return returnString;

	}

	/**  

	 * 给定父节点，子节点名称，孙节点名称；得到值  

	 * @param root   父节点  

	 * @param tagName 子节点名称  

	 * @param subTagName 孙节点名称  

	 * @return 值  

	 */

	public static String getSubTagValue(Element root,

	String tagName,

	String subTagName) {

		Element child = root.element(tagName);

		String returnString = child.elementText(subTagName);

		return returnString;

	}
	

	/**  
	 * 新Element节点，值为String类型  
	 * @param parent 父节点  
	 * @param name 新节点名称  
	 * @param value 新节点值  
	 * @return element  
	 * @throws Exception  
	 */
	public static Element appendChild(Element parent,

	String name,

	String value) {

		Element element = parent.addElement(new QName(name, parent
				.getNamespace()));

		if (value != null) {

			element.addText(value);

		}

		return element;

	}

	/**  

	 * 增加新Element节点，无值  

	 * @param parent 父节点  

	 * @param name 新节点名称  

	 * @return Element 新建节点  

	 * @throws Exception  

	 */

	public static Element appendChild(Element parent, String name) {

		return parent.addElement(new QName(name, parent.getNamespace()));

	}

	/**  

	 * 增加新Element节点，值为int类型  

	 * @param parent 父节点  

	 * @param name 新节点名称  

	 * @param value 新节点值  

	 * @return element  

	 * @throws Exception  

	 */

	public static Element appendChild(Element parent,

	String name,

	int value) {

		return appendChild(parent, name, String.valueOf(value));

	}

	/**  
	 * 增加新Element节点，值为长整形  

	 * @param parent 父节点  

	 * @param name 新节点名称  

	 * @param value 新节点值  

	 * @return element  

	 * @throws Exception  

	 */

	public static Element appendChild(Element parent,

	String name,

	long value) {

		return appendChild(parent, name, String.valueOf(value));

	}

	/**  

	 * 新加一个float值类型的节点，值为浮点型  

	 * @param parent 父节点  

	 * @param name 新节点的名称  

	 * @param value 新节点的值  

	 * @return element  

	 * @throws Exception  

	 */

	public static Element appendChild(Element parent,

	String name,

	float value) {

		return appendChild(parent, name, String.valueOf(value));

	}


	/**  

	 * 检查文档dtd定义是否正确  

	 * @param document 文档节点  

	 * @param dtdPublicId dtd定义  

	 * @return boolean  相同返回true,否则false  

	 */

	public static boolean checkDocumentType(Document document,

	String dtdPublicId) {

		DocumentType documentType = document.getDocType();

		if (documentType != null) {

			String publicId = documentType.getPublicID();

			return publicId != null && publicId.equals(dtdPublicId);

		}

		return true;

	}


	/**
	 * 通过Reader读取Document文档 
	 * @param in Reader器
	 * @param encoding 编码器
	 * @return documment
	 * @throws Exception
	 */

	public static Document readerToDocument(Reader in) {
		return readerToDocument(in, CHARSET_UTF8);
	}

	/**
	 * 通过Reader读取Document文档 如果encodingStr为null或是""，那么采用缺省编码GB2312
	 * @param in Reader器
	 * @param encoding 编码器
	 * @return documment
	 * @throws Exception
	 */

	public static Document readerToDocument(Reader in, String encoding) {
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(in, encoding);
			return document;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**  
	 * 给定输入流读取XML的Document。  
	 * 如果encodingStr为null或是""，那么采用缺省编码GB2312  
	 * @param inputSource 输入源  
	 * @param encoding 编码器  
	 * @return document  
	 * @throws Exception  
	 */

	public static Document inputStreamToDocument(InputStream inputSource) {
		return inputStreamToDocument(inputSource, CHARSET_UTF8);
	}

	/**  
	 * 给定输入流读取XML的Document。  
	 * 如果encodingStr为null或是""，那么采用缺省编码GB2312  
	 * @param inputSource 输入源  
	 * @param encoding 编码器  
	 * @return document  
	 * @throws Exception  
	 */

	public static Document inputStreamToDocument(InputStream inputSource,
			String encoding) {
		try {
			SAXReader reader = new SAXReader();
			Document document = reader.read(inputSource, encoding);
			return document;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**  

	 * 直接从字符串得到XML的Document  
	 * @param source 把一个字符串文本转化为XML的Document对象  
	 * @param encoding 编码器  
	 * @return <code>Document</code>  
	 * @throws Exception  
	 */

	public static Document stringToDocument(String source)  {
		return readerToDocument(new StringReader(source), CHARSET_UTF8);
	}
	
	/**  

	 * 直接从字符串得到XML的Document  
	 * @param source 把一个字符串文本转化为XML的Document对象  
	 * @param encoding 编码器  
	 * @return <code>Document</code>  
	 * @throws Exception  
	 */

	public static Document stringToDocument(String source, String encoding)  {
		return readerToDocument(new StringReader(source), encoding);
	}

	/**  

	 * 把XML的Document转化为java.io.Writer输出流  

	 * 不支持给定Schema文件的校验  

	 * @param document XML文档  

	 * @param outWriter 输出写入器  

	 * @param encoding 编码类型  

	 * @throws Exception 如果有任何异常转化为该异常输出  

	 */

	public static void toXML(Document document, Writer outWriter,

	String encoding)

	throws Exception {

		//  

		OutputFormat outformat = OutputFormat.createPrettyPrint();

		if (encoding == null || encoding.trim().equals("")) {

			encoding = CHARSET_UTF8;

		}

		//设置编码类型  

		outformat.setEncoding(encoding);

		XMLWriter xmlWriter = null;

		try {

			xmlWriter = new XMLWriter(outWriter, outformat);

			xmlWriter.write(document);

			xmlWriter.flush();

		}

		catch (java.io.IOException ex) {

			throw new Exception(ex);

		}

		finally {

			if (xmlWriter != null) {

				try {

					xmlWriter.close();

				}

				catch (java.io.IOException ex) {

				}

			}

		}

	}

	/**
	 * 
	 * 把XML的Document转化为java.io.Writer输出流 不支持给定Schema文件的校验
	 * @param document  XML文档
	 * @param outStream   输出写入器
	 * @param encoding  编码类型
	 * @throws Exception  如果有任何异常转化为该异常输出
	 * 
	 */

	public static void toXML(Document document, OutputStream outStream,
			String encoding)  {
		OutputFormat outformat = OutputFormat.createPrettyPrint();
		if (StringUtil.isEmpty(encoding)) {
			encoding = CHARSET_UTF8;
		}

		// 设置编码类型
		outformat.setEncoding(encoding);
		XMLWriter xmlWriter = null;
		try {
			xmlWriter = new XMLWriter(outStream, outformat);
			xmlWriter.write(document);
			xmlWriter.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (xmlWriter != null) {
				try {
					xmlWriter.close();
				} catch (java.io.IOException ex) {

				}
			}
		}
	}

	/**
	 * 把XML文档转化为String返回
	 * 
	 * @param document
	 *            要转化的XML的Document
	 * @param encoding
	 *            编码类型
	 * @return <code>String</code>
	 * @throws UnsupportedEncodingException
	 */
	public static String documentToString(Document document) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			toXML(document, stream, CHARSET_UTF8);
			if (stream != null) {
				stream.close();
			}
			return stream.toString(CHARSET_UTF8);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 把XML文档转化为String返回
	 * @param document 要转化的XML的Document
	 * @param encoding 编码类型
	 * @return <code>String</code>
	 * @throws Exception
	 *             如果有任何异常转化为该异常输出
	 */
	public static String documentToString(Document document, String encoding)
			throws Exception {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		toXML(document, stream, encoding);
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {

			}
		}
		return stream.toString(encoding);
	}
	
	
	/** 
     * 在根目录下插入一个元素 
     * @param srcXml  原xml 
     * @param nodeXml 元素xml 
     * @return 原xml插入节点后的完整xml文档 
     */ 
    public static String addElements(String srcXml, Element... elements) { 
        String resultXml = null; 
        try { 
            Document docSrc = DocumentHelper.parseText(srcXml); 
            Element parentElement = (Element) docSrc.getRootElement();
           
            for(Element element:elements)  {
            	parentElement.add(element);
            }
            resultXml = docSrc.asXML(); 
        } catch (DocumentException e) { 
            e.printStackTrace(); 
        } 
        return resultXml; 
    }
    
	/** 
     * 在根目录下插入一个元素 
     * @param srcXml  原xml 
     * @param nodeXml 元素xml 
     * @return 原xml插入节点后的完整xml文档 
     */ 
    public static String addElement(String srcXml, Element element) { 
        String resultXml = null; 
        try { 
            Document docSrc = DocumentHelper.parseText(srcXml); 
            Element parentElement = (Element) docSrc.getRootElement(); 
            parentElement.add(element); 
            resultXml = docSrc.asXML(); 
        } catch (DocumentException e) { 
            //log.error("在文档" + xpath + "位置添加新节点发生异常，请检查！"); 
            System.out.println(e.getMessage()); 
            e.printStackTrace(); 
        } 
        return resultXml; 
    }
    
	/** 
     * 在根目录下插入一个元素 
     * @param srcXml  原xml 
     * @param nodeXml 元素xml 
     * @return 原xml插入节点后的完整xml文档 
     */ 
    public static String addElement(String srcXml, String nodeXml) { 
        String resultXml = null; 
        try { 
            Document docSrc = DocumentHelper.parseText(srcXml); 
            Document docNode = DocumentHelper.parseText(nodeXml); 
            Element parentElement = (Element) docSrc.getRootElement(); 
            parentElement.add(docNode.getRootElement()); 
            resultXml = docSrc.asXML(); 
        } catch (DocumentException e) { 
            //log.error("在文档" + xpath + "位置添加新节点发生异常，请检查！"); 
            System.out.println(e.getMessage()); 
            e.printStackTrace(); 
        } 
        return resultXml; 
    }
    
	/** 
     * 在xml的指定位置插入一个元素 
     * @param srcXml  原xml 
     * @param nodeXml 元素xml 
     * @param xpath   要插入元素父节点的位置 
     * @return 原xml插入节点后的完整xml文档 
     */ 
    public static String addElement(String srcXml, String nodeXml, String xpath) { 
        String resultXml = null; 
        try { 
            Document docSrc = DocumentHelper.parseText(srcXml); 
            Document docNode = DocumentHelper.parseText(nodeXml); 
            Element parentElement = (Element) docSrc.getRootElement().selectSingleNode(xpath); 
            parentElement.add(docNode.getRootElement()); 
            resultXml = docSrc.asXML(); 
        } catch (DocumentException e) { 
            //log.error("在文档" + xpath + "位置添加新节点发生异常，请检查！"); 
            System.out.println(e.getMessage()); 
            e.printStackTrace(); 
        } 
        return resultXml; 
    } 

    /** 
     * 删除xml文档中指定ID的元素 
     * 
     * @param srcXml    原xml 
     * @param xmlNodeId 元素ID属性值 
     * @return 删除元素后的xml文档 
     */ 
    public static String removeElementById(String srcXml, String xmlNodeId) { 
        String resultXml = null; 
        try { 
            Document docSrc = DocumentHelper.parseText(srcXml); 
            Element removeElement = docSrc.getRootElement().elementByID(xmlNodeId); 
            removeElement.detach();  //直接删除自己 
//            removeElement.getParent().remove(removeElement);  //从父节点删除子节点 
            resultXml = docSrc.asXML(); 
        } catch (DocumentException e) { 
            //log.error("删除文档中ID为" + xmlNodeId + "的节点发生异常，请检查！"); 
            System.out.println(e.getMessage()); 
            e.printStackTrace(); 
        } 
        return resultXml; 
    } 

    /** 
     * 删除xml文档中以xpath为直接父节点的ID属性为空的子节点，ID属性为空包括值为空、空串、或者ID属性不存在。 
     * 
     * @param srcXml 原xml文档 
     * @param xpath  要删除空节点的所在父节点的xpath 
     * @return 删除空节点后的xml文档 
     */ 
    public static String removeNullIdElement(String srcXml, String xpath) { 
        String resultXml = null; 
        try { 
            Document srcDoc = DocumentHelper.parseText(srcXml); 
            removeNullIdElement(srcDoc, xpath); 
            resultXml = srcDoc.asXML(); 
        } catch (DocumentException e) { 
            //log.error("在" + xpath + "下删除空节点发生异常，请检查xpath是否正确！"); 
            System.out.println(e.getMessage()); 
            e.printStackTrace(); 
        } 
        return resultXml; 
    } 

    /** 
     * 删除xml文档中以xpath为直接父节点的ID属性为空的子节点，ID属性为空包括值为空、空串、或者ID属性不存在。 
     * 
     * @param srcDoc 原xml的Document对象 
     * @param xpath  要删除空节点的所在父节点的xpath 
     * @return 删除空节点后的xml文档 
     */ 
    public static Document removeNullIdElement(Document srcDoc, String xpath) { 
        Node parentNode = srcDoc.getRootElement().selectSingleNode(xpath); 
        if (!(parentNode instanceof Element)) { 
            //log.error("所传入的xpath不是Elementpath，删除空节点失败！"); 
        } else { 
            int i = 0; 
            for (Iterator<Element> it = ((Element) parentNode).elementIterator(); it.hasNext();) { 
                Element element = it.next(); 
                if (element.attribute("ID") == null) { 
                    element.detach(); 
                    i++; 
                } else { 
                    if (StringUtil.isEmpty(element.attribute("ID").getValue())) {             
                            element.detach(); 
                            i++; 
                     }
                } 
            } 
            //log.info("在" + xpath + "下成功删除" + i + "了个空节点!"); 
        } 
        return srcDoc; 
    } 

    /** 
     * 删除xml文档中指定xpath路径下所有直接子节点为空的节点
     * @param srcXml    原xml文档 
     * @param xpathList xpaht列表 
     * @return 删除空节点后的xml文档 
     */ 
    public static String removeAllNullIdElement(String srcXml, ArrayList<String> xpathList) { 
        String resultXml = null; 
        try { 
            Document srcDoc = DocumentHelper.parseText(srcXml); 
            for (Iterator<String> it = xpathList.iterator(); it.hasNext();) { 
                String xpath = it.next(); 
                removeNullIdElement(srcDoc, xpath); 
            } 
            resultXml = srcDoc.asXML(); 
        } catch (DocumentException e) { 
            System.out.println(e.getMessage()); 
            e.printStackTrace(); 
        } 
        return resultXml; 
    } 

    /** 
     * 更新xml文档中指定ID的元素,ID保持不变 
     * 
     * @param srcXml     原xml 
     * @param newNodeXml 新xml节点 
     * @param xmlNodeId  更新元素ID属性值 
     * @return 更新元素后的xml文档 
     */ 
    public static String updateElementById(String srcXml, String newNodeXml, String xmlNodeId) { 
        String resultXml = null; 
        try { 
            Document docSrc = DocumentHelper.parseText(srcXml); 
            Document newDocNode = DocumentHelper.parseText(newNodeXml); 
            //获取要更新的目标节点 
            Element updatedNode = docSrc.elementByID(xmlNodeId); 
            //获取更新目标节点的父节点 
            Element parentUpNode = updatedNode.getParent(); 
            //删除掉要更新的节点 
            parentUpNode.remove(updatedNode); 

            //获取新节点的根节点（作为写入节点） 
            Element newRoot = newDocNode.getRootElement(); 
            //处理新节点的ID属性值和BS子元素的值 
            if (newRoot.attribute("ID") == null) { 
                newRoot.addAttribute("ID", xmlNodeId); 
            } else { 
                newRoot.attribute("ID").setValue(xmlNodeId); 
            } 
            //在原文档中更新位置写入新节点 
            parentUpNode.add(newRoot); 
            resultXml = docSrc.asXML(); 
        } catch (DocumentException e) { 
            //log.error("更新xml文档中ID为" + xmlNodeId + "节点发生异常，请检查！"); 
            System.out.println(e.getMessage()); 
            e.printStackTrace(); 
        } 
        return resultXml; 
    } 

    /** 
     * 更新xml文档中指定ID的元素,并检查ID和BS，加以设置 
     * 
     * @param srcXml     原xml 
     * @param newNodeXml 新xml节点 
     * @param xmlNodeId  更新元素ID属性值 
     * @return 更新元素后的xml文档 
     */ 
    public static String updateElementByIdAddIdBs(String srcXml, String newNodeXml, String xmlNodeId) { 
        String resultXml = null; 
        try { 
            Document docSrc = DocumentHelper.parseText(srcXml); 
            Document newDocNode = DocumentHelper.parseText(newNodeXml); 
            //获取要更新的目标节点 
            Element updatedNode = docSrc.elementByID(xmlNodeId); 
            //获取更新目标节点的父节点 
            Element parentUpNode = updatedNode.getParent(); 
            //删除掉要更新的节点 
            parentUpNode.remove(updatedNode); 

            //获取新节点的根节点（作为写入节点） 
            Element newRoot = newDocNode.getRootElement(); 
            //处理新节点的ID属性值和BS子元素的值 
            if (newRoot.attribute("ID") == null) { 
                newRoot.addAttribute("ID", xmlNodeId); 
            } else { 
                newRoot.attribute("ID").setValue(xmlNodeId); 
            } 
            if (newRoot.element("BS") == null) { 
                newRoot.addElement("BS", xmlNodeId); 
            } else { 
                newRoot.element("BS").setText(xmlNodeId); 
            } 
            //在原文档中更新位置写入新节点 
            parentUpNode.add(newRoot); 
            resultXml = docSrc.asXML(); 
        } catch (DocumentException e) { 
            //log.error("更新xml文档中ID为" + xmlNodeId + "节点发生异常，请检查！"); 
            System.out.println(e.getMessage()); 
            e.printStackTrace(); 
        } 
        return resultXml; 
    } 

    /** 
     * 为xml元素设置ID属性 
     * 
     * @param xmlElement 原xml元素 
     * @return 设置id后的xml串 
     */ 
    public static String addIdAttribute(String xmlElement) { 
        String resultXml = null; 
        try { 
            Document srcDoc = DocumentHelper.parseText(xmlElement); 
            Element root = srcDoc.getRootElement(); 
//            Long nextValue = SequenceUtils.getSequeceNextValue(); 
            Long nextValue = new Random().nextLong(); 
            root.addAttribute("ID", nextValue.toString()); 
            resultXml = root.asXML(); 
        } catch (DocumentException e) { 
            //log.error("给xml元素设置ID属性发生异常，请检查！"); 
            System.out.println(e.getMessage()); 
            e.printStackTrace(); 
        } 
        return resultXml; 
    } 

    /** 
     * 为xml元素设置ID属性,并将此属性写入一个指定子节点文本值域 
     * 
     * @param xmlElement 原xml元素 
     * @param nodeName   （直接）子节点的名称，或相对当前节点的xpath路径 
     * @return 设置id和子节点后的xml串 
     */ 
    public static String addIdAndWriteNode(String xmlElement, String nodeName) { 
        String resultXml = null; 
        try { 
            Document srcDoc = DocumentHelper.parseText(xmlElement); 
            Element root = srcDoc.getRootElement(); 
//            Long nextValue = SequenceUtils.getSequeceNextValue(); 
            Long nextValue = new Random().nextLong(); 
            root.addAttribute("ID", nextValue.toString()); 
            Node bsElement = root.selectSingleNode(nodeName); 
            if (bsElement instanceof Element && bsElement != null) { 
                bsElement.setText(nextValue.toString()); 
            } else { 
                root.addElement(nodeName).setText(nextValue.toString()); 
            } 
            resultXml = root.asXML(); 
        } catch (DocumentException e) { 
            //log.error("给xml元素设置ID属性和直接" + nodeName + "子元素值时发生异常，请检查！"); 
            System.out.println(e.getMessage()); 
            e.printStackTrace(); 
        } 
        return resultXml; 
    } 

    public static String replaceNull(String str) { 
    	return str == null ? "" : str;
    }
    
}
