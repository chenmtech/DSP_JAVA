package com.cmtech.dsp.filter.design;

public class IIRSpec extends DFSpec {
	protected AFType afType = AFType.NONE;
	
	public IIRSpec(double[] wp, double[] ws, double Rp, double As, FilterType fType, AFType afType) {
		super(wp, ws, Rp, As, fType);
		this.afType = afType;
	}
}
