package com.cmtech.dsp.filter;
/*
Copyright (c) 2008 chenm
*/
import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.RealSeq;

/**
 * an interface of a filter, including analog and digital filter
 * @author chenm
 * @version 2008-10
 */
public interface IFilter {
	Double[] getB();
	Double[] getA();
	void setB(Double[] b);
	void setA(Double[] a);
	ComplexSeq freqResponse(RealSeq omega); // get the frequency response values at the frequencies omega.
	RealSeq magResponse(RealSeq omega); // get the magnitude response values at the frequencies omega.
	RealSeq phaResponse(RealSeq omega); // get the phase response values at the frequencies omega.
}
