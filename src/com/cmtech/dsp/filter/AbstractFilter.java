package com.cmtech.dsp.filter;
/*
Copyright (c) 2008 chenm
*/
import java.util.Arrays;

import com.cmtech.dsp.filter.para.FilterPara;
import com.cmtech.dsp.seq.RealSeq;


public abstract class AbstractFilter implements IFilter {
	protected Double[] b;
	protected Double[] a;
	protected FilterPara para;
	
	public AbstractFilter() {
	}
	
	public AbstractFilter(Double[] b, Double[] a) {
		this.b = Arrays.copyOf(b, b.length);
		this.a = Arrays.copyOf(a, a.length);
	}

	@Override
	public Double[] getB() {
		return b;
	}

	@Override
	public Double[] getA() {
		return a;
	}

	@Override
	public void setB(Double[] b) {
		if(b != null) this.b = Arrays.copyOf(b, b.length);
		else this.b = null;
	}

	@Override
	public void setA(Double[] a) {
		if(a != null) this.a = Arrays.copyOf(a, a.length);
		else this.a = null;
	}

	public FilterPara getFilterPara() {
		return para;
	}

	public AbstractFilter setFilterPara(FilterPara para) {
		this.para = para;
		return this;
	}
	
	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		String str = getClass().getSimpleName() + ": b=" + Arrays.toString(b) + ",a=" + Arrays.toString(a) + '\n';
		strBuilder.append(str);
		if(para != null) strBuilder.append(para.toString());
		else strBuilder.append("FilterPara: No Para");
		
		return strBuilder.toString();
	}
	
	@Override
	public RealSeq magResponse(RealSeq omega) {
		return freqResponse(omega).abs();
	}
	
	@Override
	public RealSeq phaResponse(RealSeq omega) {
		return freqResponse(omega).angle();
	}
	
}
