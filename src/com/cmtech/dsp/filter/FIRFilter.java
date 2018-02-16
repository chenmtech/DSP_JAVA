package com.cmtech.dsp.filter;

import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.RealSeq;

public class FIRFilter extends DigitalFilter{
	
	public FIRFilter(RealSeq hseq){
		super(hseq, new RealSeq(1.0));
	}
	
	public FIRFilter(RealSeq hseq, double a){
		super(hseq.multiple(1/a), new RealSeq(1.0));
	}

	public RealSeq getHn() {
		return getB();
	}

	@Override
	public void setA(RealSeq a) {
		return;
	}

	@Override
	public ComplexSeq freq(int N) {
		return new RealSeq(b).dtft(N);
	}

	@Override
	public ComplexSeq freq(RealSeq omega) {
		return new RealSeq(b).dtft(omega);
	}

}
