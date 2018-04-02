package com.cmtech.dsp.filter;

import com.cmtech.dsp.filter.structure.IDFStructure;
import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.RealSeq;
import com.cmtech.dsp.util.SeqUtil;

public abstract class DigitalFilter extends AbstractFilter implements IDigitalFilter{
	protected IDFStructure structure;
	
	public DigitalFilter(double[] b, double[] a) {
		super(b, a);
	}

	public DigitalFilter(RealSeq bseq, RealSeq aseq){
		super(bseq, aseq);
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
			return super.toString() + '\n' + "No Structure";
		else
			return super.toString() + '\n' + structure.toString();
	}
	
	@Override
	public ComplexSeq freq(int N) {
		return freq(SeqUtil.linSpace(0, Math.PI, N));
	}
	
	@Override
	public RealSeq mag(int N) {
		return mag(SeqUtil.linSpace(0, Math.PI, N));
	}
	
	@Override
	public RealSeq pha(int N) {
		return pha(SeqUtil.linSpace(0, Math.PI, N));
	}
}
