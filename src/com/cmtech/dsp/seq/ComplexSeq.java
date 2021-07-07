package com.cmtech.dsp.seq;

/*
Copyright (c) 2008 chenm
*/

import java.util.Collection;

import com.cmtech.dsp.util.Complex;
import com.cmtech.dsp.util.SeqUtil;

/**
 * A ComplexSeq is a sequence with complex values
 * @author chenm
 * @version 2008-06
 */
public class ComplexSeq extends Seq<Complex> {

	private static final long serialVersionUID = 1L;
	
	private static final IElementBasicOperator<Complex> ELEMENT_OP = new ComplexBasicOperator();
	
	@Override
	public IElementBasicOperator<Complex> getBasicOperator() {
		return ELEMENT_OP;
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
			data.add(ele);
		}
	}
	
	public ComplexSeq(Collection<Complex> d) {
		super(d);
	}
	
	public ComplexSeq(ComplexSeq seq) {
		super(seq);
	}
	
	public <T> ComplexSeq(Seq<T> seq) {
		super();
		for(int i = 0; i < seq.size(); i++) {
			data.add(new Complex((double)seq.get(i), 0.0));
		}
	}
	
	public <T> ComplexSeq(Seq<T> re, Seq<T> im) {
		super();
		int N = Math.max(re.size(), im.size());
		re.reSize(N);
		im.reSize(N);
		for(int i = 0; i < N; i++) {
			data.add(new Complex((double)re.get(i), (double)im.get(i)));
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

}
