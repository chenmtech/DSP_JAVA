package com.cmtech.dsp;

import java.util.Arrays;

import com.cmtech.dsp.bmefile.BmeFile;
import com.cmtech.dsp.exception.DspException;
import com.cmtech.dsp.filter.AnalogFilter;
import com.cmtech.dsp.filter.FIRFilter;
import com.cmtech.dsp.filter.IIRFilter;
import com.cmtech.dsp.filter.design.AFType;
import com.cmtech.dsp.filter.design.ALPDesigner;
import com.cmtech.dsp.filter.design.FIRDesigner;
import com.cmtech.dsp.filter.design.FilterType;
import com.cmtech.dsp.filter.design.IIRDesigner;
import com.cmtech.dsp.filter.design.WinType;
import com.cmtech.dsp.filter.structure.FIRDFStructure;
import com.cmtech.dsp.filter.structure.FIRLPF1Structure;
import com.cmtech.dsp.filter.structure.FIRLPFStructure;
import com.cmtech.dsp.seq.RealSeq;

public class DspApp {

	public static void main(String[] args) throws DspException{
		BmeFile.setFileDirectory("/Users/bme/Documents/matlab");
		
		double[] wp = {0.4*Math.PI};
		double[] ws = {0.3*Math.PI};
		double Rp = 1;
		double As = 40;
		FilterType fType = FilterType.HIGHPASS;
		WinType wType = WinType.KAISER;
		FIRFilter fir = FIRDesigner.design(wp,ws,Rp,As,fType,wType);
		System.out.println(fir);
		System.out.println(fir.freq(101).abs());
		System.out.println(fir.whichType());
		FIRDFStructure s = new FIRDFStructure(fir);
		double[] out = new double[1000];
		for(int i =0 ; i < 1000; i++) {
			out[i] = s.filter(1.0);
		}
		System.out.println(Arrays.toString(out));
		
		FIRLPF1Structure s1 = new FIRLPF1Structure(fir);
		for(int i =0 ; i < 1000; i++) {
			out[i] = s1.filter(1.0);
		}
		System.out.println(Arrays.toString(out));
		
		//FIRLPFStructure.Factory fac = new FIRLPFStructure.Factory();
		

		AnalogFilter alp = ALPDesigner.design(10.0,20.0,1,30,AFType.CHEB2);
		System.out.println(alp);
		RealSeq freq = alp.freq(30.0, 31).abs();
		System.out.println(freq);		
		//BmeFile.createBmeFile("alp.bme").writeData(freq.toArray()).close();
		
		AFType afType = AFType.ELLIP;
		IIRFilter iir = IIRDesigner.design(wp,ws,Rp,As,afType,fType);
		System.out.println(iir);
		freq = iir.freq(101).abs();
		System.out.println(freq);
		//BmeFile.createBmeFile("iir.bme").writeData(freq.toArray()).close();
		//BmeFile.createBmeFile("omega.bme").writeData(SeqFactory.linSpace(0, 1, 101).toArray()).close();
		




		
	}
	
	private static double[] changeSize(double[] a) {
		return a;
	}

}
