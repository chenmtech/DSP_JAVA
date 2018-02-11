/**
 * Project Name:DSP_JAVA
 * File Name:BmeFile.java
 * Package Name:com.cmtech.dsp.bmefile
 * Date:2018年2月11日上午6:23:50
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import com.cmtech.dsp.exception.FileException;
import com.cmtech.dsp.util.FormatTransfer;

/**
 * ClassName: BmeFile
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月11日 上午6:23:50 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class BmeFile {
	private static Path rootPath = Paths.get(System.getProperty("user.dir"));
	
	public static final byte[] BME = {'B', 'M', 'E'};
	private static final byte[] DEFAULT_VERSION = {0x00, 0x01};
	
	private static final int MODE_READ = 0;
	private static final int MODE_WRITE = 0;
	
	private byte[] ver = new byte[2];
	private Path file;
	
	private BufferedInputStream in;
	private BufferedOutputStream out;
	
	private BmeFileHead fileHead;
	
	public BmeFile(String fileName) throws FileException{
		Path tmpFile = rootPath.resolve(fileName);
		Path parent = tmpFile.getParent();
		if(!Files.exists(parent)) throw new FileException(parent.toString(), "文件所在路径不存在");
		file = tmpFile;
	}

	public static void setFileDirectory(String pathName) throws FileException{
		Path p = null;
		
		if(pathName == null || "".equals(pathName)) 
			throw new FileException("", "路径为空");
		else {
			p = Paths.get(pathName);
			if(p.getRoot() == null) 
				throw new FileException(pathName, "路径必须包含根目录");
			else if(!Files.exists(p)) 
				throw new FileException(pathName, "路径不存在");
			else if(!Files.isDirectory(p))
				throw new FileException(pathName, "不是路径名");
		}
		
		rootPath = p;
	}
	
	public boolean openAsBmeFile() throws FileException{
		if(file == null) 
			throw new FileException("", "文件未正常设置");
		
		try	{
			if(!Files.exists(file))	Files.createFile(file);
			in = new BufferedInputStream(new FileInputStream(file.toString()));
			out = new BufferedOutputStream(new FileOutputStream(file.toString()));
		} catch (IOException e) {
			throw new FileException(file.toString(), "文件不存在");
		} finally {
			try {
				if(in != null) {
					in.close();
					in = null;
				}
				if(out != null) {
					out.close();
					out = null;
				}
			} catch(IOException ioe) {
				throw new FileException(file.toString(), "文件操作错误");
			}
		}
		
		return false;
	}
	
	public BmeFile createNewBmeFile(byte[] ver) throws FileException{
		if(file == null) 
			throw new FileException("", "文件路径设置错误");
		
		try {
			Files.deleteIfExists(file);
			Files.createFile(file);
			in = new BufferedInputStream(new FileInputStream(file.toString()));
			out = new BufferedOutputStream(new FileOutputStream(file.toString()));
			out.write(BME);
			out.write(ver);
			if(Arrays.equals(ver, BmeFileHead10.VER)){
				fileHead = new BmeFileHead10();
				fileHead.setFs(100);
				fileHead.writeToStream(out);
			}
			
		} catch(IOException ioe) {
			throw new FileException(file.toString(), "创建文件错误");
		}
		return this;
	}
	
	public BmeFile writeData(double[] data) throws FileException{
		if(in == null || out == null || fileHead == null) {
			throw new FileException("", "请先打开或创建文件");
		}
		
		try {
			DataOutputStream dOut = new DataOutputStream(out);
			int order = fileHead.getByteOrder();
			if(order == BmeFileHead.MSB) {
				for(int i = 0; i < data.length; i++) {
					dOut.writeDouble(data[i]);
				}				
			} else {
				for(int i = 0; i < data.length; i++) {
					dOut.write(FormatTransfer.toLH(data[i]));
				}
			}
		} catch(IOException ioe) {
			throw new FileException(file.toString(), "写数据错误");
		}
		return this;
	}
	
	public void close() throws FileException{
		try {
			if(in != null) {
				in.close();
				in = null;
			}
			if(out != null) {
				out.flush();
				out.close();
				out = null;
			}
		} catch(IOException ioe) {
			throw new FileException(file.toString(), "文件操作错误");
		}
	}
	
	
	public String getFileName() {
		if(file == null) return "";
		return file.toString();
	}

}
