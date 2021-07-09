package com.cmtech.dsp.filter;

import com.cmtech.dsp.filter.structure.DigitalFilterStructType;
import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.RealSeq;

public interface IDigitalFilter {
	ComplexSeq freqResponse(int N);
	RealSeq magResponse(int N);
	RealSeq phaResponse(int N);
	void createStructure(DigitalFilterStructType sType);
	double filter(double x);
	RealSeq filter(RealSeq seq);
}
