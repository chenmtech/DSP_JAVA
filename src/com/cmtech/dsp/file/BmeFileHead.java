/**
 * Project Name:DSP_JAVA
 * File Name:BmeFileCore.java
 * Package Name:com.cmtech.dsp.file
 * Date:2018年2月11日下午1:41:50
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.file;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

import com.cmtech.dsp.exception.FileException;

/**
 * ClassName: BmeFileCore
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月11日 下午1:41:50 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public abstract class BmeFileHead {
	public static final int UINT8 = 0;
	public static final int DOUBLE = 5;
	
	public static final int MSB = 0;
	public static final int LSB = 1;
	
	private String info = "Unknown";
	private byte dataType = DOUBLE;
	private int fs = -1;
	
	public BmeFileHead() {
	}
	
	public BmeFileHead(String info, byte dataType, int fs) {
		this.info = info;
		this.dataType = dataType;
		this.fs = fs;
	}
	
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public byte getDataType() {
		return dataType;
	}
	public void setDataType(byte dataType) {
		this.dataType = dataType;
	}
	public int getFs() {
		return fs;
	}
	public void setFs(int fs) {
		this.fs = fs;
	}
	
	public abstract byte[] getVersion();
	
	public abstract int getByteOrder();
	
	public abstract void readFromStream(InputStream in) throws FileException;
	
	public abstract void writeToStream(OutputStream out) throws FileException;

}
