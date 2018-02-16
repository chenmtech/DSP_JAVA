package com.cmtech.dsp.filter.para;

import java.util.Arrays;

import com.cmtech.dsp.filter.design.FilterType;

public abstract class FilterPara {
	private double[] wp;
	private double[] ws;
	private double Rp;
	private double As;
	private FilterType fType;
	
	public FilterPara() {
		
	}
	
	public FilterPara(double[] wp, double[] ws, double Rp, double As, FilterType fType) {
		this.wp = Arrays.copyOf(wp, wp.length);
		this.ws = Arrays.copyOf(ws, ws.length);
		this.Rp = Rp;
		this.As = As;
		this.fType = fType;
	}
	
	public double[] getWp() {
		return Arrays.copyOf(wp, wp.length);
	}
	public void setWp(double[] wp) {
		this.wp = Arrays.copyOf(wp, wp.length);
	}
	public double[] getWs() {
		return Arrays.copyOf(ws, ws.length);
	}
	public void setWs(double[] ws) {
		this.ws = Arrays.copyOf(ws, ws.length);
	}
	public double getRp() {
		return Rp;
	}
	public void setRp(double rp) {
		Rp = rp;
	}
	public double getAs() {
		return As;
	}
	public void setAs(double as) {
		As = as;
	}
	public FilterType getType() {
		return fType;
	}
	public void setType(FilterType type) {
		this.fType = type;
	}
	
	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append(getClass().getSimpleName() + ':');
		if(fType == FilterType.BANDPASS || fType == FilterType.BANDSTOP) {
			strBuilder.append("wp=" + Arrays.toString(wp) + ',');
			strBuilder.append("ws=" + Arrays.toString(ws) + ',');
		} else {
			strBuilder.append("wp=" + wp[0] + ',');
			strBuilder.append("ws=" + ws[0] + ',');
		}
		strBuilder.append("Rp=" + Rp + ',');
		strBuilder.append("As=" + As + ',');
		strBuilder.append("FilterType=" + fType);
		return strBuilder.toString();
	}
	
	
}
