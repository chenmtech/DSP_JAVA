/**
 * Project Name:DSP_JAVA
 * File Name:IIRDCBlockStructure.java
 * Package Name:com.cmtech.dsp.filter.structure
 * Date:2018年4月2日下午3:12:44
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.filter.structure;

import com.cmtech.dsp.filter.IIRFilter;

/**
 * ClassName: IIRDCBlockStructure
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年4月2日 下午3:12:44 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class IIRDCBlockStructure extends AbstractDFStructure {
	private double b = 0.0;
	private double a = 0.0;
	private double xn = 0.0;
	private double yn = 0.0;
	
	public IIRDCBlockStructure(double[] b_m, double[] a_k) {
		if(b_m.length != 2 || a_k.length != 2) {
			throw new IllegalArgumentException();
		}
		
		if(a_k[0] != 1.0) {
			b = b_m[0]/a_k[0];
			a = a_k[1]/a_k[0];
		} else {
			b = b_m[0];
			a = a_k[1];
		}
	}
	
	public IIRDCBlockStructure(IIRFilter filter) {
		this(filter.getB().toArray(), filter.getA().toArray());
	}
	
	
	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * @see com.cmtech.dsp.filter.structure.IDFStructure#filter(double)
	 */
	@Override
	public double filter(double x) {
		double y = b*(x-xn)-a*yn;
		xn = x;
		return (yn = y);
	}

}

