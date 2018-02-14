package com.cmtech.dsp.filter.para;

import com.cmtech.dsp.filter.design.FilterType;

public abstract class DFPara extends FilterPara {
	public DFPara() {
		
	}
	
	public DFPara(double[] wp, double[] ws, double Rp, double As, FilterType fType) {
		super(wp, ws, Rp, As, fType);
	}

}
