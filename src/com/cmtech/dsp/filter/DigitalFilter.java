package com.cmtech.dsp.filter;

import com.cmtech.dsp.filter.structure.IDFStructure;
import com.cmtech.dsp.seq.RealSeq;

public abstract class DigitalFilter extends AbstractFilter implements IDigitalFilter{
	IDFStructure structure;

	public DigitalFilter(RealSeq bseq, RealSeq aseq){
		super(bseq, aseq);
	}

	public double filter(double x) {
		return structure.filter(x);
	}
}
