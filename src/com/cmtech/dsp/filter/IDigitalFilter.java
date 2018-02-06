package com.cmtech.dsp.filter;

import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.RealSeq;

public interface IDigitalFilter extends IFilter {
	ComplexSeq freq(int N);
}
