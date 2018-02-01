package com.cmtech.dsp_java.filter;

import com.cmtech.dsp_java.seq.ComplexSeq;
import com.cmtech.dsp_java.seq.RealSeq;

public interface IFilter {
	RealSeq getB();
	RealSeq getA();
	void setB(RealSeq b);
	void setA(RealSeq a);
	ComplexSeq freq(RealSeq omega);
}
