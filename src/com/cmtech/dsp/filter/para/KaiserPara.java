/**
 * Project Name:DSP_JAVA
 * File Name:KaiserPara.java
 * Package Name:com.cmtech.dsp.filter.para
 * Date:2018年2月16日上午8:11:11
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.filter.para;

import com.cmtech.dsp.filter.design.FilterType;
import com.cmtech.dsp.filter.design.WinType;

/**
 * ClassName: KaiserPara
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月16日 上午8:11:11 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class KaiserPara extends FIRPara {
	private double beta = 0.0;
	
	public KaiserPara() {
		super.setWinType(WinType.KAISER);
	}

	public KaiserPara(double[] wp, double[] ws, double Rp, double As, FilterType fType) {
		super(wp, ws, Rp, As, fType);
		super.setWinType(WinType.KAISER);
	}
	
	public KaiserPara(double[] wp, double[] ws, 
				   double Rp, double As, FilterType fType,
				   int N, double[] wc, double beta) {
		super(wp, ws, Rp, As, fType, N, wc, WinType.KAISER);

		this.beta = beta;
	}

	public double getBeta() {
		return beta;
	}

	public void setBeta(double beta) {
		this.beta = beta;
	}
	
	@Override
	public String toString() {
		return super.toString() + ',' + "beta=" + beta; 
	}
	
	
}
