package com.util.sort;

public class QuickSoft {

	private void swap(int a[], int i, int j) {
		int tmp = a[i];
		a[i] = a[j];
		a[j] = tmp;
	}

	private int partition(int a[], int p, int r) {
		int point = a[r];
		// 将小于等于point的元素移到左边区域
		// 将大于point的元素移到右边区域
		int index = p;
		for (int i = index; i < r; ++i) {
			if (a[i] - point <= 0) {
				swap(a, index++, i);
			}
		}
		swap(a, index, r);
		return index;
	}

	public void qsort(int a[], int p, int r) {
		if (p < r) {
			// 确定拆分点，并对数组元素进行移动
			// 这是快速排序算法的关键步骤
			int q = partition(a, p, r);
			// 对左半段排序
			qsort(a, p, q - 1);
			// 对右半段排序
			qsort(a, q + 1, r);
		}
	}

	public static void main(String[] args) {
		// 声明一个类
		QuickSoft ms = new QuickSoft();
		int len = 10;
		int a[] = new int[len];
		// 初始化a数组

		System.out.println("原始数组如下：");
		for (int i = 0; i < a.length; i++) {
			// 产生a.length个随机数
			a[i] = (int) (Math.random() * 100000);
			System.out.println(a[i]);
		}
		System.out.println("---------------------");
		System.out.println("第一次分组后");
		ms.partition(a, 0, len - 1);
		for (int i = 0; i < a.length; i++) {
			System.out.println(a[i]);
		}
		System.out.println("---------------------");
		// 快速排序
		ms.qsort(a, 0, len - 1);

		System.out.println("排序后的数组如下：");
		for (int i = 0; i < a.length; i++) {
			System.out.println(a[i]);
		}

	}
}