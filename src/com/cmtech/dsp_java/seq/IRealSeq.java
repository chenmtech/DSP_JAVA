/**
 * Project Name:DSP_JAVA
 * File Name:IRealSeq.java
 * Package Name:com.cmtech.dsp_java.seq
 * Date:2018年2月5日下午3:08:58
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp_java.seq;

/**
 * ClassName: IRealSeq
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月5日 下午3:08:58 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public interface IRealSeq extends ISeq {
	double get(int i);
	boolean set(int i, double element);
	double[] toArray();
	IRealSeq reverse();
	IRealSeq plus(double a);
	IRealSeq minus(double a);
	IRealSeq multiple(double a);
	IRealSeq divide(double a);
	double sum();
	double max();
	double min();
}
