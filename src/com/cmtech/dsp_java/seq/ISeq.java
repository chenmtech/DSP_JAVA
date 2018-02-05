/**
 * Project Name:DSP_JAVA
 * File Name:ISeq.java
 * Package Name:com.cmtech.dsp_java.seq
 * Date:2018年2月5日下午3:06:38
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp_java.seq;

/**
 * ClassName: ISeq
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月5日 下午3:06:38 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public interface ISeq {
	void initToZeroSequence(int N);
	void clear();
	int size();
	void changeSize(int N);
	RealSeq abs();
	RealSeq angle();
	ComplexSeq dtft(RealSeq omega);
	ComplexSeq dtft(int N);
	ComplexSeq fft();
}
