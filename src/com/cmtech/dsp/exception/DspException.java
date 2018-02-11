/**
 * Project Name:DSP_JAVA
 * File Name:DspException.java
 * Package Name:com.cmtech.dsp.exception
 * Date:2018年2月11日上午7:06:11
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.exception;

import java.io.Serializable;

/**
 * ClassName: DspException
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月11日 上午7:06:11 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class DspException extends Exception implements Serializable {
	private DspExceptionCode code;
    private String description;

    public DspException(DspExceptionCode code, String description) {
        this.code = code;
        this.description = description;
    }

    public DspExceptionCode getCode() {
        return code;
    }

    public DspException setCode(DspExceptionCode code) {
        this.code = code;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DspException setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + 
        		  	'{' +
                "code=" + code +
                ", description='" + description + '\'' +
                '}';
    }
}
