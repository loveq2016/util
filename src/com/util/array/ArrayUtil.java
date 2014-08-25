package com.util.array;

public class ArrayUtil {
	
	/**
	 * Array is Null.
	 * @param <T>
	 * @param array
	 * @return
	 */
	public static <T> boolean isEmpty(T[] array) {
		return array == null || array.length == 0;
	}

	/**
	 * Array is not Null.
	 * @param <T>
	 * @param array
	 * @return
	 */
	public static <T> boolean isNotEmpty(T[] array) {
		return !isEmpty(array);
	}
	
	
	public static <T> String arrayToString(T[] array) {
		if (array != null) {
			StringBuilder sb = new StringBuilder();
			for (T t : array) {
				sb.append(t+",");
			}
			sb.deleteCharAt(sb.length()-1);
			return sb.toString();
		}
		return null;
	}
	
	
	
}
