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


public class IIRDCBlockStructure extends AbstractDFStructure {
	private double b = 0.0;
	private double a = 0.0;
	private double xn = 0.0;
	private double yn = 0.0;
	
	public IIRDCBlockStructure(Double[] b2, Double[] a2) {
		if(b2.length != 2 || a2.length != 2) {
			throw new IllegalArgumentException();
		}
		
		if(a2[0] != 1.0) {
			b = b2[0]/a2[0];
			a = a2[1]/a2[0];
		} else {
			b = b2[0];
			a = a2[1];
		}
	}
	
	public IIRDCBlockStructure(IIRFilter filter) {
		this(filter.getB(), filter.getA());
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

