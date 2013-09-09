package com.util.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.util.string.StringUtil;

public final class FileUtil {

	private static final String CHARSET_UTF8 = "UTF-8";
	
	public static String getFileSuffix(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}
	
	/**
	 * 删除单个文件
	 * @param path
	 * @return
	 */
	public static boolean deleteFile(String path){
		boolean flag = false;
		File file = new File(path);
		if(file.exists()) {
			flag = file.delete();
		} 
		return flag;
	}
	
	public static void isExist(String path) {
		File file = new File(path);
		if (file.isDirectory()) {
			System.out.println("the   dir   is   exits");
		} else {
			System.out.println(file.mkdirs());
			System.out.println("have  made a dir!");
		}
	}
	
	/**
	 * 遍历文件夹中文件
	 * @param filepath
	 * @return 返回file［］数组
	 */
	public static File[] getFileList(String filepath) {
		File d = null;
		File list[] = null;
		
		// 建立当前目录中文件的File对象
		try {
			d = new File(filepath);
			if (d.exists()) {
				list = d.listFiles();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 取得代表目录中所有文件的File对象数组

		return list;
	}

	/**
	 * 读取文本文件内容
	 * 
	 * @param filePathAndName
	 *            带有完整绝对路径的文件名
	 * @param encoding
	 *            文本文件打开的编码方式
	 * @return 返回文本文件的内容
	 */
	public static String readText(String path) throws IOException {
		return readText(path, CHARSET_UTF8);
	}

	/**
	 * 读取文本文件内容
	 * 
	 * @param filePathAndName
	 *            带有完整绝对路径的文件名
	 * @param encoding
	 *            文本文件打开的编码方式
	 * @return 返回文本文件的内容
	 */
	public static String readText(String path, String encoding)  {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		InputStreamReader isr = null;
		FileInputStream fs;
		try {
			fs = new FileInputStream(path);
			
			if (StringUtil.isEmpty(encoding)) {
				isr = new InputStreamReader(fs);
			} else {
				isr = new InputStreamReader(fs, encoding);
			}
			br = new BufferedReader(isr);
		
			String data = "";
			while ((data = br.readLine()) != null) {
			    sb.append(data+"\r\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				isr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	 
	/** 
     * 写文件到本地 
     * @param in 
     * @param fileName 
     * @throws IOException 
     */  
    public static void write(InputStream in,String path) {  
        OutputStream os = null;
		try {
			os = new FileOutputStream(path);
			byte[] buffer = new byte[1024 * 1024];  
	          int bytesum = 0;  
	          int byteread = 0;  
	          while ((byteread = in.read(buffer)) != -1) {  
	              bytesum += byteread;  
	              os.write(buffer, 0, byteread);  
	              os.flush();  
	          }  
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			 try {
				if (os!= null)os.close();
				if (in!= null)in.close();  
			} catch (IOException e) {
				e.printStackTrace();
			}  
	        
		}  
    }  
	
    
  
}
