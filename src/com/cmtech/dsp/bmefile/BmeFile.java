/**
 * Project Name:DSP_JAVA
 * File Name:BmeFile.java
 * Package Name:com.cmtech.dsp.bmefile
 * Date:2018年2月11日上午6:23:50
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.bmefile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	private static Set<String> fileInOperation = new HashSet<>();
	private static Path rootPath = Paths.get(System.getProperty("user.dir"));
	private static final BmeFileHead DEFAULT_FILE_HEAD = BmeFileHeadFactory.createDefault();
	
	public static final byte[] BME = {'B', 'M', 'E'};
	
	private Path file;
	
	private DataInputStream in;
	private DataOutputStream out;
	
	private final BmeFileHead fileHead;
	
	private BmeFile(String fileName) throws FileException{
		checkFile(fileName);
		fileHead = open();
	}
	
	private BmeFile(String fileName, BmeFileHead head) throws FileException{
		checkFile(fileName);
		fileHead = create(head);
	}
	

	public static BmeFile openBmeFile(String fileName) throws FileException{
		return new BmeFile(fileName);
	}
	
	public static BmeFile createBmeFile(String fileName) throws FileException{
		return new BmeFile(fileName, DEFAULT_FILE_HEAD);
	}
	
	public static BmeFile createBmeFile(String fileName, BmeFileHead head) throws FileException{
		return new BmeFile(fileName, head);
	}
	
	
	private void checkFile(String fileName) throws FileException{
		Path tmpFile = rootPath.resolve(fileName);
		Path parent = tmpFile.getParent();
		if(!Files.exists(parent)) throw new FileException(parent.toString(), "文件所在路径不存在");

		if(fileInOperation.contains(tmpFile.toString())) 
			throw new FileException(tmpFile.toString(), "文件已经打开");
		else {
			file = tmpFile;
			fileInOperation.add(file.toString());
		}
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
				throw new FileException(pathName, "不是有效路径名");
		}
		
		rootPath = p;
	}
	
	private BmeFileHead open() throws FileException{
		BmeFileHead fileHead;
		
		if(file == null) 
			throw new FileException("", "文件未正常设置");
		if(!Files.exists(file))
			throw new FileException(file.toString(), "文件不存在");
		if(in != null || out != null)
			throw new FileException(file.toString(), "文件已经打开，需要关闭后重新打开");
		
		try	{
			in = new DataInputStream(
					new BufferedInputStream(
							new FileInputStream(file.toString())));
			byte[] bme = new byte[3];
			in.read(bme);
			if(!Arrays.equals(bme, BME)) throw new FileException(file.toString(), "文件格式不对");
			byte[] ver = new byte[2];
			in.read(ver);
			fileHead = BmeFileHeadFactory.create(ver);
			fileHead.readFromStream(in);
		} catch (IOException e) {
			throw new FileException(file.toString(), "文件打开错误");
		}
		return fileHead;
	}
	
	public double[] readData(double[] d) throws FileException{
		if(in == null || fileHead == null) {
			throw new FileException("", "请先打开文件");
		}
		
		if(fileHead.getDataType() != BmeFileDataType.DOUBLE) {
			throw new FileException(file.toString(), "读取数据类型错误");
		}
		
		List<Double> lst = new ArrayList<Double>();
		double[] data = new double[0];
		byte[] buf = new byte[8];
		ByteBuffer big = ByteBuffer.wrap(buf).order(ByteOrder.BIG_ENDIAN);
		ByteBuffer little = ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN);
		try {
			if(fileHead.getByteOrder() == ByteOrder.BIG_ENDIAN) {
				while(in.available() > 0) 
					lst.add(in.readDouble());				
			} else {
				while(in.available() > 0) {
					little.putDouble(0, in.readDouble());
					lst.add(big.getDouble(0));
				}
			}
			
			data = new double[lst.size()];
			for(int i = 0; i < lst.size(); i++) {
				data[i] = lst.get(i);
			}
		} catch(IOException ioe) {
			throw new FileException(file.toString(), "读数据错误");
		}
		return data;
	}
	
	public int[] readData(int[] d) throws FileException{
		if(in == null || fileHead == null) {
			throw new FileException("", "请先打开文件");
		}
		
		if(fileHead.getDataType() != BmeFileDataType.INT32) {
			throw new FileException(file.toString(), "读取数据类型错误");
		}
		
		List<Integer> lst = new ArrayList<Integer>();
		int[] data = new int[0];
		byte[] buf = new byte[4];
		ByteBuffer big = ByteBuffer.wrap(buf).order(ByteOrder.BIG_ENDIAN);
		ByteBuffer little = ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN);
		try {
			if(fileHead.getByteOrder() == ByteOrder.BIG_ENDIAN) {
				while(in.available() > 0) 
					lst.add(in.readInt());				
			} else {
				while(in.available() > 0) {
					little.putInt(0, in.readInt());
					lst.add(big.getInt(0));
				}
			}
			
			data = new int[lst.size()];
			for(int i = 0; i < lst.size(); i++) {
				data[i] = lst.get(i);
			}
		} catch(IOException ioe) {
			throw new FileException(file.toString(), "读数据错误");
		}
		return data;
	}
	
	public byte[] readData(byte[] d) throws FileException{
		if(in == null || fileHead == null) {
			throw new FileException("", "请先打开文件");
		}
		
		if(fileHead.getDataType() != BmeFileDataType.UINT8) {
			throw new FileException(file.toString(), "读取数据类型错误");
		}
		
		List<Byte> lst = new ArrayList<>();
		byte[] data = new byte[0];
		try {
			while(in.available() > 0) 
				lst.add(in.readByte());
			
			data = new byte[lst.size()];
			for(int i = 0; i < lst.size(); i++) {
				data[i] = lst.get(i);
			}
		} catch(IOException ioe) {
			throw new FileException(file.toString(), "读数据错误");
		}
		return data;
	}
	
	private BmeFileHead create(BmeFileHead head) throws FileException{
		if(file == null) 
			throw new FileException("", "文件路径设置错误");
		if(head == null)
			throw new FileException("file head", "文件头错误");
		if(in != null || out != null)
			throw new FileException(file.toString(), "文件已经打开，需要关闭后重新打开");
		
		try {
			Files.deleteIfExists(file);
			Files.createFile(file);
			out = new DataOutputStream(
					new BufferedOutputStream(
							new FileOutputStream(file.toString())));
			out.write(BME);
			out.write(head.getVersion());
			head.writeToStream(out);
		} catch(IOException ioe) {
			throw new FileException(file.toString(), "创建文件错误");
		}
		return head;
	}
	
	public BmeFile writeData(double[] data) throws FileException{
		if(out == null || fileHead == null) {
			throw new FileException("", "请先创建文件");
		}
		
		if(fileHead.getDataType() != BmeFileDataType.DOUBLE) {
			throw new FileException("", "写入数据类型错误");
		}
		
		try {
			if(fileHead.getByteOrder() == ByteOrder.BIG_ENDIAN) {
				for(int i = 0; i < data.length; i++) {
					out.writeDouble(data[i]);
				}				
			} else {
				for(int i = 0; i < data.length; i++) {
					out.write(FormatTransfer.toLH(data[i]));
				}
			}
		} catch(IOException ioe) {
			throw new FileException(file.toString(), "写数据错误");
		}
		return this;
	}
	
	public BmeFile writeData(double data) throws FileException{
		try {
			if(fileHead.getByteOrder() == ByteOrder.BIG_ENDIAN) {
				out.writeDouble(data);			
			} else {
				out.write(FormatTransfer.toLH(data));
			}
		} catch(IOException ioe) {
			throw new FileException(file.toString(), "写数据错误");
		}
		return this;
	}
	
	public BmeFile writeData(int[] data) throws FileException{
		if(out == null || fileHead == null) {
			throw new FileException("", "请先创建文件");
		}
		
		if(fileHead.getDataType() != BmeFileDataType.INT32) {
			throw new FileException("", "写入数据类型错误");
		}
		
		try {
			if(fileHead.getByteOrder() == ByteOrder.BIG_ENDIAN) {
				for(int i = 0; i < data.length; i++) {
					out.writeInt(data[i]);
				}				
			} else {
				for(int i = 0; i < data.length; i++) {
					out.write(FormatTransfer.toLH(data[i]));
				}
			}
		} catch(IOException ioe) {
			throw new FileException(file.toString(), "写数据错误");
		}
		return this;
	}
	
	public BmeFile writeData(int data) throws FileException{
		try {
			if(fileHead.getByteOrder() == ByteOrder.BIG_ENDIAN) {
				out.writeInt(data);			
			} else {
				out.write(FormatTransfer.toLH(data));
			}
		} catch(IOException ioe) {
			throw new FileException(file.toString(), "写数据错误");
		}
		return this;
	}
	
	public BmeFile writeData(byte[] data) throws FileException{
		if(out == null || fileHead == null) {
			throw new FileException("", "请先创建文件");
		}
		
		if(fileHead.getDataType() != BmeFileDataType.UINT8) {
			throw new FileException("", "写入数据类型错误");
		}
		
		try {
			for(int i = 0; i < data.length; i++) {
				out.writeByte(data[i]);
			}
		} catch(IOException ioe) {
			throw new FileException(file.toString(), "写数据错误");
		}
		return this;
	}
	
	public BmeFile writeData(byte data) throws FileException{
		try {
			out.writeByte(data);
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
				out.close();
				out = null;
			}
		} catch(IOException ioe) {
			throw new FileException(file.toString(), "关闭文件操作错误");
		} finally {
			fileInOperation.remove(file.toString());
		}
	}
	
	
	public String getFileName() {
		if(file == null) return "";
		return file.toString();
	}
	
	public String getInfo() {
		if(fileHead == null) return "";
		return fileHead.getInfo();
	}

	public BmeFileDataType getDataType() {
		if(fileHead == null) return null;
		return fileHead.getDataType();
	}

	public int getFs() {
		if(fileHead == null) return -1;
		return fileHead.getFs();
	}
	
	public byte[] getVersion() {
		if(fileHead == null) return null;
		return fileHead.getVersion();
	}
	
	public ByteOrder getByteOrder() {
		if(fileHead == null) return null;
		return fileHead.getByteOrder();
	}
	
	public BmeFileHead getBmeFileHead() {
		return fileHead;
	}

	@Override
	public String toString() {
		return "[文件名：" + getFileName() + ":"+ fileHead + "]";
	}
}
