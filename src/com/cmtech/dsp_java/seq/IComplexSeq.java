/**
 * Project Name:DSP_JAVA
 * File Name:IComplexSeq.java
 * Package Name:com.cmtech.dsp_java.seq
 * Date:2018年2月5日下午3:12:46
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp_java.seq;

/**
 * ClassName: IComplexSeq
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月5日 下午3:12:46 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public interface IComplexSeq extends ISeq {
	Complex get(int i);
	boolean set(int i, Complex element);
	Complex[] toArray();
	IComplexSeq reverse();
	IComplexSeq plus(Complex a);
	IComplexSeq minus(Complex a);
	IComplexSeq multiple(Complex a);
	IComplexSeq divide(Complex a);
	Complex sum();
	IRealSeq dB();
}
