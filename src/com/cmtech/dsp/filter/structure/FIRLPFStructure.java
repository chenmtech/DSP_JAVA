/**
 * Project Name:DSP_JAVA
 * File Name:FIRLPFStructure.java
 * Package Name:com.cmtech.dsp.filter.structure
 * Date:2018年2月17日上午7:24:25
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.filter.structure;

import java.util.Arrays;

import com.cmtech.dsp.filter.FIRFilter;

/**
 * ClassName: FIRLPFStructure
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月17日 上午7:24:25 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public abstract class FIRLPFStructure extends AbstractDFStructure {
	protected double[] h_n;
	protected double[] x_n;
	protected int N;
	protected int hN;
	
	public FIRLPFStructure(double[] h_n) {
		N = h_n.length;
		this.h_n = Arrays.copyOf(h_n, N);
		x_n = new double[N];		
		hN = (N % 2 == 0) ? N/2-1 : (N-1)/2;
	}
	
	public FIRLPFStructure(FIRFilter filter) {
		this(filter.getB().toArray());
	}
	


}
