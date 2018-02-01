package com.cmtech.dsp_java.seq;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class RealSeq extends AbstractSeq<Double> {
	
	public RealSeq() {
		data = new Double[0];
	}
	
	public RealSeq(int N) {
		setAsZeroSequence(N);
	}
	
	public RealSeq(Double...d) {
		super(d);
	}
	
	
	public RealSeq(Collection<Double> d) {
		super(d);
	}
	
	public RealSeq(RealSeq seq) {
		super(seq);
	}


	@Override
	public void setAsZeroSequence(int N) {
		// TODO Auto-generated method stub
		data = new Double[N];
		for(int i = 0; i < N; i++) {
			data[i] = 0.0;
		}
	}	
	
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		data = new Double[0];
	}


	@Override
	public void changeSize(int N) {
		Double[] buf = new Double[N];
		for(int i = 0; i < N; i++) {
			if(i < data.length) buf[i] = data[i];
			else buf[i] = 0.0;
		}
		data = buf;
	}
	
	@Override
	public RealSeq reverse() {
		RealSeq out = new RealSeq(size());
		for(int i = 0; i < data.length; i++)
	    {
	        out.data[i] = data[data.length-1-i];
	    }
		return out;
	}
	
	@Override
	public RealSeq add(Double a) {
		RealSeq out = new RealSeq(this);
		for(int i = 0; i < size(); i++) {
			out.data[i] += a;
		}
		return out;
	}
	
	@Override
	public RealSeq multiple(Double a) {
		RealSeq out = new RealSeq(this);
		for(int i = 0; i < size(); i++) {
			out.data[i] *= a;
		}
		return out;
	}	
	
	
	@Override
	public RealSeq abs() {
		RealSeq out = new RealSeq(this);
		for(int i = 0; i < size(); i++) {
			out.data[i] = Math.abs(out.data[i]);
		}
		return out;
	}

	@Override
	public Double sum() {
		// TODO Auto-generated method stub
		Double sum = 0.0;
		for(Double ele : data) {
			sum += ele;
		}
		return sum;
	}
	
	
	@Override
	public ComplexSeq dtft(RealSeq omega) {
		// TODO Auto-generated method stub
		ComplexSeq out = new ComplexSeq(this).dtft(omega);
		return out;
	}
	
	@Override
	public ComplexSeq dtft(int N) {
		// TODO Auto-generated method stub
		ComplexSeq out = dtft(SeqFactory.linSpace(0, Math.PI, N));
		return out;
	}

	public double max() {
		return Collections.max(Arrays.asList(data));
	}
	
	public double min() {
		return Collections.min(Arrays.asList(data));
	}
	
}
