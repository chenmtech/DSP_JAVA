/**
 * Project Name:DSP_JAVA
 * File Name:AbstractSeq.java
 * Package Name:com.cmtech.dsp.seq
 * Date:2018年2月25日上午6:01:20
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.newseq;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.cmtech.dsp.seq.ComplexSeq;

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
public abstract class AbstractSeq<T> implements ISeq1<T> {
	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * @since JDK 1.6
	 */
	private static final long serialVersionUID = 1L;
	
	ISeqEleOperator<T> eOp = null;
	
	List<T> data = new ArrayList<T>();
	
	public AbstractSeq() {
		eOp = getSeqEleOperator();
	}
	
	public AbstractSeq(int N) {
		this();
		setToZero(N);
	}
	
	public AbstractSeq(T...d) {
		this();
		for(T ele : d) {
			data.add(eOp.newElement(ele));
		}
	}
	
	public AbstractSeq(Collection<T> d) {
		this();
		for(T ele : d) {
			data.add(eOp.newElement(ele));
		}
	}
	
	public AbstractSeq(AbstractSeq<T> seq) {
		this(seq.data);
	}
	
	public abstract ISeqEleOperator<T> getSeqEleOperator();
	
	@Override
	public int size() {		
		return data.size();
	}
	
	@Override
	public ISeq1<T> setToZero(int N) {
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
	public ISeq1<T> clear() {
		data = new ArrayList<>();
		return this;
	}
	
	@Override
	public ISeq1<T> changeSize(int N) {
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
	public RealSeq1 abs() {
		RealSeq1 out = new RealSeq1();
		for(T ele : data) {
			out.data.add(eOp.abs(ele));
		}
		return out;
	}
	
	@Override
	public RealSeq1 angle() {
		RealSeq1 out = new RealSeq1();
		for(T ele : data) {
			out.data.add(eOp.angle(ele));
		}
		return out;
	}

	@Override
	public T[] toArray() {
		T[] rtn = eOp.newArray(size());
		int i = 0;
		for(T ele : data) {
			rtn[i++] = eOp.newElement(ele);
		}
		return rtn;
	}

	@Override
	public T[] toArray(int N) {
		T[] rtn = eOp.newArray(N);
		int min = Math.min(N, size());
		for(int i = 0; i < min; i++) {
			rtn[i] = eOp.newElement(data.get(i));
		}
		return rtn;
	}

	@Override
	public ISeq1<T> reverse() {
		AbstractSeq<T> out = eOp.newInstance();
		for(int i = 0; i < data.size(); i++)
	    {
	        out.data.add(data.get(data.size()-1-i));
	    }
		return out;
	}
	
	
}
