/**
 * Project Name:DSP_JAVA
 * File Name:ComplexSeq1.java
 * Package Name:com.cmtech.dsp.seq.newseq
 * Date:2018年2月25日上午6:40:29
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.newseq;

import java.util.Collection;

import com.cmtech.dsp.seq.Complex;
import com.cmtech.dsp.seq.RealSeq;

/**
 * ClassName: ComplexSeq1
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月25日 上午6:40:29 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class ComplexSeq1 extends AbstractSeq<Complex> {

	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * @since JDK 1.6
	 */
	private static final long serialVersionUID = 1L;
	
	private static final ISeqEleOperator<Complex> EOP = new ComplexSeqEleOperator();
	
	@Override
	public ISeqEleOperator<Complex> getSeqEleOperator() {
		return EOP;
	}
	
	public ComplexSeq1() {
		super();
	}
	
	public ComplexSeq1(int N) {
		super(N);
	}
	
	public ComplexSeq1(Complex...d) {
		super(d);
	}
	
	public ComplexSeq1(Collection<Complex> d) {
		super(d);
	}
	
	public ComplexSeq1(ComplexSeq1 seq) {
		super(seq);
	}
	
	public ComplexSeq1(RealSeq1 re, RealSeq1 im) {
		super();
		int N = Math.max(re.size(), im.size());
		re.changeSize(N);
		im.changeSize(N);
		for(int i = 0; i < N; i++) {
			data.add(new Complex(re.get(i), im.get(i)));
		}
	}
	
	public ComplexSeq1(RealSeq1 re) {
		this(re, new RealSeq1(re.size()));
	}

	public RealSeq1 dB() {
		RealSeq1 out = abs();
		double max = out.max();
		for(int i = 0; i < out.size(); i++) {			
			out.set(i, 20*Math.log10(out.get(i)/max));
		}
		return out;	
	}
}
