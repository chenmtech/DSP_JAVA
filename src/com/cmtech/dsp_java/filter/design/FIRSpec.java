package com.cmtech.dsp_java.filter.design;

public class FIRSpec extends DFSpec {
	
	protected int N = 0;
	protected double[] wc = {0.0,0.0};
	protected WinType wType = WinType.UNKNOWN;

	public FIRSpec(double[] wp, double[] ws, double Rp, double As, FilterType fType) {
		super(wp, ws, Rp, As, fType);
	}
	
	public WinType getWinType() {
		return wType;
	}

	public void setWinType(WinType winType) {
		this.wType = winType;
	}

	public int getN() {
		return N;
	}

	public void setN(int n) {
		N = n;
	}

	public double[] getWc() {
		return wc;
	}

	public void setWc(double[] wc) {
		this.wc = wc;
	}
	
	

}
