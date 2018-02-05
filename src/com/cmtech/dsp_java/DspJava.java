package com.cmtech.dsp_java;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import com.cmtech.dsp_java.filter.FIRFilter;
import com.cmtech.dsp_java.filter.IIRFilter;
import com.cmtech.dsp_java.filter.design.AFType;
import com.cmtech.dsp_java.filter.design.FIRDesigner;
import com.cmtech.dsp_java.filter.design.FIRSpec;
import com.cmtech.dsp_java.filter.design.FilterType;
import com.cmtech.dsp_java.filter.design.IIRDesigner;
import com.cmtech.dsp_java.filter.design.IIRSpec;
import com.cmtech.dsp_java.filter.design.WinType;
import com.cmtech.dsp_java.seq.Complex;
import com.cmtech.dsp_java.seq.ComplexSeq;
import com.cmtech.dsp_java.seq.RealSeq;
import com.cmtech.dsp_java.seq.SeqFactory;
import com.cmtech.dsp_java.seq.SeqUtil;

public class DspJava {

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
		
		RealSeq tmp = new RealSeq(1.0);
		System.out.println(tmp);
		
		RealSeq aaa = new RealSeq(1.0,0.0,-1.0);
		System.out.println(aaa);
		changeSize((double[])aaa.toArray());
		System.out.println(aaa);
		
		ArrayList<Integer> arr = new ArrayList<Integer>();
		for(int i = 0; i < 10; i++)
			arr.add((int)Math.random()*10);
		
		//Double[] d = {1.0};
		Complex[] darr = (Complex[]) Array.newInstance(Complex.class, 10);
		System.out.println(Arrays.asList(darr));
		
		aaa.clear();
		System.out.println(aaa);
		ComplexSeq cseq = new ComplexSeq(aaa,aaa);
		cseq.clear();
		System.out.println(cseq);
		
		RealSeq seq11 = new RealSeq();
		seq11.clear();
		System.out.println(seq11);
		
		RealSeq seq111 = SeqFactory.createRndSeq(3);
		RealSeq seq112 = SeqFactory.createRndSeq(3);
		RealSeq seq113 = SeqUtil.conv(seq111, seq112);
		System.out.println(seq113);
		ComplexSeq seq121 = new ComplexSeq(new RealSeq(3),seq111);
		ComplexSeq seq122 = new ComplexSeq(new RealSeq(3),seq112);
		ComplexSeq seq123 = SeqUtil.conv(seq121, seq122);
		System.out.println(seq123);
		
		
		
		
	}
	
	private static double[] changeSize(double[] a) {
		return a;
	}

}
