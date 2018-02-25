/**
 * Project Name:DSP_JAVA
 * File Name:RealSeq1.java
 * Package Name:com.cmtech.dsp.seq
 * Date:2018年2月25日上午6:15:57
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.newseq;

import java.util.Arrays;
import java.util.Collection;

import com.cmtech.dsp.seq.RealSeq;

/**
 * ClassName: RealSeq1
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月25日 上午6:15:57 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class RealSeq1 extends AbstractSeq<Double> {
	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * @since JDK 1.6
	 */
	private static final long serialVersionUID = 1L;
	
	private static final ISeqEleOperator<Double> EOP = new RealSeqEleOperator();
	
	@Override
	public ISeqEleOperator<Double> getSeqEleOperator() {
		return EOP;
	}
	
	public RealSeq1() {
		super();
	}
	
	public RealSeq1(int N) {
		super(N);
	}
	
	public RealSeq1(Double...d) {
		super(d);
	}
	
	public RealSeq1(Collection<Double> d) {
		super(d);
	}
	
	public RealSeq1(RealSeq1 seq) {
		super(seq);
	}
}
