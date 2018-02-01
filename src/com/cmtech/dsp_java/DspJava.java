package com.cmtech.dsp_java;

import com.cmtech.dsp_java.filter.FIRFilter;
import com.cmtech.dsp_java.filter.IIRFilter;
import com.cmtech.dsp_java.filter.design.AFType;
import com.cmtech.dsp_java.filter.design.FIRDesigner;
import com.cmtech.dsp_java.filter.design.FIRSpec;
import com.cmtech.dsp_java.filter.design.FilterType;
import com.cmtech.dsp_java.filter.design.IIRDesigner;
import com.cmtech.dsp_java.filter.design.IIRSpec;
import com.cmtech.dsp_java.filter.design.WinType;
import com.cmtech.dsp_java.seq.RealSeq;

public class DspJava {

	public static void main(String[] args) {

		double[] ws = {0.4*Math.PI, 0.5*Math.PI};
		double[] wp = {0.3*Math.PI, 0.6*Math.PI};
		double Rp = 1;
		double As = 20;
		FilterType fType = FilterType.BANDSTOP;
		
		FIRSpec firSpec = new FIRSpec(wp,ws,Rp,As,fType);
		firSpec.setWinType(WinType.KAISER);
		FIRFilter fir = FIRDesigner.design(firSpec);
		System.out.println(fir.getHn());
		
		System.out.println(fir.freq(101).abs());
		
		AFType afType = AFType.BUTT;
		IIRSpec iirSpec = new IIRSpec(wp,ws,Rp,As,fType,afType);
		IIRFilter iir = IIRDesigner.design(iirSpec);
		System.out.println(iir.getB());
		System.out.println(iir.getA());
		System.out.println(iir.freq(101).abs());
		
		RealSeq tmp = new RealSeq(1.0);
		System.out.println(tmp);
		
	}

}
