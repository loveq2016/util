package com.util.test;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		XStream xStream = new XStream(); 
		
		List<String[]> list = new ArrayList<String[]>();
		String[] array1 = new String[]{"111","222"};
		String[] array2 = new String[]{"333","444"};
		String[] array3 = new String[]{"555","666"};
		list.add(array1);
		list.add(array2);
		list.add(array3);
		//String[] array = new String[]{"111","222"};
		System.out.println(xStream.toXML(list));
				
		/*String a = "11";
		Integer b = 1;
		test(a,b);
		System.out.println(b);
		System.out.println(a);*/
	}

	public static void test(String a,Integer b) {
		a = "qqq";
		b = 2;
		System.out.println(a);
	}
}
