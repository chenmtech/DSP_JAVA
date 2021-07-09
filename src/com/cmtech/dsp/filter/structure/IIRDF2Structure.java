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


public class IIRDF2Structure extends AbstractDFStructure {
	private Double[] bm;
	private Double[] ak;
	private double[] wn;
	private int M;
	private int N;
	
	public IIRDF2Structure(Double[] b, Double[] a) {
		M = b.length;
		N = a.length;
		bm = Arrays.copyOf(b, M);
		ak = Arrays.copyOf(a, N);
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
		this(filter.getB(), filter.getA());
	}
	
	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * @see com.cmtech.dsp.filter.structure.IDigitalFilterStructure#filter(double)
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
