/**
 * Project Name:DSP_JAVA
 * File Name:IIRDF2Structure.java
 * Package Name:com.cmtech.dsp.filter.structure
 * Date:2018年2月17日下午3:30:47
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.filter.structure;

import java.util.Arrays;

import com.cmtech.dsp.filter.IIRFilter;

/**
 * ClassName: IIRDF2Structure
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月17日 下午3:30:47 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class IIRDF2Structure extends AbstractDFStructure {
	private double[] bm;
	private double[] ak;
	private double[] wn;
	private int M;
	private int N;
	
	public IIRDF2Structure(double[] b_m, double[] a_k) {
		M = b_m.length;
		N = a_k.length;
		bm = Arrays.copyOf(b_m, M);
		ak = Arrays.copyOf(a_k, N);
		wn = new double[N];
		if(ak[0] != 1.0) {
			for(int i = 0; i < M; i++) {
				bm[i] /= ak[0];
			}
			for(int i = 1; i < N; i++) {
				ak[i] /= ak[0];
			}
			ak[0] = 1.0;
		}
	}
	
	public IIRDF2Structure(IIRFilter filter) {
		this(filter.getB().toArray(), filter.getA().toArray());
	}
	
	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * @see com.cmtech.dsp.filter.structure.IDFStructure#filter(double)
	 */
	@Override
	public double filter(double x) {
		int i = 0;
	    double wSum = 0.0;
	    double ySum = 0.0;
	    //见幻灯第13页 
	    for(i = N-1; i > 0; i--)
	    {
	        wSum -= (ak[i] * (wn[i] = wn[i-1]));
	        if(i < M) ySum += (bm[i] * wn[i]);
	    } 
	    wSum += x;
	    return ( ySum += (bm[0] * (wn[0] = wSum)) );
	}

}
