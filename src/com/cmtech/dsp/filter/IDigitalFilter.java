package com.cmtech.dsp.filter;

import com.cmtech.dsp.filter.structure.StructType;
import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.RealSeq;

public interface IDigitalFilter extends IFilter {
	ComplexSeq freq(int N);
	RealSeq mag(int N);
	RealSeq pha(int N);
	IDigitalFilter createStructure(StructType sType);
	double filter(double x);
	RealSeq filter(RealSeq seq);
}
