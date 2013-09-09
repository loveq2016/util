package com.util.array;

import java.util.List;

public class ListUtil {

	/**
	 * Array is Null.
	 * @param <T>
	 * @param array
	 * @return
	 */
	public static <T> boolean isEmpty(List<T> list) {
		return list == null || list.isEmpty();
	}

	/**
	 * Array is not Null.
	 * @param <T>
	 * @param array
	 * @return
	 */
	public static <T> boolean isNotEmpty(List<T> list) {
		return !isEmpty(list);
	}
	
	public static <T> String listToString(List<T> list) {
		if (list != null) {
			StringBuilder sb = new StringBuilder();
			for (T t : list) {
				sb.append(t+",");
			}
			sb.deleteCharAt(sb.length()-1);
			return sb.toString();
		}
		return null;
	}
	
}
