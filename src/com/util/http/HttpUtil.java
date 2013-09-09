package com.util.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;

import com.util.array.ArrayUtil;
import com.util.config.Conf;
import com.util.date.DateUtil;

public final class HttpUtil extends HttpURLUtil {

	private static final Log log = LogFactory.getLog(HttpUtil.class);

	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public void dd() {

	}

	public static InputStream getInputStream(String url) {
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) new URL(url).openConnection();
			int state = connection.getResponseCode();
			if (state == HttpURLConnection.HTTP_OK) {
				return connection.getInputStream();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getInputStream(HttpServletRequest request,
			String charset) {
		BufferedReader in = null;
		StringBuilder sb = new StringBuilder();
		try {
			in = new BufferedReader(new InputStreamReader(
					request.getInputStream(), charset));

			String line;
			while ((line = in.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 获得请求路径
	 * 
	 * @param request
	 * @return
	 */
	public static String getRequestPath(HttpServletRequest request) {
		String requestPath = request.getRequestURI();
		return requestPath.substring(request.getContextPath().length() + 1);// 去掉项目路径
	}

	public static String getInputStream(HttpServletRequest request) {
		return getInputStream(request, Conf.CODING_UTF8);
	}

	public static Map<String, String> getParameterMap(HttpServletRequest request) {

		Enumeration<String> rnames = request.getParameterNames();
		Map<String, String> map = null;
		if (rnames != null) {
			map = new HashMap<String, String>();
			for (Enumeration<String> e = rnames; e.hasMoreElements();) {
				String thisName = e.nextElement().toString();
				String thisValue = ArrayUtil.arrayToString(request
						.getParameterValues(thisName));
				if ("setId".equalsIgnoreCase(thisName)) {
					Object object = request.getSession().getAttribute("user");
				} else if ("setName".equalsIgnoreCase(thisName)) {

				} else if ("setDate".equalsIgnoreCase(thisName)) {
					thisName = request.getParameter(thisName);
					thisValue = DateUtil.getDateSecondFormat();
				} else {
					thisValue = ArrayUtil.arrayToString(request
							.getParameterValues(thisName));
				}
				map.put(thisName, thisValue);
				log.debug("name======" + thisName + "-------value====="
						+ thisValue);
			}
		}
		return map;
	}

	public static Map<String, String> getParameterMap() {
		return getParameterMap(getRequest());
	}

	/**
	 * 得到HttpServletRequest对象
	 * 
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
	}

	/** 获取HttpServletResponse */
	public static HttpServletResponse getResponse() {
		return ((ServletWebRequest)(ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getResponse();
	}

}
