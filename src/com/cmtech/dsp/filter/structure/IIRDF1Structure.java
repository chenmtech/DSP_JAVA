/**
 * Project Name:DSP_JAVA
 * File Name:IIRDF1Structure.java
 * Package Name:com.cmtech.dsp.filter.structure
 * Date:2018年2月17日下午3:17:47
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.filter.structure;

import java.util.Arrays;

import com.cmtech.dsp.filter.IIRFilter;


public class IIRDF1Structure extends AbstractDFStructure {
	private Double[] bm;
	private Double[] ak;
	private double[] xm;
	private double[] yk;
	private int M;
	private int N;
	
	public IIRDF1Structure(Double[] b, Double[] a) {
		M = b.length;
		N = a.length;
		bm = Arrays.copyOf(b, M);
		ak = Arrays.copyOf(a, N);
		xm = new double[M];
		yk = new double[N];
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
	
	public IIRDF1Structure(IIRFilter filter) {
		this(filter.getB(), filter.getA());
	}
	
	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * @see com.cmtech.dsp.filter.structure.IDigitalFilterStructure#filter(double)
	 */
	@Override
	public double filter(double x) {
		//先计算全零点部分，见幻灯第5页 
	    double wn = 0.0;
	    int i = 0;
	    for(i = M-1; i > 0; i--)
	    {
	        wn += (bm[i] * (xm[i] = xm[i-1]));        
	    }
	    wn += (bm[0] * (xm[0] = x));
	    
	    //再计算全极点部分，见幻灯第6页 
	    for(i = N-1; i > 0; i--)
	    {
	        wn -= (ak[i] * (yk[i] = yk[i-1]));
	    }    
	    return (yk[0] = wn);
	}

}
