package com.cmtech.dsp.filter.para;

import com.cmtech.dsp.filter.design.AnalogFilterType;
import com.cmtech.dsp.filter.design.FilterType;

public class AnalogFilterPara extends FilterPara {
	protected AnalogFilterType afType = AnalogFilterType.NONE;
	
	public AnalogFilterPara() {
		
	}
	
	public AnalogFilterPara(double[] wp, double[] ws, double Rp, double As, FilterType fType, AnalogFilterType afType) {
		super(wp, ws, Rp, As, fType);
		this.afType = afType;
	}
	
	public AnalogFilterType getAnalogFilterType() {
		return afType;
	}
	
	@Override
	public String toString() {
		return super.toString() + ',' + "AnalogFilterType=" + afType; 
	}
	
	
}
