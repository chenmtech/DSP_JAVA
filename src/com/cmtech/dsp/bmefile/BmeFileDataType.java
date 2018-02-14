/**
 * Project Name:DSP_JAVA
 * File Name:BmeFileDataType.java
 * Package Name:com.cmtech.dsp.file
 * Date:2018年2月12日上午11:05:51
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.bmefile;

/**
 * ClassName: BmeFileDataType
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月12日 上午11:05:51 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public enum BmeFileDataType {
	INT32(0),    //int
    UINT8(1),    //unsigned char    
	DOUBLE(5),   //double
	UNKNOWN(-1); //未知

    private int code;

    BmeFileDataType(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
    
    
}
