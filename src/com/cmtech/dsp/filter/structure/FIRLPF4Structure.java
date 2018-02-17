/**
 * Project Name:DSP_JAVA
 * File Name:FIRLPF4Structure.java
 * Package Name:com.cmtech.dsp.filter.structure
 * Date:2018年2月17日上午7:38:29
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.filter.structure;

import com.cmtech.dsp.filter.FIRFilter;

/**
 * ClassName: FIRLPF4Structure
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月17日 上午7:38:29 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class FIRLPF4Structure extends FIRLPFStructure {
	public FIRLPF4Structure(double[] h_n) {
		super(h_n);
	}
	
	public FIRLPF4Structure(FIRFilter filter) {
		super(filter);
	}
	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * @see com.cmtech.dsp.filter.structure.IDFStructure#filter(double)
	 */
	@Override
	public double filter(double x) {
		int i = 0;
		double y = 0.0;
		for(i = N-1; i > 0; i--)
	        x_n[i] = x_n[i-1];
	    x_n[0] = x;
	    
	    for(i = 0; i <= hN; i++)
            y += (h_n[i] * (x_n[i] - x_n[N-1-i])); 
	    
	    return y;
	}

}
