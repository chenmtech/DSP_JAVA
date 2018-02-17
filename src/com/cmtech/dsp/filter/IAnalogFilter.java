package com.cmtech.dsp.filter;

import com.cmtech.dsp.seq.ComplexSeq;

public interface IAnalogFilter extends IFilter {
	ComplexSeq freq(double Qmax, int N);
}
