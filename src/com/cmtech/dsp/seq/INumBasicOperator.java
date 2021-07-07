package com.cmtech.dsp.seq;

/*
Copyright (c) 2008 chenm
*/

/**
 * An interface of an operator with some basic operations for a number 
 * @author chenm
 * @version 2008-03
 * @param <T> type of the number, which can be a complex
 */
public interface INumBasicOperator<T> {
	T zeroElement();  // create a zero element
	T[] newArray(int N); // create a new array with length N
	Seq<T> newSeq(); // create a new sequence with the element type <T>
	T add(T d1, T d2); // add two number
	T subtract(T d1, T d2); // subtract 
	T multiply(T d1, T d2); // multiply
	T divide(T d1, T d2); // divide
	double abs(T ele); // to get the absolute of the real number or the modulus of the complex number
	double angle(T ele); // angle
}
