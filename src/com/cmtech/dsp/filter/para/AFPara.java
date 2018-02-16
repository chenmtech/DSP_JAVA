package com.cmtech.dsp.filter.para;

import com.cmtech.dsp.filter.design.AFType;
import com.cmtech.dsp.filter.design.FilterType;

public class AFPara extends FilterPara {
	protected AFType afType = AFType.NONE;
	
	public AFPara() {
		
	}
	
	public AFPara(double[] wp, double[] ws, double Rp, double As, FilterType fType, AFType afType) {
		super(wp, ws, Rp, As, fType);
		this.afType = afType;
	}
	
	public AFType getAFType() {
		return afType;
	}
	
	@Override
	public String toString() {
		return super.toString() + ',' + "AFType=" + afType; 
	}
	
	
}
