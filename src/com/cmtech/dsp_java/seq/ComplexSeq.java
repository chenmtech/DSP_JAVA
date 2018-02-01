package com.cmtech.dsp_java.seq;

import java.util.Collection;

public class ComplexSeq extends AbstractSeq<Complex> {
	
	public ComplexSeq() {
		data = new Complex[0];
	}
	
	public ComplexSeq(int N) {
		setAsZeroSequence(N);
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
		int N = Math.min(re.size(), im.size());
		data = new Complex[N];
		for(int i = 0; i < N; i++) {
			data[i] = new Complex(re.get(i), im.get(i));
		}
	}
	
	public ComplexSeq(RealSeq re) {
		this(re, new RealSeq(re.size()));
	}
	
	
	@Override
	public void setAsZeroSequence(int N) {
		data = new Complex[N];
		for(int i = 0; i < N; i++) {
			data[i] = new Complex();
		}
	}


	@Override
	public void changeSize(int N) {
		Complex[] buf = new Complex[N];
		for(int i = 0; i < N; i++) {
			if(i < data.length) buf[i] = data[i];
			else buf[i] = new Complex();
		}
		data = buf;
	}

	@Override
	public void clear() {
		data = new Complex[0];
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
	public ComplexSeq add(Complex a) {
		ComplexSeq out = new ComplexSeq(this);
		for(int i = 0; i < size(); i++) {
			out.data[i].add(a);
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
	public RealSeq abs() {
		RealSeq out = new RealSeq(size());
		for(int i = 0; i < size(); i++) {
			out.data[i] = data[i].abs();
		}
		return out;
	}
	
	public RealSeq angle() {
		RealSeq out = new RealSeq(size());
		for(int i = 0; i < size(); i++) {
			out.data[i] = data[i].angle();
		}
		return out;
	}

	@Override
	public Complex sum() {
		// TODO Auto-generated method stub
		Complex sum = new Complex();
		for(Complex ele : data) {
			sum.add(ele);
		}
		return sum;
	}

	@Override
	public ComplexSeq dtft(RealSeq omega) {
		// TODO Auto-generated method stub
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
	
	@Override
	public ComplexSeq dtft(int N) {
		// TODO Auto-generated method stub
		ComplexSeq out = dtft(SeqFactory.linSpace(0, Math.PI, N));
		return out;
	}
	

}
