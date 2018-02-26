/**
 * Project Name:DSP_JAVA
 * File Name:FIRDFStructure.java
 * Package Name:com.cmtech.dsp.filter.structure
 * Date:2018年2月17日上午6:34:03
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.filter.structure;

import java.util.Arrays;

import com.cmtech.dsp.filter.FIRFilter;

/**
 * ClassName: FIRDFStructure
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月17日 上午6:34:03 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class FIRDFStructure extends AbstractDFStructure {
	private double[] h_n;
	private double[] x_n;
	private int N;
	
	public FIRDFStructure(double[] h_n) {
		N = h_n.length;
		this.h_n = Arrays.copyOf(h_n, N);
		x_n = new double[N];		
	}
	
	public FIRDFStructure(FIRFilter filter) {
		this(filter.getB().toArray()); 
	}
	
	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * @see com.cmtech.dsp.filter.structure.IDFStructure#filter(java.lang.Object)
	 */
	@Override
	public double filter(double x) {
		double y = 0.0;
	    int i = 0;
	    for(i = N-1; i > 0; i--)
	    {
		    	x_n[i] = x_n[i-1];
		    	y += (h_n[i] * x_n[i]);
	    }
	    y += (h_n[0] * x);
	    x_n[0] = x;
	    
	    return y;
	}

}
