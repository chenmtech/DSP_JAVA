/**
 * Project Name:DSP_JAVA
 * File Name:RealSeqEleOperator.java
 * Package Name:com.cmtech.dsp.seq
 * Date:2018年2月25日上午5:56:56
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.seq;


public class RealSeqBaseOperator implements ISeqBaseOperator<Double>{


	@Override
	public Double zeroElement() {
		return 0.0;
	}
	
	@Override
	public Double newElement(Double ele) {
		return ele;
	}


	@Override
	public Double[] newArray(int N) {
		Double[] out = new Double[N];
		for(int i = 0; i < N; i++) {
			out[i] = 0.0;
		}
		return out;
	}


	@Override
	public Double add(Double d1, Double d2) {
		return d1+d2;
	}

	@Override
	public Double subtract(Double d1, Double d2) {
		return d1-d2;
	}
	
	@Override
	public Double multiple(Double d1, Double d2) {
		return d1*d2;
	}
	
	@Override
	public Double divide(Double d1, Double d2) {
		return d1/d2;
	}

	@Override
	public double abs(Double ele) {
		return Math.abs(ele);
	}

	@Override
	public double angle(Double ele) {
		return Math.atan2(0, ele);
	}

	@Override
	public Seq<Double> newInstance() {
		return new RealSeq();
	}



}
