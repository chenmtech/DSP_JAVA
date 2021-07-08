package com.cmtech.dsp.seq;

/*
Copyright (c) 2008 chenm
*/

/**
 * An interface of an operator which do some basic operations for a element of a sequence 
 * @author chenm
 * @version 2008-03
 * @param <T> type of the element of a sequence, which can only be a extended Number or a Complex class
 */
public interface IElementBasicOperator<T> {
	T zeroElement();  // create a zero element
	T[] newArray(int N); // create a new array with length N
	Seq<T> nullSeq(); // create a new null sequence which element type is <T>
	T add(T d1, T d2); // add two numbers
	T subtract(T d1, T d2); // subtract 
	T multiply(T d1, T d2); // multiply
	T divide(T d1, T d2); // divide
	double abs(T ele); // to get the absolute value of a real number or the modulus of a complex number
	double angle(T ele); // angle
}
