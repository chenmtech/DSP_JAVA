package com.cmtech.dsp_java.filter;

import com.cmtech.dsp_java.filter.design.Spec;
import com.cmtech.dsp_java.seq.RealSeq;

public abstract class AbstractFilter implements IFilter {
	protected double[] b;
	protected double[] a;
	protected Spec spec;
	
	public AbstractFilter() {
	}
	
	public AbstractFilter(RealSeq bseq, RealSeq aseq) {
		if(bseq != null) b = bseq.toArray();
		if(aseq != null) a = aseq.toArray();
	}

	
	@Override
	public RealSeq getB() {
		return new RealSeq(b);
	}

	@Override
	public RealSeq getA() {
		return new RealSeq(a);
	}

	@Override
	public void setB(RealSeq b) {
		this.b = b.toArray();
	}

	@Override
	public void setA(RealSeq a) {
		this.a = a.toArray();
	}

	public Spec getSpec() {
		return spec;
	}

	public void setSpec(Spec spec) {
		this.spec = spec;
	}
	
	
	
}
