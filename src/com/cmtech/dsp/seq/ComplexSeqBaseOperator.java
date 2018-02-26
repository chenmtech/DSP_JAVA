/**
 * Project Name:DSP_JAVA
 * File Name:ComplexSeqEleOperator.java
 * Package Name:com.cmtech.dsp.seq
 * Date:2018年2月25日上午5:58:33
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.seq;

import com.cmtech.dsp.util.Complex;

/**
 * ClassName: ComplexSeqEleOperator
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月25日 上午5:58:33 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class ComplexSeqBaseOperator implements ISeqBaseOperator<Complex>{

	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * @see com.cmtech.dsp.seq.newseq.ISeqBaseOperator#zeroElement()
	 */
	@Override
	public Complex zeroElement() {
		return new Complex();
	}
	
	@Override
	public Complex newElement(Complex ele) {
		return new Complex(ele);
	}
	
	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * @see com.cmtech.dsp.seq.newseq.ISeqBaseOperator#newArray(int)
	 */
	@Override
	public Complex[] newArray(int N) {
		Complex[] out = new Complex[N];
		for(int i = 0; i < N; i++) {
			out[i] = new Complex();
		}
		return out;
	}


	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * @see com.cmtech.dsp.seq.newseq.ISeqBaseOperator#add(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Complex add(Complex d1, Complex d2) {
		return Complex.add(d1, d2);
	}
	
	@Override
	public Complex subtract(Complex d1, Complex d2) {
		return Complex.subtract(d1, d2);
	}
	
	@Override
	public Complex multiple(Complex d1, Complex d2) {
		return Complex.multiple(d1, d2);
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

	@Override
	public Seq<Complex> newInstance() {
		return new ComplexSeq();
	}
}
