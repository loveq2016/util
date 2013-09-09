package com.util.sort;

public class BubbleSort {

	public static void main(String[] args) {
		int[] values = { 3, 1, 6, 2, 9, 0, 7, 4, 5 };

		sort(values);
		for (int i = 0; i < values.length; ++i) {
			System.out.println("Index: " + i + "Value: " + values[i]);
		}
	}

	public static void sort(int[] values) {
		int temp;
		for (int i = 0; i < values.length; ++i) {
			for (int j = 0; j < values.length - i - 1; ++j) {
				if (values[j] > values[j + 1]) {
					temp = values[j];
					values[j] = values[j + 1];
					values[j + 1] = temp;
				}
			}
		}
	}

}
