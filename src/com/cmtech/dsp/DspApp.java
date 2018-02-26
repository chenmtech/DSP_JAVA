package com.cmtech.dsp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cmtech.dsp.bmefile.BmeFile;
import com.cmtech.dsp.exception.DspException;
import com.cmtech.dsp.seq.Complex;
import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.ISeq;
import com.cmtech.dsp.seq.RealSeq;
import com.cmtech.dsp.seq.RealSeqBaseOperator;
import com.cmtech.dsp.seq.SeqFactory;
import com.cmtech.dsp.seq.SeqUtil;

public class DspApp {

	public static void main(String[] args) throws DspException{
		BmeFile.setFileDirectory("/Users/bme/Documents/matlab");
		
		//ComplexSeq1 seq = new ComplexSeq1(10);
		//System.out.println(seq);
		
		/*Complex c1 = new Complex(1.1,1.1);
		Complex c2 = new Complex(2.2,2.2);
		Complex c3 = new Complex(3.3,3.3);
		List<Complex> lst = new ArrayList<>();
		lst.add(c1);
		lst.add(c2);
		lst.add(c3);
		ComplexSeq seq = new ComplexSeq(lst);
		ComplexSeq seq1 = (ComplexSeq)seq.reverse();
		System.out.println(seq);
		System.out.println(seq1);
		seq.get(0).set(2.0, 2.0);
		System.out.println(seq);
		System.out.println(seq1);*/
		
		//ComplexSeq ejw = SeqFactory.createEJWSeq(Math.PI/10, 0, 10);
		//System.out.println(ejw.angle());
		
		RealSeq re1 = SeqFactory.createRandomSeq(3);
		RealSeq im1 = SeqFactory.createRandomSeq(3);
		ComplexSeq seq1 = new ComplexSeq(re1, im1);
		RealSeq re2 = SeqFactory.createRandomSeq(4);
		RealSeq im2 = SeqFactory.createRandomSeq(4);
		ComplexSeq seq2 = new ComplexSeq(re2, im2);
		System.out.println(seq1);
		System.out.println(seq2);
		ISeq result = SeqUtil.conv(re1, re2);
		System.out.println(result);
		ComplexSeq fft = re1.dtft(11);
		System.out.println(fft);
		ISeq result1 = SeqUtil.convUsingDFT(re1, re2).getReal();
		System.out.println(result1);
	}
	
	private static Double newElement(Double a) {
		return a;
	}

}
