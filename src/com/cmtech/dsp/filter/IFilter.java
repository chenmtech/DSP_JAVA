package com.cmtech.dsp.filter;

import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.RealSeq;

public interface IFilter {
	RealSeq getB();
	RealSeq getA();
	void setB(RealSeq b);
	void setA(RealSeq a);
	ComplexSeq freq(RealSeq omega);
	RealSeq mag(RealSeq omega);
	RealSeq pha(RealSeq omega);
}
