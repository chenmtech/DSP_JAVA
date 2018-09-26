package com.cmtech.dsp;

import static java.lang.Math.PI;

import com.cmtech.dsp.bmefile.BmeFile;
import com.cmtech.dsp.exception.DspException;
import com.cmtech.dsp.filter.IDigitalFilter;
import com.cmtech.dsp.filter.IIRFilter;
import com.cmtech.dsp.filter.design.AFType;
import com.cmtech.dsp.filter.design.FilterType;
import com.cmtech.dsp.filter.design.IIRDesigner;
import com.cmtech.dsp.filter.design.NotchDesigner;
import com.cmtech.dsp.filter.structure.StructType;
import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.RealSeq;
import com.cmtech.dsp.util.SeqUtil;

public class DspApp {

	public static void main(String[] args) throws DspException{
		BmeFile.setFileDirectory("/Users/bme/Documents/matlab");
		//BmeFile.setFileDirectory("e://matlabcode");
		
		RealSeq seq1 = SeqUtil.createSinSeq(1.0, 0.2*PI, 0, 101);
		RealSeq seq2 = SeqUtil.createSinSeq(1.0, 0.7*PI, 0, 101);
		RealSeq before = (RealSeq) SeqUtil.add(seq1, seq2);
		double[] ws = {0.4*Math.PI};
		double[] wp = {0.5*Math.PI};
		double Rp = 0.3;
		double As = 40;
		AFType afType = AFType.BUTT;
		FilterType fType = FilterType.HIGHPASS;
		IDigitalFilter filter = IIRDesigner.design(wp, ws, Rp, As, afType, fType);
		RealSeq after = filter.filter(before);
		before.saveAsBmeFile("before1.bme");
		after.saveAsBmeFile("after1.bme");
		
		ComplexSeq fft = seq1.fft();
		System.out.println(fft.abs());
		ComplexSeq fft1 = new ComplexSeq(seq1,seq2).fft();
		System.out.println(fft1);
		//BmeFile.createBmeFile("fft.bme").writeData(fft.abs().toArray()).close();
		
		RealSeq sinSeq1 = SeqUtil.createSinSeq(1, 0.2*PI, 0, 500);
		RealSeq sinSeq2 = SeqUtil.createSinSeq(1, 0.3*PI, 0, 500);
		RealSeq sinSeq3 = SeqUtil.createSinSeq(1, 0.4*PI, 0, 500);
		before = (RealSeq) SeqUtil.add(sinSeq1, sinSeq2);
		before = (RealSeq) SeqUtil.add(before, sinSeq3);
		System.out.println(before);
		
		IIRFilter notch = NotchDesigner.design(0.3*PI, 0.01*PI);
		RealSeq after1 = notch.filter(before);
		
		notch.createStructure(StructType.IIR_NOTCH);
		
		after = notch.filter(before);
		System.out.println(after1);
		System.out.println(after);
		
		//before.saveAsBmeFile("before.bme");
		//after.saveAsBmeFile("after.bme");

		
	}
	
	

}
