package com.cmtech.dsp.seq;
/*
Copyright (c) 2008 chenm
*/
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.cmtech.dsp.util.FFT;


public abstract class Seq<T> implements ISeq<T> {

	private static final long serialVersionUID = 1L;
	
	INumBasicOperator<T> eOp = null;
	
	List<T> data = new ArrayList<T>();
	
	public Seq() {
		eOp = getBasicOperator();
	}
	
	public Seq(int N) {
		this();
		setToZeroSeq(N);
	}
	
	public Seq(Collection<T> d) {
		this();
		for(T ele : d) {
			data.add(ele);
		}
	}
	
	public Seq(Seq<T> seq) {
		this(seq.data);
	}
	
	@Override
	public int size() {		
		return data.size();
	}
	
	@Override
	public ISeq<T> setToZeroSeq(int N) {
		data = new ArrayList<T>();
		for(int i = 0; i < N; i++) {
			data.add(eOp.zeroElement());
		}
		return this;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[ size=" + size() + " data=" + data + " ]";
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
	public ISeq<T> clear() {
		data = new ArrayList<>();
		return this;
	}
	
	@Override
	public ISeq<T> changeSize(int N) {
		if(size() == N) return this;
		
		List<T> buf = new ArrayList<>();
		for(int i = 0; i < N; i++) {
			if(i < data.size()) buf.add(data.get(i));
			else buf.add(eOp.zeroElement());
		}
		data = buf;
		return this;
	}
	
	@Override
	public RealSeq abs() {
		RealSeq out = new RealSeq();
		for(T ele : data) {
			out.data.add(eOp.abs(ele));
		}
		return out;
	}
	
	@Override
	public RealSeq angle() {
		RealSeq out = new RealSeq();
		for(T ele : data) {
			out.data.add(eOp.angle(ele));
		}
		return out;
	}

	@Override
	public ISeq<T> reverse() {
		Seq<T> out = eOp.newSeq();
		for(int i = 0; i < data.size(); i++)
	    {
	        out.data.add(data.get(data.size()-1-i));
	    }
		return out;
	}

	@Override
	public ISeq<T> plus(T a) {
		Seq<T> out = eOp.newSeq();
		for(int i = 0; i < data.size(); i++)
	    {
	        out.data.add(eOp.add(data.get(i), a));
	    }
		return out;
	}
	
	@Override
	public ISeq<T> minus(T a) {
		Seq<T> out = eOp.newSeq();
		for(int i = 0; i < data.size(); i++)
	    {
	        out.data.add(eOp.subtract(data.get(i), a));
	    }
		return out;
	}

	@Override
	public ISeq<T> multiple(T a) {
		Seq<T> out = eOp.newSeq();
		for(int i = 0; i < data.size(); i++)
	    {
	        out.data.add(eOp.multiply(data.get(i), a));
	    }
		return out;
	}

	@Override
	public ISeq<T> divide(T a) {
		Seq<T> out = eOp.newSeq();
		for(int i = 0; i < data.size(); i++)
	    {
	        out.data.add(eOp.divide(data.get(i), a));
	    }
		return out;
	}

	@Override
	public T sum() {
		T sum = eOp.zeroElement();
		for(T ele : data) {
			sum = eOp.add(sum, ele);
		}
		
		return sum;
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
	public ISeq<T> append(T ele) {
		data.add(ele);
		return this;
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
}
