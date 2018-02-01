package com.cmtech.dsp_java.filter.design;

public class AFSpec extends Spec {
	protected AFType afType = AFType.NONE;
	
	public AFSpec(double[] wp, double[] ws, double Rp, double As, FilterType fType, AFType afType) {
		super(wp, ws, Rp, As, fType);
		this.afType = afType;
	}
	
	
}
