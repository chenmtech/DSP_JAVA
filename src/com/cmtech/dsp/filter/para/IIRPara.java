package com.cmtech.dsp.filter.para;

import com.cmtech.dsp.filter.design.AnalogFilterType;
import com.cmtech.dsp.filter.design.FilterType;

public class IIRPara extends DigitalFilterPara {
	private AnalogFilterType afType = AnalogFilterType.NONE;
	
	public IIRPara() {
		
	}
	
	public IIRPara(double[] wp, double[] ws, double Rp, double As, AnalogFilterType afType, FilterType fType) {
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
