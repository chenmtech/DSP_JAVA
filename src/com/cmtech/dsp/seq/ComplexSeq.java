/**
 * Project Name:DSP_JAVA
 * File Name:ComplexSeq1.java
 * Package Name:com.cmtech.dsp.seq.newseq
 * Date:2018年2月25日上午6:40:29
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.seq;

import java.util.Collection;

import com.cmtech.dsp.util.Complex;
import com.cmtech.dsp.util.SeqUtil;

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
public class ComplexSeq extends Seq<Complex> {

	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * @since JDK 1.6
	 */
	private static final long serialVersionUID = 1L;
	
	private static final ISeqBaseOperator<Complex> EOP = new ComplexSeqBaseOperator();
	
	@Override
	public ISeqBaseOperator<Complex> getSeqBaseOperator() {
		return EOP;
	}
	
	public ComplexSeq() {
		super();
	}
	
	public ComplexSeq(int N) {
		super(N);
	}
	
	public ComplexSeq(Complex...d) {
		super();
		for(Complex ele : d) {
			data.add(eOp.newElement(ele));
		}
	}
	
	public ComplexSeq(Collection<Complex> d) {
		super(d);
	}
	
	public ComplexSeq(ComplexSeq seq) {
		super(seq);
	}
	
	public ComplexSeq(RealSeq re, RealSeq im) {
		super();
		int N = Math.max(re.size(), im.size());
		re.changeSize(N);
		im.changeSize(N);
		for(int i = 0; i < N; i++) {
			data.add(new Complex(re.get(i), im.get(i)));
		}
	}
	
	public ComplexSeq(double[] re, double[] im) {
		int N = Math.max(re.length, im.length);
		double R = 0.0;
		double I = 0.0;
		for(int i = 0; i < N; i++) {
			R = (i < re.length) ? re[i] : 0.0;
			I = (i < im.length) ? im[i] : 0.0;
			data.add(new Complex(R, I));
		}
	}
	
	public ComplexSeq(RealSeq re) {
		this(re, new RealSeq(re.size()));
	}

	public RealSeq dB() {
		RealSeq out = abs();
		double max = out.max();
		for(int i = 0; i < out.size(); i++) {			
			out.set(i, 20*Math.log10(out.get(i)/max));
		}
		return out;	
	}
	
	@Override
	public ComplexSeq dtft(RealSeq omega) {
		int Nw = omega.size();
		ComplexSeq out = new ComplexSeq(Nw);
		int N = size();
		ComplexSeq ejwn, tmpseq;
		for(int i = 0; i < Nw; i++) {
			ejwn = SeqUtil.createEJWSeq(-omega.get(i), 0, N);
			tmpseq = (ComplexSeq)SeqUtil.multiple(this, ejwn);
			out.set(i, tmpseq.sum());
		}
		return out;
	}
	
	@Override
	public ComplexSeq dtft(int N) {
		//复序列的DTFT没有对称性，所以需要求0~2*PI整个周期的频谱
		return dtft(SeqUtil.linSpace(0, 2*Math.PI, N));	
	}
	
	public RealSeq getReal() {
		RealSeq out = new RealSeq();
		for(int i = 0; i < size(); i++) {
			out.append(data.get(i).getReal());
		}
		return out;
	}
	
	public RealSeq getImag() {
		RealSeq out = new RealSeq();
		for(int i = 0; i < size(); i++) {
			out.append(data.get(i).getImag());
		}
		return out;
	}

	public Complex[] toArray() {
		return toArray(size());
	}

	public Complex[] toArray(int N) {
		Complex[] rtn = eOp.newArray(N);
		int min = Math.min(N, size());
		for(int i = 0; i < min; i++) {
			rtn[i] = eOp.newElement(data.get(i));
		}
		return rtn;
	}

}
