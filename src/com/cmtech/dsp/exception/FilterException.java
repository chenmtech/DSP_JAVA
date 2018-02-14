/**
 * Project Name:DSP_JAVA
 * File Name:FilterException.java
 * Package Name:com.cmtech.dsp.exception
 * Date:2018年2月14日下午2:14:43
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.exception;

/**
 * ClassName: FilterException
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月14日 下午2:14:43 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class FilterException extends DspException {
	public FilterException(String description) {
		super(DspExceptionCode.FILTER_ERR, description);
	}
	

	@Override
    public String toString() {
        return getClass().getSimpleName() + 
        		  	"{\"" + super.getDescription() + "\"}";
    }
}
