package com.cmtech.dsp.filter;
/*
Copyright (c) 2008 chenm
*/
import com.cmtech.dsp.filter.structure.IDFStructure;
import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.RealSeq;
import com.cmtech.dsp.util.SeqUtil;

public abstract class AbstractDigitalFilter extends AbstractFilter implements IDigitalFilter{
	protected IDFStructure structure;
	
	public AbstractDigitalFilter(Double[] b, Double[] a) {
		super(b, a);
	}

	@Override
	public double filter(double x) {
		return structure.filter(x);
	}
	
	@Override
	public RealSeq filter(RealSeq seq) {
		RealSeq out = new RealSeq();
		for(int i = 0; i < seq.size(); i++) {
			out.append(filter(seq.get(i)));
		}
		return out;
	}
	

	public IDFStructure getStructure() {
		return structure;
	}

	
	@Override
	public String toString() {
		if(structure == null) 
			return super.toString() + "\nFilterStructure: " + "No Structure";
		else
			return super.toString() + "\nFilterStructure: " + structure.toString();
	}
	
	@Override
	public ComplexSeq freqResponse(int N) {
		return freqResponse(SeqUtil.linSpace(0, Math.PI, N));
	}
	
	@Override
	public RealSeq magResponse(int N) {
		return magResponse(SeqUtil.linSpace(0, Math.PI, N));
	}
	
	@Override
	public RealSeq phaResponse(int N) {
		return phaResponse(SeqUtil.linSpace(0, Math.PI, N));
	}
}
