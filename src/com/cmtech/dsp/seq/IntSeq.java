package com.cmtech.dsp.seq;

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
}
