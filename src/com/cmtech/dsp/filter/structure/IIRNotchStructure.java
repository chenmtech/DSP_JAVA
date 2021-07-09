/**
 * Project Name:DSP_JAVA
 * File Name:IIRNotchStructure.java
 * Package Name:com.cmtech.dsp.filter.structure
 * Date:2018年4月2日下午3:58:35
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.filter.structure;

import com.cmtech.dsp.filter.IIRFilter;


public class IIRNotchStructure extends AbstractDFStructure {
	private double b1 = 0.0;
	private double b2 = 0.0;
	private double a1 = 0.0;
	private double a2 = 0.0;
	private double xn_1 = 0.0;
	private double xn_2 = 0.0;
	private double yn_1 = 0.0;
	private double yn_2 = 0.0;
	
	public IIRNotchStructure(Double[] b, Double[] a) {
		if(b.length != 3 || a.length != 3) {
			throw new IllegalArgumentException();
		}
		
		if(a[0] != 1.0) {
			b1 = b[1]/a[0];
			b2 = b[2]/a[0];
			a1 = a[1]/a[0];
			a2 = a[2]/a[0];
		} else {
			b1 = b[1];
			b2 = b[2];
			a1 = a[1];
			a2 = a[2];
		}
	}
	
	public IIRNotchStructure(IIRFilter filter) {
		this(filter.getB(), filter.getA());
	}
	
	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * @see com.cmtech.dsp.filter.structure.IDigitalFilterStructure#filter(double)
	 */
	@Override
	public double filter(double x) {
		double y = b2*(x+xn_2)+b1*xn_1-a1*yn_1-a2*yn_2;
		xn_2 = xn_1; xn_1 = x;
		yn_2 = yn_1; yn_1 = y;
		
		return y;
	}

}
