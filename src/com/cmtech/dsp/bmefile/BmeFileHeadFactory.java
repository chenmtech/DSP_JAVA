/**
 * Project Name:DSP_JAVA
 * File Name:BmeFileHeadFactory.java
 * Package Name:com.cmtech.dsp.bmefile
 * Date:2018年2月17日上午5:33:27
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.bmefile;

import java.util.Arrays;

import com.cmtech.dsp.exception.FileException;

/**
 * ClassName: BmeFileHeadFactory
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月17日 上午5:33:27 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class BmeFileHeadFactory {
	public static BmeFileHead create(byte[] ver) throws FileException{
		if(Arrays.equals(ver, BmeFileHead10.VER)) {
			return new BmeFileHead10();
		} else if(Arrays.equals(ver, BmeFileHead20.VER)) {
			return new BmeFileHead20();
		} else {
			throw new FileException(Arrays.toString(ver), "不支持此文件版本");
		}
	}
	
	public static BmeFileHead createDefault() {
		return new BmeFileHead10();
	}

}
