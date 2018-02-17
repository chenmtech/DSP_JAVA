/**
 * Project Name:DSP_JAVA
 * File Name:IIRTDF2Structure.java
 * Package Name:com.cmtech.dsp.filter.structure
 * Date:2018年2月17日下午3:35:45
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.filter.structure;

import java.util.Arrays;

import com.cmtech.dsp.filter.IIRFilter;

/**
 * ClassName: IIRTDF2Structure
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月17日 下午3:35:45 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class IIRTDF2Structure extends AbstractDFStructure {
	private double[] bm;
	private double[] ak;
	private double[] wi;
	private int M;
	private int N;
	
	public IIRTDF2Structure(double[] b_m, double[] a_k) {
		M = b_m.length;
		N = a_k.length;
		bm = Arrays.copyOf(b_m, M);
		ak = Arrays.copyOf(a_k, N);
		wi = new double[N];
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
	
	public IIRTDF2Structure(IIRFilter filter) {
		this(filter.getB().toArray(), filter.getA().toArray());
	}
	
	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * @see com.cmtech.dsp.filter.structure.IDFStructure#filter(double)
	 */
	@Override
	public double filter(double x) {
		//见幻灯17页 
	    double y = bm[0]*x + wi[1];
	    for(int i = 1; i < N-1; i++)
	    {
	        wi[i] = wi[i+1] - ak[i]*y + ( (i < M) ? (bm[i]*x) : 0.0 );
	    }
	    wi[N-1] = -ak[N-1]*y + ( (M == N) ? (bm[N-1]*x) : 0.0 );
	    return y;
	}

}
