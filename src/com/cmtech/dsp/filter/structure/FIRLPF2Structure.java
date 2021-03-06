/**
 * Project Name:DSP_JAVA
 * File Name:FIRLPF2Structure.java
 * Package Name:com.cmtech.dsp.filter.structure
 * Date:2018年2月17日上午7:23:25
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.filter.structure;

import com.cmtech.dsp.filter.FIRFilter;


public class FIRLPF2Structure extends FIRLPFStructure {
	
	public FIRLPF2Structure(Double[] b) {
		super(b);
	}
	
	public FIRLPF2Structure(FIRFilter filter) {
		super(filter);
	}

	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * @see com.cmtech.dsp.filter.structure.IDigitalFilterStructure#filter(double)
	 */
	@Override
	public double filter(double x) {
		int i = 0;
		double y = 0.0;
		for(i = N-1; i > 0; i--)
	        x_n[i] = x_n[i-1];
	    x_n[0] = x;
	    
	    for(i = 0; i <= hN; i++)
            y += (h_n[i] * (x_n[i] + x_n[N-1-i])); 
		return y;
	}

}
