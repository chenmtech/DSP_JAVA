package com.cmtech.dsp.seq;
/*
Copyright (c) 2008 chenm
*/
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.cmtech.dsp.util.Complex;
import com.cmtech.dsp.util.FFT;
import com.cmtech.dsp.util.SeqUtil;

/**
 * this is a abstract Seq<T>, which implement almost the methods in the interface ISeq<T>
 * @author chenm
 * @version 2008-06
 * @param <T> the element type, which can only be the extended Number or the Complex class
 */
public abstract class Seq<T> implements ISeq<T> {

	private static final long serialVersionUID = 1L;
	
	// number basic operator
	IElementBasicOperator<T> basicOp = null;
	
	// the data array
	List<T> data = new ArrayList<T>();
	
	public Seq() {
		basicOp = getBasicOperator();
	}
	
	public Seq(int N) {
		this();
		zero(N);
	}
	
	public Seq(Collection<T> d) {
		this();
		data.addAll(d);
	}
	
	public Seq(Seq<T> seq) {
		this(seq.data);
	}
	
	@Override
	public int size() {		
		return data.size();
	}
	
	@Override
	public void zero(int N) {
		data = new ArrayList<T>();
		for(int i = 0; i < N; i++) {
			data.add(basicOp.zeroElement());
		}
	}
	
	@Override
	public T get(int i) {
		return data.get(i);
	}

	@Override
	public boolean set(int i, T element) {
		data.set(i, element);
		return true;
	}
	
	@Override
	public void clear() {
		data = new ArrayList<>();
	}
	
	@Override
	public void reSize(int N) {
		if(size() == N) return;
		
		List<T> tmp = new ArrayList<>();
		for(int i = 0; i < N; i++) {
			if(i < data.size()) tmp.add(data.get(i));
			else tmp.add(basicOp.zeroElement());
		}
		data = tmp;
	}
	
	@Override
	public RealSeq abs() {
		RealSeq out = new RealSeq();
		for(T ele : data) {
			out.data.add(basicOp.abs(ele));
		}
		return out;
	}
	
	@Override
	public RealSeq angle() {
		RealSeq out = new RealSeq();
		for(T ele : data) {
			out.data.add(basicOp.angle(ele));
		}
		return out;
	}

	@Override
	public ISeq<T> reverse() {
		Seq<T> out = basicOp.nullSeq();
		for(int i = 0; i < data.size(); i++)
	    {
	        out.data.add(data.get(data.size()-1-i));
	    }
		return out;
	}

	@Override
	public ISeq<T> add(T a) {
		Seq<T> out = basicOp.nullSeq();
		for(int i = 0; i < data.size(); i++)
	    {
	        out.data.add(basicOp.add(data.get(i), a));
	    }
		return out;
	}
	
	@Override
	public ISeq<T> subtract(T a) {
		Seq<T> out = basicOp.nullSeq();
		for(int i = 0; i < data.size(); i++)
	    {
	        out.data.add(basicOp.subtract(data.get(i), a));
	    }
		return out;
	}

	@Override
	public ISeq<T> multiply(T a) {
		Seq<T> out = basicOp.nullSeq();
		for(int i = 0; i < data.size(); i++)
	    {
	        out.data.add(basicOp.multiply(data.get(i), a));
	    }
		return out;
	}

	@Override
	public ISeq<T> divide(T a) {
		Seq<T> out = basicOp.nullSeq();
		for(int i = 0; i < data.size(); i++)
	    {
	        out.data.add(basicOp.divide(data.get(i), a));
	    }
		return out;
	}

	@Override
	public T sum() {
		T sum = basicOp.zeroElement();
		for(T ele : data) {
			sum = basicOp.add(sum, ele);
		}
		
		return sum;
	}

	@Override
	public void append(T ele) {
		data.add(ele);
	}

	@Override
	public T[] toArray(int N) {
		T[] rtn = basicOp.newArray(N);
		int min = Math.min(N, size());
		for(int i = 0; i < min; i++) {
			rtn[i] = data.get(i);
		}
		return rtn;
	}

	@Override
	public T[] toArray() {
		return toArray(size());
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

	@Override
	public ComplexSeq fft() {
		return FFT.fft(this);
	}

	@Override
	public ComplexSeq fft(int N) {
		return FFT.fft(this, N);
	}

	@Override
	public ComplexSeq ifft() {
		return FFT.ifft(this);
	}

	@Override
	public ComplexSeq ifft(int N) {
		return FFT.ifft(this, N);
	}	
	
	@Override
	public boolean equals(Object otherObject) {
		if(this == otherObject) return true;
		if(otherObject == null) return false;
		if(getClass() != otherObject.getClass()) return false;
		@SuppressWarnings("unchecked")
		Seq<T> other = (Seq<T>)otherObject;
		return  data.equals(other.data);
	}

	@Override
	public int hashCode() {
		return data.hashCode();
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[ size=" + size() + " data=" + data + " ]";
	}

}
