package com.cmtech.dsp.seq;
/*
Copyright (c) 2008 chenm
*/
import java.io.Serializable;

/**
 * An interface for a sequence
 * @author chenm
 * @version 2008-05
 * @param <T> the element type
 */
public interface ISeq<T> extends Serializable{
	INumBasicOperator<T> getBasicOperator(); // get element basic operator
	int size();  // size
	ISeq<T> setToZeroSeq(int N); // set to a sequence with the element zero and the length N
	T get(int i);
	boolean set(int i, T element);
	ISeq<T> clear();
	ISeq<T> changeSize(int N);
	RealSeq abs();
	RealSeq angle();
	ISeq<T> reverse();
	ISeq<T> plus(T a);
	ISeq<T> minus(T a);
	ISeq<T> multiple(T a);
	ISeq<T> divide(T a);
	T sum();
	ISeq<T> append(T ele);
	ComplexSeq dtft(RealSeq omega);
	ComplexSeq dtft(int N);
	ComplexSeq fft();
	ComplexSeq fft(int N);
	ComplexSeq ifft();
	ComplexSeq ifft(int N);
}
