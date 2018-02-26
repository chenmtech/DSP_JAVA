/**
 * Project Name:DSP_JAVA
 * File Name:AbstractSeq.java
 * Package Name:com.cmtech.dsp.seq
 * Date:2018年2月25日上午6:01:20
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.seq;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.cmtech.dsp.util.FFT;

/**
 * ClassName: AbstractSeq
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月25日 上午6:01:20 
 *
 * @author bme
 * @version 
 * @param <T>
 * @since JDK 1.6
 */
public abstract class Seq<T> implements ISeq<T> {
	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * @since JDK 1.6
	 */
	private static final long serialVersionUID = 1L;
	
	ISeqBaseOperator<T> eOp = null;
	
	List<T> data = new ArrayList<T>();
	
	public Seq() {
		eOp = getSeqBaseOperator();
	}
	
	public Seq(int N) {
		this();
		setToZero(N);
	}
	
	public Seq(Collection<T> d) {
		this();
		for(T ele : d) {
			data.add(eOp.newElement(ele));
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
	public ISeq<T> setToZero(int N) {
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
		Seq<T> out = eOp.newInstance();
		for(int i = 0; i < data.size(); i++)
	    {
	        out.data.add(eOp.newElement(data.get(data.size()-1-i)));
	    }
		return out;
	}

	@Override
	public ISeq<T> plus(T a) {
		Seq<T> out = eOp.newInstance();
		for(int i = 0; i < data.size(); i++)
	    {
	        out.data.add(eOp.add(data.get(i), a));
	    }
		return out;
	}
	
	@Override
	public ISeq<T> minus(T a) {
		Seq<T> out = eOp.newInstance();
		for(int i = 0; i < data.size(); i++)
	    {
	        out.data.add(eOp.subtract(data.get(i), a));
	    }
		return out;
	}

	@Override
	public ISeq<T> multiple(T a) {
		Seq<T> out = eOp.newInstance();
		for(int i = 0; i < data.size(); i++)
	    {
	        out.data.add(eOp.multiple(data.get(i), a));
	    }
		return out;
	}

	@Override
	public ISeq<T> divide(T a) {
		Seq<T> out = eOp.newInstance();
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
