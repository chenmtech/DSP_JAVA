package com.cmtech.dsp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import com.cmtech.dsp.exception.DspException;
import com.cmtech.dsp.file.BmeFile;
import com.cmtech.dsp.filter.FIRFilter;
import com.cmtech.dsp.filter.IIRFilter;
import com.cmtech.dsp.filter.design.AFType;
import com.cmtech.dsp.filter.design.FIRDesigner;
import com.cmtech.dsp.filter.design.FIRSpec;
import com.cmtech.dsp.filter.design.FilterType;
import com.cmtech.dsp.filter.design.IIRDesigner;
import com.cmtech.dsp.filter.design.IIRSpec;
import com.cmtech.dsp.filter.design.WinType;
import com.cmtech.dsp.seq.Complex;
import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.FFT;
import com.cmtech.dsp.seq.RealSeq;
import com.cmtech.dsp.seq.SeqFactory;

public class DspApp {

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
		System.out.println(iir.freq(101).dB());
		
		RealSeq seq1 = SeqFactory.createRndSeq(10);
		RealSeq seq2 = SeqFactory.createRndSeq(10);
		ComplexSeq seq3 = new ComplexSeq(seq1, seq2);
		System.out.println(seq3);
		//seq1.changeSize(8);
		//ComplexSeq fft = seq3.fft();
		ComplexSeq fft = seq3.fft();
		seq3 = fft.ifft();
		System.out.println(seq3);

		ComplexSeq seq4 = new ComplexSeq(seq3);
		System.out.println(seq3.hashCode());
		System.out.println(seq4.hashCode());
		System.out.println(seq3.equals(seq1));
		
		System.out.println(BmeFile.configFileDirectory("/Users/bme/Documents"));
	}
	
	private static double[] changeSize(double[] a) {
		return a;
	}

}
