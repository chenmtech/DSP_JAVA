package com.cmtech.dsp_java.filter.design;

public abstract class DFSpec extends Spec {
	public DFSpec(double[] wp, double[] ws, double Rp, double As, FilterType fType) {
		super(wp, ws, Rp, As, fType);
	}

}
