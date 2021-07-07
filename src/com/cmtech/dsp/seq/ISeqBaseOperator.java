package com.cmtech.dsp.seq;


public interface ISeqBaseOperator<T> {
	T zeroElement();
	T newElement(T ele);
	T[] newArray(int N);
	T add(T d1, T d2);
	T subtract(T d1, T d2);
	T multiple(T d1, T d2);
	T divide(T d1, T d2);
	double abs(T ele);
	double angle(T ele);
	Seq<T> newInstance();
}
