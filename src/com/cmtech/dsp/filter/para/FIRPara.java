package com.cmtech.dsp.filter.para;

import java.util.Arrays;

import com.cmtech.dsp.filter.design.FilterType;
import com.cmtech.dsp.filter.design.WinType;

public class FIRPara extends DFPara {
	private int N = 0;	// h(n)的长度
	private double[] wc = {0.0,0.0};		// 理想滤波器的截止频率
	private WinType wType = WinType.UNKNOWN;		// 窗函数类型
	
	public FIRPara() {
		
	}

	public FIRPara(double[] wp, double[] ws, double Rp, double As, FilterType fType) {
		super(wp, ws, Rp, As, fType);
	}
	
	public FIRPara(double[] wp, double[] ws, 
				   double Rp, double As, FilterType fType,
				   int N, double[] wc, WinType wType) {
		super(wp, ws, Rp, As, fType);
		this.N = N;
		this.wc = Arrays.copyOf(wc, wc.length);
		this.wType = wType;
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
	
	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder(super.toString());
		strBuilder.append(',' + "N=" + N);
		if(getType() == FilterType.BANDPASS || getType() == FilterType.BANDSTOP) 
			strBuilder.append(',' + "wc=" + Arrays.toString(wc));
		else
			strBuilder.append(',' + "wc=" + wc[0]);
		strBuilder.append(',' + "WinType=" + wType);
		return strBuilder.toString();
	}

}
