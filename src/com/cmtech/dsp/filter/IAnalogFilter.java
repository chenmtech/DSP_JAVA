package com.cmtech.dsp.filter;

import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.RealSeq;

public interface IAnalogFilter {
	ComplexSeq freqResponse(double Qmax, int N);
	RealSeq magResponse(double Qmax, int N);
	RealSeq phaResponse(double Qmax, int N);
}
