package com.cmtech.dsp.filter.para;

import com.cmtech.dsp.filter.design.AnalogFilterType;
import com.cmtech.dsp.filter.design.FilterType;

public class AFPara extends FilterPara {
	protected AnalogFilterType afType = AnalogFilterType.NONE;
	
	public AFPara() {
		
	}
	
	public AFPara(double[] wp, double[] ws, double Rp, double As, FilterType fType, AnalogFilterType afType) {
		super(wp, ws, Rp, As, fType);
		this.afType = afType;
	}
	
	public AnalogFilterType getAFType() {
		return afType;
	}
	
	@Override
	public String toString() {
		return super.toString() + ',' + "AFType=" + afType; 
	}
	
	
}
