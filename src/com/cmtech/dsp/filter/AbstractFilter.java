package com.cmtech.dsp.filter;

import com.cmtech.dsp.filter.para.FilterPara;
import com.cmtech.dsp.seq.RealSeq;

public abstract class AbstractFilter implements IFilter {
	protected double[] b;
	protected double[] a;
	protected FilterPara para;
	
	public AbstractFilter() {
	}
	
	public AbstractFilter(RealSeq bseq, RealSeq aseq){
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
		if(b != null) this.b = b.toArray();
		else this.b = null;
	}

	@Override
	public void setA(RealSeq a) {
		if(a != null) this.a = a.toArray();
		else this.a = null;
	}

	public FilterPara getFilterPara() {
		return para;
	}

	public void setFilterPara(FilterPara para) {
		this.para = para;
	}
	
	
	
}
