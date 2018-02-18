package com.cmtech.dsp;

import java.util.Arrays;

import com.cmtech.dsp.bmefile.BmeFile;
import com.cmtech.dsp.exception.DspException;
import com.cmtech.dsp.filter.AnalogFilter;
import com.cmtech.dsp.filter.FIRFilter;
import com.cmtech.dsp.filter.IIRFilter;
import com.cmtech.dsp.filter.design.AFDesigner;
import com.cmtech.dsp.filter.design.AFType;
import com.cmtech.dsp.filter.design.FIRDesigner;
import com.cmtech.dsp.filter.design.FilterType;
import com.cmtech.dsp.filter.design.IIRDesigner;
import com.cmtech.dsp.filter.design.WinType;
import com.cmtech.dsp.filter.structure.FSType;
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
		fir.createStructure(FSType.FIR_DF);
		System.out.println(fir);
		double[] out = new double[1000];
		for(int i =0 ; i < 1000; i++) {
			out[i] = fir.filter(1.0);
		}
		System.out.println(Arrays.toString(out));
		
		fir.createStructure(FSType.FIR_LPF);
		for(int i =0 ; i < 1000; i++) {
			out[i] = fir.filter(1.0);
		}
		System.out.println(Arrays.toString(out));
		
		double[] wp1 = new double[] {2*Math.PI*30, 2*Math.PI*40};
		double[] ws1 = new double[] {2*Math.PI*20, 2*Math.PI*50};
		AnalogFilter alp = AFDesigner.design(wp1,ws1,0.5,40,AFType.ELLIP,FilterType.BANDPASS);
		System.out.println(alp);
		RealSeq freq = alp.freq(2*Math.PI*70.0, 141).abs();
		System.out.println(freq);		
		BmeFile.createBmeFile("alp.bme").writeData(freq.toArray()).close();
		
		AFType afType = AFType.ELLIP;
		IIRFilter iir = IIRDesigner.design(wp,ws,Rp,As,afType,fType);
		//iir.createStructure(FSType.IIR_TDF2);
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
