package com.cmtech.dsp.seq;
/*
Copyright (c) 2008 chenm
*/
import java.io.Serializable;

/**
 * An interface for a sequence
 * @author chenm
 * @version 2008-05
 * @param <T> the element type, which can be one of the number types or the Complex class
 */
public interface ISeq<T> extends Serializable{
	// get the element basic operator
	IElementBasicOperator<T> getBasicOperator(); 
	
	// get the length of this sequence
	int size(); 
	
	// set to a sequence with all the elements zero and the length N
	void setZero(int N);
	
	// get the ith element
	T get(int i); 
	
	// set the ith element
	boolean set(int i, T element);
	
	// clear and delete all the elements
	void clear(); 
	
	// change the length of this sequence to N
	void reSize(int N); 
	
	// calculate the absolute value of every element and create a RealSeq using these values
	RealSeq abs(); 
	
	// calculate the angle value of every element and create a RealSeq using the values
	RealSeq angle();
	
	// sum all the elements
	T sum(); 
	
	// append one element
	void append(T ele); 
	
	// reverse the sequence and return a new sequence. the original sequence don't change
	ISeq<T> reverse(); 
	
	// add every element value with the "a" and return a new sequence. the original sequence don't change
	ISeq<T> add(T a); 
	
	// make every element subtracted with the "a" and return a new sequence. the original sequence don't change
	ISeq<T> subtract(T a); 
	
	// make every element multiplied with the "a" and return a new sequence. the original sequence don't change
	ISeq<T> multiply(T a); 
	
	// make every element divided with the "a" and return a new sequence. the original sequence don't change
	ISeq<T> divide(T a); 
	
	// output an array with the length N
	T[] toArray(int N);
	
	// output an array
	T[] toArray();
	
	// return the DTFT values of this sequence at the frequency points contained in the RealSeq omega
	ComplexSeq dtft(RealSeq omega); 
	
	// return the DTFT values of this sequence at the N frequency points in the range of [0, pi], or [0, 2*pi] for the ComplexSeq
	ComplexSeq dtft(int N); 
	
	// return FFT values of this sequence. If the length of the sequence is not 2^X, the FFT length is changed to 2^X
	ComplexSeq fft(); 
	
	// return the FFT values of this sequence. If the N is not 2^X, the FFT length is changed to 2^X
	ComplexSeq fft(int N);
	
	// return the iFFT values of this sequence. If the length of the sequence is not 2^X, the iFFT length is changed to 2^X
	ComplexSeq ifft();
	
	// return the iFFT values of this sequence. If the N is not 2^X, the iFFT length is changed to 2^X
	ComplexSeq ifft(int N);
}
