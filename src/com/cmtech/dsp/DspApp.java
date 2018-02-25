package com.cmtech.dsp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.cmtech.dsp.bmefile.BmeFile;
import com.cmtech.dsp.exception.DspException;
import com.cmtech.dsp.newseq.ComplexSeq1;
import com.cmtech.dsp.newseq.RealSeqEleOperator;
import com.cmtech.dsp.seq.Complex;

public class DspApp {

	public static void main(String[] args) throws DspException{
		BmeFile.setFileDirectory("/Users/bme/Documents/matlab");
		
		//ComplexSeq1 seq = new ComplexSeq1(10);
		//System.out.println(seq);
		
		Complex c1 = new Complex(1.1,1.1);
		Complex c2 = new Complex(2.2,2.2);
		Complex c3 = new Complex(3.3,3.3);
		List<Complex> lst = new ArrayList<>();
		lst.add(c1);
		lst.add(c2);
		lst.add(c3);
		ComplexSeq1 seq = new ComplexSeq1(lst);
		System.out.println(seq);
		lst.get(0).set(1.0, 1.0);
		System.out.println(seq);
		System.out.println(lst);
		
		Double[] d = new RealSeqEleOperator().newArray(10);
		System.out.println(Arrays.toString(d));
	}
	
	private static Double newElement(Double a) {
		return a;
	}

}
