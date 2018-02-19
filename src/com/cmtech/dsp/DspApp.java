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
		
		RealSeq realZero = SeqFactory.createZeroSeq(10, RealSeq.class);
		ComplexSeq complexZero = SeqFactory.createZeroSeq(10, ComplexSeq.class);
		System.out.println(realZero);
		System.out.println(complexZero);
		
		RealSeq sinSeq = SeqFactory.createSinSeq(1.5, Math.PI/5, 0, 100);
		BmeFile.createBmeFile("sinseq.bme").writeData(sinSeq.toArray()).close();

		ComplexSeq ejw = SeqFactory.createEJWSeq(Math.PI/10, 0, 10);
		System.out.println(ejw);
		System.out.println(ejw.abs());
		
		RealSeq rnd = SeqFactory.createRandomSeq(10);
		System.out.println(rnd);
		
		RealSeq lin = SeqFactory.linSpace(1.1, 9.9, 9);
		System.out.println(lin);
	}
	
	private static double[] changeSize(double[] a) {
		return a;
	}

}
