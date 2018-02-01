package com.cmtech.dsp_java.filter;

import com.cmtech.dsp_java.seq.ComplexSeq;
import com.cmtech.dsp_java.seq.RealSeq;

public interface IAnalogFilter extends IFilter {
	ComplexSeq freq(double Qmax, int N);
}
