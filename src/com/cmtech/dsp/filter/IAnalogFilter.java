package com.cmtech.dsp.filter;

import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.RealSeq;

public interface IAnalogFilter extends IFilter {
	ComplexSeq freq(double Qmax, int N);
	RealSeq mag(double Qmax, int N);
	RealSeq pha(double Qmax, int N);
}
