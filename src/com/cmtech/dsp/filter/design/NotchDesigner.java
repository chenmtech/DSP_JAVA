/**
 * Project Name:DSP_JAVA
 * File Name:NotchDesigner.java
 * Package Name:com.cmtech.dsp.filter.design
 * Date:2018年2月17日下午4:23:49
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.filter.design;

import com.cmtech.dsp.filter.IIRFilter;
import com.cmtech.dsp.filter.structure.StructType;

/**
 * ClassName: NotchDesigner
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月17日 下午4:23:49 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class NotchDesigner {
	private NotchDesigner() {
		
	}
	
	public static synchronized IIRFilter design(double w0, double deltaw) {
		double[] b = new double[3];
		double[] a = new double[3];
		double r = 1-deltaw/2;
		double cosw0 = Math.cos(w0);
		b[0] = (1+r*r-2*r*cosw0)/(2-2*cosw0);
		b[1] = -2*cosw0*b[0];
		b[2] = b[0];
		a[0] = 1;
		a[1] = -2*r*cosw0;
		a[2] = r*r;
		return new IIRFilter(b,a).createStructure(StructType.IIR_DF2);
	}
	
	public static synchronized IIRFilter design(double f0, double deltaf, double fs) {
		return design(2*Math.PI*f0/fs, 2*Math.PI*deltaf/fs);
	}
}
