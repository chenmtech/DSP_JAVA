/**
 * Project Name:DSP_JAVA
 * File Name:ISeq1.java
 * Package Name:com.cmtech.dsp.seq
 * Date:2018年2月25日上午5:50:06
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.seq;

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
public interface ISeq<T> extends Serializable{
	ISeqBaseOperator<T> getSeqBaseOperator();
	int size();
	ISeq<T> setToZero(int N);
	T get(int i);
	boolean set(int i, T element);
	ISeq<T> clear();
	ISeq<T> changeSize(int N);
	RealSeq abs();
	RealSeq angle();
	ISeq<T> reverse();
	ISeq<T> plus(T a);
	ISeq<T> minus(T a);
	ISeq<T> multiple(T a);
	ISeq<T> divide(T a);
	T sum();
	ISeq<T> append(T ele);
	ComplexSeq dtft(RealSeq omega);
	ComplexSeq dtft(int N);
	ComplexSeq fft();
	ComplexSeq fft(int N);
	ComplexSeq ifft();
	ComplexSeq ifft(int N);
	
}
