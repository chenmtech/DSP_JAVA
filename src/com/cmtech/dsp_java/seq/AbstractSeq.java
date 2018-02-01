package com.cmtech.dsp_java.seq;

import java.util.Arrays;
import java.util.Collection;

public abstract class AbstractSeq<E> implements ISeq<E> {
	protected E[] data;
	
	public AbstractSeq(E... d) {
		data = Arrays.copyOf(d, d.length);
	}
	
	public AbstractSeq(Collection<E> d) {
		data = Arrays.copyOf((E[])d.toArray(),d.size());
	}
	
	public AbstractSeq(AbstractSeq seq) {
		data = Arrays.copyOf((E[])seq.toArray(), seq.size());
	}

	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[ size=" + data.length + " data=" + Arrays.toString(data) + " ]";
	}
	
	@Override
	public E get(int i) {
		if(i < data.length)
			return data[i];
		else
			return null;
	}

	@Override
	public boolean set(int i, E element) {
		if(i < size())
		{
			data[i] = element;
			return true;
		}
		else
			return false;
	}

	@Override
	public E[] toArray() {
		return Arrays.copyOf(data, data.length);
	}

	@Override
	public int size() {
		return data.length;
	}

}
