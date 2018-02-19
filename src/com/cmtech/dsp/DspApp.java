package com.cmtech.dsp;

import java.util.Arrays;
import java.util.List;

import com.cmtech.dsp.bmefile.BmeFile;
import com.cmtech.dsp.exception.DspException;
import com.cmtech.dsp.seq.Complex;
import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.RealSeq;
import com.cmtech.dsp.seq.SeqFactory;

public class DspApp {

	public static void main(String[] args) throws DspException{
		BmeFile.setFileDirectory("/Users/bme/Documents/matlab");
		

		RealSeq re = SeqFactory.createRndSeq(4);
		RealSeq im = SeqFactory.createRndSeq(4);
		ComplexSeq cseq = new ComplexSeq(re, im);
		Complex[] carr = cseq.toArray();
		List<Complex> lst = Arrays.asList(carr);
		System.out.println(lst);
		ComplexSeq cseq1 = new ComplexSeq(lst);
		System.out.println(cseq1);
		cseq1.set(0, new Complex(1.0,1.0));		
		System.out.println(cseq1);

	}
	
	private static double[] changeSize(double[] a) {
		return a;
	}

}
