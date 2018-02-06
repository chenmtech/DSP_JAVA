package com.cmtech.dsp;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
		System.out.println(seq1);
		//seq1.changeSize(8);
		ComplexSeq fft = seq1.fft();
		System.out.println(fft);
		ComplexSeq dtft = seq1.dtft(9);
		System.out.println(dtft);
		
		//Complex cpl = new Complex(1.1,22.2);
		try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("test.txt"));
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("test.txt")))
		{
			oos.writeObject(fft);
			ComplexSeq fftread = (ComplexSeq)ois.readObject();
			System.out.println(fftread);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private static double[] changeSize(double[] a) {
		return a;
	}

}