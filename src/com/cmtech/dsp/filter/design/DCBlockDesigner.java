/**
 * Project Name:DSP_JAVA
 * File Name:DCBlockDesigner.java
 * Package Name:com.cmtech.dsp.filter.design
 * Date:2018年2月17日下午4:14:35
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.filter.design;

import com.cmtech.dsp.filter.IIRFilter;
import com.cmtech.dsp.filter.structure.StructType;

/**
 * ClassName: DCBlockDesigner
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月17日 下午4:14:35 
 *
 * @author bme
 * @version 0.0.1
 * @since JDK 1.6
 */
public class DCBlockDesigner {
	private DCBlockDesigner() {
		
	}
	
	public static synchronized IIRFilter design(double deltaw) {
		Double[] b = new Double[2];
		Double[] a = new Double[2];
		double r = 1-deltaw;
		b[0] = (1+r)/2;
		b[1] = -b[0];
		a[0] = 1.0;
		a[1] = -r;
		return new IIRFilter(b,a).createStructure(StructType.IIR_DF2);
	}
	
	public static synchronized IIRFilter design(double deltaf, double fs) {
		return design(2*Math.PI*deltaf/fs);
	}

}
