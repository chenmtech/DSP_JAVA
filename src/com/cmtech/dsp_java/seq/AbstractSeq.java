package com.cmtech.dsp_java.seq;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;

public abstract class AbstractSeq<E> implements ISeq<E> {
	protected E[] data;
	
	@SafeVarargs
	public AbstractSeq(E... d) {
		data = Arrays.copyOf(d, d.length);
	}
	
	@SuppressWarnings("unchecked")
	public AbstractSeq(Collection<E> d) {
		data = (E[]) Arrays.copyOf(d.toArray(),d.size());
	}
	
	public AbstractSeq(AbstractSeq<E> seq) {
		data = (E[]) Arrays.copyOf(seq.toArray(), seq.size());
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
	
	@Override
	public void clear() {
		data = (E[]) Array.newInstance(data.getClass().getComponentType(), 0);
	}
	
	@Override
	public ComplexSeq dtft(int N) {
		return dtft(SeqFactory.linSpace(0, Math.PI, N));
	}

}
