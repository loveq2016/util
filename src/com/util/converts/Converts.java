package com.util.converts;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public final class Converts {

	private static String hexString = "0123456789ABCDEF";

	/**
	 * 把16进制字符串转换成字节数组
	 * @param hex
	 * @return
	 */
	public static byte[] stringToByte(String str) {
		int len = (str.length() / 2);
		byte[] result = new byte[len];
		char[] achar = str.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private static byte toByte(char c) {
		byte b = (byte) hexString.indexOf(c);
		return b;
	}

	/** 
	 * 把字节数组转换成16进制字符串
	 * @param bArray
	 * @return
	 */
	public static String byteToString(byte[] bytes) {
		StringBuffer sb = new StringBuffer(bytes.length);
		String str;
		for (int i = 0; i < bytes.length; i++) {
			str = Integer.toHexString(0xFF & bytes[i]);
			if (str.length() < 2)
				sb.append(0);
			sb.append(str.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 把字节数组转换为对象
	 * @param bytes
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object bytesToObject(byte[] bytes) throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream in = new ByteArrayInputStream(bytes);
		ObjectInputStream oi = new ObjectInputStream(in);
		Object o = oi.readObject();
		oi.close();
		return o;
	}

	/** 
	 * 把可序列化对象转换成字节数组
	 * @param s
	 * @return
	 * @throws IOException
	 */
	public static byte[] objectToBytes(Serializable serializable) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream ot = new ObjectOutputStream(out);
		ot.writeObject(serializable);
		ot.flush();
		ot.close();
		return out.toByteArray();
	}

	public static String objectToHexString(Serializable serializable)
			throws IOException {
		return byteToString(objectToBytes(serializable));
	}

	public static Object stringToObject(String str)
			throws IOException, ClassNotFoundException {
		return bytesToObject(stringToByte(str));
	}

	/**
	 * @函数功能: BCD码转为10进制串(阿拉伯数据)
	 * @输入参数: BCD码
	 * @输出结果: 10进制串
	 */
	public static String bcdToString(byte[] bytes) {
		StringBuffer temp = new StringBuffer(bytes.length * 2);

		for (int i = 0; i < bytes.length; i++) {
			temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
			temp.append((byte) (bytes[i] & 0x0f));
		}
		return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp
				.toString().substring(1) : temp.toString();
	}

	/** 
	 * @函数功能: 10进制串转为BCD码
	 * @输入参数: 10进制串
	 * @输出结果: BCD码
	 */
	public static byte[] stringToBcd(String asc) {
		int len = asc.length();
		int mod = len % 2;

		if (mod != 0) {
			asc = "0" + asc;
			len = asc.length();
		}

		byte abt[] = new byte[len];
		if (len >= 2) {
			len = len / 2;
		}

		byte bbt[] = new byte[len];
		abt = asc.getBytes();
		int j, k;

		for (int p = 0; p < asc.length() / 2; p++) {
			if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
				j = abt[2 * p] - '0';
			} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
				j = abt[2 * p] - 'a' + 0x0a;
			} else {
				j = abt[2 * p] - 'A' + 0x0a;
			}

			if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
				k = abt[2 * p + 1] - '0';
			} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
				k = abt[2 * p + 1] - 'a' + 0x0a;
			} else {
				k = abt[2 * p + 1] - 'A' + 0x0a;
			}

			int a = (j << 4) + k;
			byte b = (byte) a;
			bbt[p] = b;
		}
		return bbt;
	}

	/** 
	 * @函数功能: BCD码转ASC码
	 * @输入参数: BCD串
	 * @输出结果: ASC码
	 */
	public static String BCDToASC(byte[] bytes) {
		StringBuffer temp = new StringBuffer(bytes.length * 2);

		for (int i = 0; i < bytes.length; i++) {
			int h = ((bytes[i] & 0xf0) >>> 4);
			int l = (bytes[i] & 0x0f);
			temp.append(BToA[h]).append(BToA[l]);
		}
		return temp.toString();
	}

	//转换十六进制编码为字符串
	public static String toStringHex(String str) {
		if ("0x".equals(str.substring(0, 2))) {
			str = str.substring(2);
		}
		byte[] baKeyword = new byte[str.length() / 2];
		for (int i = 0; i < baKeyword.length; i++) {
			try {
				baKeyword[i] = (byte) (0xff & Integer.parseInt(str.substring(
						i * 2, i * 2 + 2), 16));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			str = new String(baKeyword, "utf-8");//UTF-16le:Not
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	public final static char[] BToA = "0123456789ABCDEF".toCharArray();

	public static String decode(String bytes) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(
				bytes.length() / 2);
		//将每2位16进制整数组装成一个字节
		for (int i = 0; i < bytes.length(); i += 2)
			baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString
					.indexOf(bytes.charAt(i + 1))));
		return new String(baos.toByteArray());
	}

	public static int byteToInt(byte[] b, int offset) {
		return b[(offset + 3)] & 0xFF | (b[(offset + 2)] & 0xFF) << 8
				| (b[(offset + 1)] & 0xFF) << 16 | (b[offset] & 0xFF) << 24;
	}

	public static int byteToInt(byte[] b) {
		return b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16
				| (b[0] & 0xFF) << 24;
	}

	public static long byteToLong(byte[] b) {
		return b[7] & 0xFF | (b[6] & 0xFF) << 8 | (b[5] & 0xFF) << 16
				| (b[4] & 0xFF) << 24 | (b[3] & 0xFF) << 32
				| (b[2] & 0xFF) << 40 | (b[1] & 0xFF) << 48 | b[0] << 56;
	}

	public static long byteToLong(byte[] b, int offset) {
		return b[(offset + 7)] & 0xFF | (b[(offset + 6)] & 0xFF) << 8
				| (b[(offset + 5)] & 0xFF) << 16
				| (b[(offset + 4)] & 0xFF) << 24
				| (b[(offset + 3)] & 0xFF) << 32
				| (b[(offset + 2)] & 0xFF) << 40
				| (b[(offset + 1)] & 0xFF) << 48 | b[offset] << 56;
	}

	public static byte[] intToByte(int n) {
		byte[] b = new byte[4];
		b[0] = (byte) (n >> 24);
		b[1] = (byte) (n >> 16);
		b[2] = (byte) (n >> 8);
		b[3] = (byte) n;
		return b;
	}

	public static void intToByte(int n, byte[] buf, int offset) {
		buf[offset] = (byte) (n >> 24);
		buf[(offset + 1)] = (byte) (n >> 16);
		buf[(offset + 2)] = (byte) (n >> 8);
		buf[(offset + 3)] = (byte) n;
	}

	public static byte[] shortToByte(int n) {
		byte[] b = new byte[2];
		b[0] = (byte) (n >> 8);
		b[1] = (byte) n;
		return b;
	}

	public static void shortToByte(int n, byte[] buf, int offset) {
		buf[offset] = (byte) (n >> 8);
		buf[(offset + 1)] = (byte) n;
	}

	public static byte[] longToByte(long n) {
		byte[] b = new byte[8];

		b[0] = (byte) (int) (n >> 56);
		b[1] = (byte) (int) (n >> 48);
		b[2] = (byte) (int) (n >> 40);
		b[3] = (byte) (int) (n >> 32);
		b[4] = (byte) (int) (n >> 24);
		b[5] = (byte) (int) (n >> 16);
		b[6] = (byte) (int) (n >> 8);
		b[7] = (byte) (int) n;
		return b;
	}

	public static void longToByte(long n, byte[] buf, int offset) {
		buf[offset] = (byte) (int) (n >> 56);
		buf[(offset + 1)] = (byte) (int) (n >> 48);
		buf[(offset + 2)] = (byte) (int) (n >> 40);
		buf[(offset + 3)] = (byte) (int) (n >> 32);
		buf[(offset + 4)] = (byte) (int) (n >> 24);
		buf[(offset + 5)] = (byte) (int) (n >> 16);
		buf[(offset + 6)] = (byte) (int) (n >> 8);
		buf[(offset + 7)] = (byte) (int) n;
	}
}
