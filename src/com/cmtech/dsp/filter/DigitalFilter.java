package com.cmtech.dsp.filter;

import com.cmtech.dsp.seq.RealSeq;

public abstract class DigitalFilter extends AbstractFilter implements IDigitalFilter{

	public DigitalFilter(RealSeq bseq, RealSeq aseq){
		super(bseq, aseq);
	}


}
