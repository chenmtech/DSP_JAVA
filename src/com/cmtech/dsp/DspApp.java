package com.cmtech.dsp;

import java.util.Arrays;
import java.util.List;

import com.cmtech.dsp.bmefile.BmeFile;
import com.cmtech.dsp.exception.DspException;
import com.cmtech.dsp.seq.Complex;
import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.RealSeq;
import com.cmtech.dsp.seq.SeqFactory;
import com.cmtech.dsp.seq.SeqUtil;

public class DspApp {

	public static void main(String[] args) throws DspException{
		BmeFile.setFileDirectory("/Users/bme/Documents/matlab");
		
		RealSeq re1 = SeqFactory.createRandomSeq(4);
		RealSeq im1 = SeqFactory.createRandomSeq(4);
		ComplexSeq seq1 = new ComplexSeq(re1, im1);
		//System.out.println(seq1);
		RealSeq re2 = SeqFactory.createRandomSeq(6);
		RealSeq im2 = SeqFactory.createRandomSeq(6);
		ComplexSeq seq2 = new ComplexSeq(re2, im2);
		ComplexSeq seq3 = SeqUtil.add(seq1, seq2);
		//seq3.set(1, new Complex(1.0,1.0));
		ComplexSeq seq4 = SeqUtil.multiple(seq1, seq2);
		System.out.println(seq1);
		System.out.println(seq2);
		System.out.println(seq3);
		System.out.println(seq4);
		
		
	}
	
	private static double[] changeSize(double[] a) {
		return a;
	}

}
