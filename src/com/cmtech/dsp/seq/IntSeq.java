package com.cmtech.dsp.seq;
/*
Copyright (c) 2008 chenm
*/
import java.util.Collection;

/**
 * this is a sequence with Integer values
 * @author chenm
 * @version 2008-06
 */
public class IntSeq extends Seq<Integer> {

	private static final long serialVersionUID = 1L;
	
	private static final IElementBasicOperator<Integer> ELEMENT_OP = new IntegerBasicOperator();

	@Override
	public IElementBasicOperator<Integer> getBasicOperator() {
		return ELEMENT_OP;
	}

	public IntSeq() {
		super();
	}
	
	public IntSeq(int N) {
		super(N);
	}
	
	public IntSeq(Integer...d) {
		super();
		for(int ele : d) {
			data.add(ele);
		}
	}
	
	public IntSeq(Collection<Integer> d) {
		super(d);
	}
	
	public IntSeq(IntSeq seq) {
		super(seq);
	}
}
