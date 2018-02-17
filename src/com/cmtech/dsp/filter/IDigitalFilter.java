package com.cmtech.dsp.filter;

import com.cmtech.dsp.seq.ComplexSeq;

public interface IDigitalFilter extends IFilter {
	ComplexSeq freq(int N);
}
