package com.cmtech.dsp_java.filter;

import com.cmtech.dsp_java.seq.RealSeq;

public abstract class DigitalFilter extends AbstractFilter implements IDigitalFilter{

	public DigitalFilter(RealSeq bseq, RealSeq aseq) {
		super(bseq, aseq);
	}


}
