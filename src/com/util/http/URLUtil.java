package com.util.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/** 
 * URL工具 
 * @author gary 
 * 
 */
public class URLUtil {

	/** 
	 * 对url进行编码 
	 */
	public static String encodeURL(String url) {
		try {
			return URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/** 
	 * 对url进行解码 
	 * @param url 
	 * @return 
	 */
	public static String decodeURL(String url) {
		try {
			return URLDecoder.decode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/** 
	 * 判断URL地址是否存在 
	 * @param url 
	 * @return 
	 */
	public static boolean isURLExist(HttpURLConnection connection) {
			try {
				int state = connection.getResponseCode();
				if (state == HttpURLConnection.HTTP_OK) {
					return true;
				} else {
					return false;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			
	}

	/** 
	 * 将请求参数还原为key=value的形式,for struts2 
	 * @param params 
	 * @return 
	 */
	public static String getParamString(Map<?, ?> params) {
		StringBuffer queryString = new StringBuffer(256);
		Iterator<?> it = params.keySet().iterator();
		int count = 0;
		while (it.hasNext()) {
			String key = (String) it.next();
			String[] param = (String[]) params.get(key);
			for (int i = 0; i < param.length; i++) {
				if (count == 0) {
					count++;
				} else {
					queryString.append("&");
				}
				queryString.append(key);
				queryString.append("=");
				try {
					queryString.append(URLEncoder.encode((String) param[i],
							"UTF-8"));
				} catch (UnsupportedEncodingException e) {
				}
			}
		}
		return queryString.toString();
	}

	/** 
	 * 获得请求的路径及参数 
	 * @param request 
	 * @return 
	 */
	public static String getRequestURL(HttpServletRequest request) {
		StringBuffer originalURL = new StringBuffer(request.getServletPath());
		Map<?, ?> parameters = request.getParameterMap();
		if (parameters != null && parameters.size() > 0) {
			originalURL.append("?");
			originalURL.append(getParamString(parameters));
		}
		return originalURL.toString();
	}

	/** 
	 * 抓取网页内容,自动识别编码 
	 * @param urlString 
	 * @return 
	 */
	public static String readWebPageContent(String urlLink) {
		HttpURLConnection conn = null;
		try {
			StringBuilder sb = new StringBuilder();
			URL url = new URL(urlLink);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(1500);
			conn.setReadTimeout(5000);

			URLConnection c = url.openConnection();
			c.connect();
			
			String contentType = c.getContentType();
			String characterEncoding = null;
			int index = contentType.indexOf("charset=");
			if (index == -1) {
				characterEncoding = "UTF-8";
			} else {
				characterEncoding = contentType.substring(index + 8,
						contentType.length());
			}
			InputStreamReader isr = new InputStreamReader(
					conn.getInputStream(), characterEncoding);
			BufferedReader br = new BufferedReader(isr);
			String temp;
			while ((temp = br.readLine()) != null) {
				sb.append(temp).append("\n");
			}
			br.close();
			isr.close();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}

	public static String sendXML(String urlLink, String xml, String encode) {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(urlLink);
			connection = (HttpURLConnection) url.openConnection();
			
			connection.setConnectTimeout(1500);
			connection.setReadTimeout(5000);
			
			connection.setRequestProperty("Content-type", "text/xml");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");

			connection.getOutputStream().write(xml.getBytes(encode));
			connection.getOutputStream().flush();
			connection.getOutputStream().close();

			StringBuilder sb = new StringBuilder();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), encode));
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			in.close();
			return sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

	}
	
	public static String sendText(String urlLink, String text, String encode) {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(urlLink);
			connection = (HttpURLConnection) url.openConnection();
			
			connection.setConnectTimeout(1500);
			connection.setReadTimeout(5000);
			
			connection.setRequestProperty("Content-type","text/plain;charset=UTF-8");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");

			connection.getOutputStream().write(text.getBytes(encode));
			connection.getOutputStream().flush();
			connection.getOutputStream().close();

			StringBuilder sb = new StringBuilder();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), encode));
			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
			in.close();
			return sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}

	}
	
}
