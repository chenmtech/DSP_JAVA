/**
 * Project Name:DSP_JAVA
 * File Name:BmeFileHead10.java
 * Package Name:com.cmtech.dsp.file
 * Date:2018年2月11日下午1:57:37
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
	
	public BmeFileHead10(String info, byte dataType, int fs) {
		super(info, dataType, fs);
	}

	@Override
	public byte[] getVersion() {
		return VER;
	}
	
	@Override
	public int getByteOrder() {
		return LSB;
	}
	
	@Override
	public void readFromStream(InputStream in) throws FileException{
		try {
			DataInputStream dIn = new DataInputStream(in);
			// ver1.0内部数据为LSB，要反过来变为MSB
			int infoLen = FormatTransfer.reverseInt(dIn.readInt());
			byte[] str = new byte[infoLen];
			dIn.read(str);
			setInfo(Arrays.toString(str));
			setDataType(dIn.readByte());
			setFs(FormatTransfer.reverseInt(dIn.readInt()));
		} catch(IOException ioe) {
			throw new FileException("文件头", "读入错误");
		}
	}
	
	@Override
	public void writeToStream(OutputStream out) throws FileException {
		try {
			DataOutputStream dOut = new DataOutputStream(out);
			int infoLen = getInfo().length();
			// ver1.0要写为LSB
			dOut.writeInt(FormatTransfer.reverseInt(infoLen));	
			dOut.write(getInfo().getBytes());
			dOut.writeByte(getDataType());
			// ver1.0要写为LSB
			dOut.writeInt(FormatTransfer.reverseInt(getFs()));
		} catch(IOException ioe) {
			throw new FileException("文件头", "写出错误");
		}
	}

}
