/**
 * 
 */
package com.cmtech.dsp_java.seq;

/**
 * @author chenm
 *
 */
public interface ISeq<E> {
	void setAsZeroSequence(int N);
	E get(int i);
	boolean set(int i, E element);
	E[] toArray();
	void clear();
	int size();
	void changeSize(int N);	
	ISeq<E> reverse();	
	ISeq<E> add(E a);
	ISeq<E> multiple(E a);
	RealSeq abs();
	E sum();
	ComplexSeq dtft(RealSeq omega);
	ComplexSeq dtft(int N);
}
