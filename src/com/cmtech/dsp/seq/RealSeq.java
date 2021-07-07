package com.cmtech.dsp.seq;
/*
Copyright (c) 2008 chenm
*/
import java.util.Collection;
import java.util.Collections;

import com.cmtech.dsp.bmefile.BmeFile;
import com.cmtech.dsp.bmefile.BmeFileHead;
import com.cmtech.dsp.exception.FileException;
import com.cmtech.dsp.util.SeqUtil;


public class RealSeq extends Seq<Double>{

	private static final long serialVersionUID = 1L;
	
	private static final INumBasicOperator<Double> EOP = new DoubleBasicOperator();
	
	@Override
	public INumBasicOperator<Double> getBasicOperator() {
		return EOP;
	}
	
	public RealSeq() {
		super();
	}
	
	public RealSeq(int N) {
		super(N);
	}
	
	public RealSeq(double...d) {
		super();
		for(double ele : d) {
			data.add(ele);
		}
	}
	
	public RealSeq(Collection<Double> d) {
		super(d);
	}
	
	public RealSeq(RealSeq seq) {
		super(seq);
	}

	public Double max() {
		return Collections.max(data);
	}

	public Double min() {
		return Collections.min(data);
	}

	@Override
	public ComplexSeq dtft(RealSeq omega) {
		ComplexSeq out = new ComplexSeq(this).dtft(omega);
		return out;
	}

	@Override
	public ComplexSeq dtft(int N) {
		return dtft(SeqUtil.linSpace(0, Math.PI, N));
	}

	public double[] toArray() {
		return toArray(size());
	}

	public double[] toArray(int N) {
		double[] rtn = new double[N];
		int min = Math.min(N, size());
		for(int i = 0; i < min; i++) {
			rtn[i] = data.get(i);
		}
		return rtn;
	}
	
	public void saveAsBmeFile(String fileName) throws FileException {
		BmeFile.createBmeFile(fileName).writeData(toArray()).close();
	}
	
	public void saveAsBmeFile(String fileName, BmeFileHead head) throws FileException {
		BmeFile.createBmeFile(fileName, head).writeData(toArray()).close();
	}
}
