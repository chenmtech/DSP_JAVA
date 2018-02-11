/**
 * Project Name:DSP_JAVA
 * File Name:FileException.java
 * Package Name:com.cmtech.dsp.exception
 * Date:2018年2月11日上午10:04:21
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.exception;

/**
 * ClassName: FileException
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月11日 上午10:04:21 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class FileException extends DspException {
	private String fileName;
	public FileException(String fileName, String description) {
		super(DspExceptionCode.BMEFILE_OPERATION_ERR, description);
		this.fileName = fileName;
	}
	

	@Override
    public String toString() {
        return getClass().getSimpleName() + 
        		  	"{\"" + fileName + ':' + super.getDescription() + "\"}";
    }

}
