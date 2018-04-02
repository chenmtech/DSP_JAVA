package com.cmtech.dsp;

import static java.lang.Math.PI;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import com.cmtech.dsp.bmefile.BmeFile;
import com.cmtech.dsp.exception.DspException;
import com.cmtech.dsp.filter.IDigitalFilter;
import com.cmtech.dsp.filter.IIRFilter;
import com.cmtech.dsp.filter.design.AFType;
import com.cmtech.dsp.filter.design.DCBlockDesigner;
import com.cmtech.dsp.filter.design.FilterType;
import com.cmtech.dsp.filter.design.IIRDesigner;
import com.cmtech.dsp.filter.structure.IIRDCBlockStructure;
import com.cmtech.dsp.filter.structure.StructType;
import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.RealSeq;
import com.cmtech.dsp.seq.Seq;
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
		
		IIRFilter dcFilter = DCBlockDesigner.design(1, 200);
		System.out.println(dcFilter);
		
		dcFilter.createStructure(StructType.IIR_DCBLOCK);
		RealSeq sinSeq = SeqUtil.createSinSeq(1, 0.1*PI, 0, 200);
		sinSeq = (RealSeq) sinSeq.plus(1.0);
		System.out.println(sinSeq);
		
		RealSeq outSeq = dcFilter.filter(sinSeq);
		System.out.println(outSeq);
		sinSeq.saveAsBmeFile("before.bme");
		outSeq.saveAsBmeFile("after.bme");
		
	}
	
	

}
