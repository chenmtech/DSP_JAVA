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

/**
 * ClassName: IIRNotchStructure
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年4月2日 下午3:58:35 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class IIRNotchStructure extends AbstractDFStructure {
	private double b1 = 0.0;
	private double b2 = 0.0;
	private double a1 = 0.0;
	private double a2 = 0.0;
	private double xn_1 = 0.0;
	private double xn_2 = 0.0;
	private double yn_1 = 0.0;
	private double yn_2 = 0.0;
	
	public IIRNotchStructure(double[] b_m, double[] a_k) {
		if(b_m.length != 3 || a_k.length != 3) {
			throw new IllegalArgumentException();
		}
		
		if(a_k[0] != 1.0) {
			b1 = b_m[1]/a_k[0];
			b2 = b_m[2]/a_k[0];
			a1 = a_k[1]/a_k[0];
			a2 = a_k[2]/a_k[0];
		} else {
			b1 = b_m[1];
			b2 = b_m[2];
			a1 = a_k[1];
			a2 = a_k[2];
		}
	}
	
	public IIRNotchStructure(IIRFilter filter) {
		this(filter.getB().toArray(), filter.getA().toArray());
	}
	
	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * @see com.cmtech.dsp.filter.structure.IDFStructure#filter(double)
	 */
	@Override
	public double filter(double x) {
		double y = b2*(x+xn_2)+b1*xn_1-a1*yn_1-a2*yn_2;
		xn_2 = xn_1; xn_1 = x;
		yn_2 = yn_1; yn_1 = y;
		
		return y;
	}

}
