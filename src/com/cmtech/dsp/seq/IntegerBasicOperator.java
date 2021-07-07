package com.cmtech.dsp.seq;

public class IntegerBasicOperator implements IElementBasicOperator<Integer> {

	@Override
	public Integer zeroElement() {
		return 0;
	}

	@Override
	public Integer[] newArray(int N) {
		return new Integer[N];
	}

	@Override
	public Seq<Integer> nullSeq() {
		return new IntSeq();
	}

	@Override
	public Integer add(Integer d1, Integer d2) {
		return d1+d2;
	}

	@Override
	public Integer subtract(Integer d1, Integer d2) {
		return d1-d2;
	}

	@Override
	public Integer multiply(Integer d1, Integer d2) {
		return d1*d2;
	}

	@Override
	public Integer divide(Integer d1, Integer d2) {
		return d1/d2;
	}

	@Override
	public double abs(Integer ele) {
		return Math.abs(ele);
	}

	@Override
	public double angle(Integer ele) {
		return Math.atan2(0, ele);
	}

}
