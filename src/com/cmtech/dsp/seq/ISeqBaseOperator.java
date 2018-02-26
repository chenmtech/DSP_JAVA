/**
 * Project Name:DSP_JAVA
 * File Name:SeqEleOperator.java
 * Package Name:com.cmtech.dsp.seq
 * Date:2018年2月25日上午5:53:49
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.seq;

/**
 * ClassName: SeqEleOperator
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月25日 上午5:53:49 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public interface ISeqBaseOperator<T> {
	T zeroElement();
	T newElement(T ele);
	T[] newArray(int N);
	T add(T d1, T d2);
	T subtract(T d1, T d2);
	T multiple(T d1, T d2);
	T divide(T d1, T d2);
	double abs(T ele);
	double angle(T ele);
	Seq<T> newInstance();
}
