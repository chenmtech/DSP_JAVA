/**
 * Project Name:DSP_JAVA
 * File Name:BmeFileHead10.java
 * Package Name:com.cmtech.dsp.file
 * Date:2018年2月11日下午1:57:37
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.bmefile;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Arrays;

import com.cmtech.dsp.exception.FileException;
import com.cmtech.dsp.util.FormatTransfer;

/**
 * ClassName: BmeFileHead10
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月11日 下午1:57:37 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class BmeFileHead10 extends BmeFileHead {
	public static final byte[] VER = new byte[] {0x00, 0x01};
	
	public BmeFileHead10() {
		super();
	}
	
	public BmeFileHead10(String info, BmeFileDataType dataType, int fs) {
		super(info, dataType, fs);
	}
	
	public BmeFileHead10(BmeFileHead fileHead) {
		super(fileHead);
	}
	
	@Override
	public byte[] getVersion() {
		return VER;
	}

	
	@Override
	public ByteOrder getByteOrder() {
		return ByteOrder.LITTLE_ENDIAN;
	}
	
	@Override
	public BmeFileHead setByteOrder(ByteOrder byteOrder) {
		return this;
	}
	
	@Override
	public void readFromStream(DataInputStream in) throws FileException{
		try {
			// ver1.0内部数据字节序为LSB，要反过来变为MSB
			int infoLen = FormatTransfer.reverseInt(in.readInt());
			byte[] str = new byte[infoLen];
			in.read(str);
			setInfo(new String(str));
			int dType = in.readByte();
			setDataType(BmeFileDataType.UNKNOWN);
			for(BmeFileDataType type : BmeFileDataType.values()) {
				if(dType == type.getCode()) {
					setDataType(type);
					break;
				}
			}
			setFs(FormatTransfer.reverseInt(in.readInt()));
		} catch(IOException ioe) {
			throw new FileException("文件头", "读入错误");
		}
	}
	
	@Override
	public void writeToStream(DataOutputStream out) throws FileException {
		try {
			int infoLen = getInfo().length();
			// ver1.0要写为LSB字节序
			out.write(FormatTransfer.toLH(infoLen));
			out.write(getInfo().getBytes());
			out.writeByte((byte)getDataType().getCode());
			// ver1.0要写为LSB
			out.write(FormatTransfer.toLH(getFs()));
		} catch(IOException ioe) {
			throw new FileException("文件头", "写出错误");
		}
	}

	@Override
	public String toString() {
		return "[文件头信息：" 
				+ getClass().getSimpleName() + ";"
				+ Arrays.toString(getVersion()) + ";"
				+ getByteOrder() + ";"
				+ getInfo() + ";"
				+ getDataType() + ";"
				+ getFs() + "]";
	}
}
