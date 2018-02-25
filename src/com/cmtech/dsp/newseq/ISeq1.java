/**
 * Project Name:DSP_JAVA
 * File Name:ISeq1.java
 * Package Name:com.cmtech.dsp.seq
 * Date:2018年2月25日上午5:50:06
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.newseq;

import java.io.Serializable;

/**
 * ClassName: ISeq1
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月25日 上午5:50:06 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public interface ISeq1<T> extends Serializable{
	int size();
	ISeq1<T> setToZero(int N);
	T get(int i);
	boolean set(int i, T element);
	ISeq1<T> clear();
	ISeq1<T> changeSize(int N);
	RealSeq1 abs();
	RealSeq1 angle();
	T[] toArray();
	T[] toArray(int N);
	ISeq1<T> reverse();
	ISeq1<T> plus(T a);
	ISeq1<T> minus(T a);
	ISeq1<T> multiple(T a);
	ISeq1<T> divide(T a);
	T sum();
	
	
	
/*	ComplexSeq dtft(RealSeq omega);
	ComplexSeq dtft(int N);
	ComplexSeq fft();
	ComplexSeq fft(int N);
	ComplexSeq ifft();
	ComplexSeq ifft(int N);

	*/
}
