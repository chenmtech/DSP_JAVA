package com.cmtech.dsp_java.seq;

import java.util.Collection;

/**
 * ClassName: ComplexSeq
 * Function: 复序列. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月2日 上午5:56:24 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class ComplexSeq extends AbstractSeq<Complex> {
	
	public ComplexSeq() {
		data = new Complex[0];
	}
	
	public ComplexSeq(int N) {
		setToAllZeroSequence(N);
	}
	
	public ComplexSeq(Complex... d) {
		super(d);
	}
	
	public ComplexSeq(Collection<Complex> d) {
		super(d);
	}
	
	public ComplexSeq(ComplexSeq seq) {
		super(seq);
	}
	
	public ComplexSeq(RealSeq re, RealSeq im) {
		int N = Math.max(re.size(), im.size());
		re.changeSize(N);
		im.changeSize(N);
		data = new Complex[N];
		for(int i = 0; i < N; i++) {
			data[i] = new Complex(re.get(i), im.get(i));
		}
	}
	
	public ComplexSeq(RealSeq re) {
		this(re, new RealSeq(re.size()));
	}
	
	
	@Override
	public void setToAllZeroSequence(int N) {
		data = new Complex[N];
		for(int i = 0; i < N; i++) {
			data[i] = new Complex();
		}
	}


	@Override
	public void changeSize(int N) {
		if(size() == N) return;
		
		Complex[] buf = new Complex[N];
		for(int i = 0; i < N; i++) {
			if(i < data.length) buf[i] = data[i];
			else buf[i] = new Complex();
		}
		data = buf;
	}

	
	@Override
	public ComplexSeq reverse() {
		ComplexSeq out = new ComplexSeq(size());
		for(int i = 0; i < data.length; i++)
	    {
	        out.data[i] = data[data.length-1-i];
	    }
		return out;
	}
	
	@Override
	public ComplexSeq plus(Complex a) {
		ComplexSeq out = new ComplexSeq(this);
		for(int i = 0; i < size(); i++) {
			out.data[i].add(a);
		}
		return out;
	}
	
	
	@Override
	public ComplexSeq minus(Complex a) {
		ComplexSeq out = new ComplexSeq(this);
		for(int i = 0; i < size(); i++) {
			out.data[i].subtract(a);
		}
		return out;
	}

	@Override
	public ComplexSeq multiple(Complex a) {
		ComplexSeq out = new ComplexSeq(this);
		for(int i = 0; i < size(); i++) {
			out.data[i].multiple(a);
		}
		return out;
	}
	
	@Override
	public ComplexSeq divide(Complex a) {
		ComplexSeq out = new ComplexSeq(this);
		for(int i = 0; i < size(); i++) {
			out.data[i].divide(a);
		}
		return out;
	}
	
	@Override
	public RealSeq abs() {
		RealSeq out = new RealSeq(size());
		for(int i = 0; i < size(); i++) {
			out.data[i] = data[i].abs();
		}
		return out;
	}
	
	@Override
	public RealSeq angle() {
		RealSeq out = new RealSeq(size());
		for(int i = 0; i < size(); i++) {
			out.data[i] = data[i].angle();
		}
		return out;
	}

	@Override
	public Complex sum() {
		Complex sum = new Complex();
		for(Complex ele : data) {
			sum.add(ele);
		}
		return sum;
	}

	@Override
	public ComplexSeq dtft(RealSeq omega) {
		int Nw = omega.size();
		ComplexSeq out = new ComplexSeq(Nw);
		int N = size();
		ComplexSeq ejwn, tmpseq;
		for(int i = 0; i < Nw; i++) {
			ejwn = SeqFactory.createEJWSeq(-omega.get(i), 0, N);
			tmpseq = SeqUtil.multiple(this, ejwn);
			out.set(i, tmpseq.sum());
		}
		return out;
	}
	
	public RealSeq dB() {
		RealSeq out = abs();
		double max = out.max();
		for(int i = 0; i < out.size(); i++) {			
			out.set(i, 20*Math.log10(out.get(i)/max));
		}
		return out;	
	}
}
