package com.cmtech.dsp.seq;
/*
Copyright (c) 2008 chenm
*/
import java.util.Collection;
import java.util.Collections;

import com.cmtech.dsp.bmefile.BmeFile;
import com.cmtech.dsp.bmefile.BmeFileHead;
import com.cmtech.dsp.exception.FileException;


public class RealSeq extends Seq<Double>{

	private static final long serialVersionUID = 1L;
	
	private static final IElementBasicOperator<Double> ELEMENT_OP = new DoubleBasicOperator();
	
	@Override
	public IElementBasicOperator<Double> getBasicOperator() {
		return ELEMENT_OP;
	}
	
	public RealSeq() {
		super();
	}
	
	public RealSeq(int N) {
		super(N);
	}
	
	public RealSeq(Double...d) {
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
	
	public void saveAsBmeFile(String fileName) throws FileException {
		BmeFile.createBmeFile(fileName).writeData(toArray()).close();
	}
	
	public void saveAsBmeFile(String fileName, BmeFileHead head) throws FileException {
		BmeFile.createBmeFile(fileName, head).writeData(toArray()).close();
	}
}
