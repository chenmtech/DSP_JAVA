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

/**
 * ClassName: IIRDF1Structure
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月17日 下午3:17:47 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class IIRDF1Structure extends AbstractDFStructure {
	private double[] bm;
	private double[] ak;
	private double[] xm;
	private double[] yk;
	private int M;
	private int N;
	
	public IIRDF1Structure(double[] b_m, double[] a_k) {
		M = b_m.length;
		N = a_k.length;
		bm = Arrays.copyOf(b_m, M);
		ak = Arrays.copyOf(a_k, N);
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
		this(filter.getB().toArray(), filter.getA().toArray());
	}
	
	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * @see com.cmtech.dsp.filter.structure.IDFStructure#filter(double)
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
