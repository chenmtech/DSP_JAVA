package com.cmtech.dsp.filter.para;

import com.cmtech.dsp.filter.design.FilterType;

public abstract class DigitalFilterPara extends FilterPara {
	public DigitalFilterPara() {
		
	}
	
	public DigitalFilterPara(double[] wp, double[] ws, double Rp, double As, FilterType fType) {
		super(wp, ws, Rp, As, fType);
	}

}
