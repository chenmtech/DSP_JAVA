package com.cmtech.dsp.seq;
/*
Copyright (c) 2008 chenm
*/
public class DoubleBasicOperator implements INumBasicOperator<Double>{

	@Override
	public Double zeroElement() {
		return 0.0;
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
	public Seq<Double> newSeq() {
		return new RealSeq();
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
	public Double multiply(Double d1, Double d2) {
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
}
