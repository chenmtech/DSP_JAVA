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
		
		RealSeq seq1 = SeqFactory.createRandomSeq(4);
		RealSeq seq2 = SeqFactory.createRandomSeq(2);
		RealSeq seq3 = SeqUtil.conv(seq1, seq2);
		RealSeq seq4 = SeqUtil.convUsingDFT(seq1, seq2);
		System.out.println(seq1);
		System.out.println(seq2);
		System.out.println(seq3);
		System.out.println(seq4);
		
		double[] arr1 = seq1.toArray();
		arr1 = Arrays.copyOf(arr1, 5);
		System.out.println(Arrays.toString(arr1));
	}
	
	private static double[] changeSize(double[] a) {
		return a;
	}

}
