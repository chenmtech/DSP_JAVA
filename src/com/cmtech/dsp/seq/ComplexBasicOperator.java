package com.cmtech.dsp.seq;

/*
Copyright (c) 2008 chenm
*/

import com.cmtech.dsp.util.Complex;


public class ComplexBasicOperator implements INumBasicOperator<Complex>{


	@Override
	public Complex zeroElement() {
		return new Complex();
	}	

	@Override
	public Complex[] newArray(int N) {
		Complex[] out = new Complex[N];
		for(int i = 0; i < N; i++) {
			out[i] = new Complex();
		}
		return out;
	}

	@Override
	public Seq<Complex> newSeq() {
		return new ComplexSeq();
	}
	
	@Override
	public Complex add(Complex d1, Complex d2) {
		return Complex.add(d1, d2);
	}
	
	@Override
	public Complex subtract(Complex d1, Complex d2) {
		return Complex.subtract(d1, d2);
	}
	
	@Override
	public Complex multiply(Complex d1, Complex d2) {
		return Complex.multiply(d1, d2);
	}
	
	@Override
	public Complex divide(Complex d1, Complex d2) {
		return Complex.divide(d1, d2);
	}

	@Override
	public double abs(Complex ele) {
		return ele.abs();
	}

	@Override
	public double angle(Complex ele) {
		return ele.angle();
	}
}
