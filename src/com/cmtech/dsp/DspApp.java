package com.cmtech.dsp;

import static java.lang.Math.PI;

import com.cmtech.dsp.bmefile.BmeFile;
import com.cmtech.dsp.exception.DspException;
import com.cmtech.dsp.filter.IIRFilter;
import com.cmtech.dsp.filter.design.AFType;
import com.cmtech.dsp.filter.design.FilterType;
import com.cmtech.dsp.filter.design.IIRDesigner;
import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.FFT;
import com.cmtech.dsp.seq.RealSeq;
import com.cmtech.dsp.seq.SeqFactory;

public class DspApp {

	public static void main(String[] args) throws DspException{
		BmeFile.setFileDirectory("/Users/bme/Documents/matlab");
		
		double[] wp = {0.4*PI, 0.5*PI};
		double[] ws = {0.3*PI, 0.6*PI};
		double Rp = 0.3;
		double As = 40;
		FilterType fType = FilterType.BANDPASS;
		AFType afType = AFType.ELLIP;
		IIRFilter filter = IIRDesigner.design(wp, ws, Rp, As, afType, fType);
		RealSeq mag = filter.mag(1001);
		RealSeq pha = filter.pha(1001);
		RealSeq omega = SeqFactory.linSpace(0, PI, 1001);
		BmeFile.createBmeFile("mag.bme").writeData(mag.toArray()).close();
		BmeFile.createBmeFile("pha.bme").writeData(pha.toArray()).close();
		BmeFile.createBmeFile("omega.bme").writeData(omega.toArray()).close();
		
		RealSeq re = SeqFactory.createRandomSeq(5);
		RealSeq im = SeqFactory.createRandomSeq(5);
		ComplexSeq seq = new ComplexSeq(re,im);
		ComplexSeq fft = FFT.fft(seq);
		ComplexSeq ifft = FFT.ifft(fft);
		System.out.println(seq);
		System.out.println(ifft);
	}
	
	private static double[] changeSize(double[] a) {
		return a;
	}

}
