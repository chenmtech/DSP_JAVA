package com.cmtech.dsp.filter;

import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.RealSeq;

public class FIRFilter extends DigitalFilter{
	
	public FIRFilter(RealSeq hseq) {
		super(hseq, null);
	}
	
	public FIRFilter(RealSeq hseq, double a) {
		super(hseq.multiple(1/a), null);
	}

	public RealSeq getHn() {
		return getB();
	}

	@Override
	public RealSeq getA() {
		return new RealSeq(1.0);
	}

	@Override
	public void setA(RealSeq a) {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public ComplexSeq freq(int N) {
		// TODO Auto-generated method stub
		return new RealSeq(b).dtft(N);
	}

	@Override
	public ComplexSeq freq(RealSeq omega) {
		// TODO Auto-generated method stub
		return new RealSeq(b).dtft(omega);
	}

}
