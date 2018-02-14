/**
 * Project Name:DSP_JAVA
 * File Name:BmeFileCore.java
 * Package Name:com.cmtech.dsp.file
 * Date:2018年2月11日下午1:41:50
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.bmefile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.ByteOrder;

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
	private String info = "Unknown";
	private BmeFileDataType dataType = BmeFileDataType.DOUBLE;
	private int fs = -1;
	
	public BmeFileHead() {
	}
	
	public BmeFileHead(String info, BmeFileDataType dataType, int fs) {
		this.info = info;
		this.dataType = dataType;
		this.fs = fs;
	}
	
	public BmeFileHead(BmeFileHead fileHead) {
		info = fileHead.info;
		dataType = fileHead.dataType;
		fs = fileHead.fs;
	}
	
	public abstract byte[] getVersion();
	
	public String getInfo() {
		return info;
	}
	
	public BmeFileHead setInfo(String info) {
		this.info = info;
		return this;
	}
	
	public BmeFileDataType getDataType() {
		return dataType;
	}
	
	public BmeFileHead setDataType(BmeFileDataType dataType) {
		this.dataType = dataType;
		return this;
	}
	
	public int getFs() {
		return fs;
	}
	
	public BmeFileHead setFs(int fs) {
		this.fs = fs;
		return this;
	}
	
	public abstract ByteOrder getByteOrder();
	public abstract BmeFileHead setByteOrder(ByteOrder byteOrder);
	
	

	
	public abstract void readFromStream(DataInputStream in) throws FileException;
	
	public abstract void writeToStream(DataOutputStream out) throws FileException;

}
