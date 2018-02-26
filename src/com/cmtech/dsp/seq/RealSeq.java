/**
 * Project Name:DSP_JAVA
 * File Name:RealSeq1.java
 * Package Name:com.cmtech.dsp.seq
 * Date:2018年2月25日上午6:15:57
 * Copyright (c) 2018, e_yujunquan@163.com All Rights Reserved.
 *
 */
package com.cmtech.dsp.seq;

import java.util.Collection;
import java.util.Collections;

import com.cmtech.dsp.bmefile.BmeFile;
import com.cmtech.dsp.bmefile.BmeFileHead;
import com.cmtech.dsp.exception.FileException;
import com.cmtech.dsp.util.SeqUtil;

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
public class RealSeq extends Seq<Double>{
	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * @since JDK 1.6
	 */
	private static final long serialVersionUID = 1L;
	
	private static final ISeqBaseOperator<Double> EOP = new RealSeqBaseOperator();
	
	@Override
	public ISeqBaseOperator<Double> getSeqBaseOperator() {
		return EOP;
	}
	
	public RealSeq() {
		super();
	}
	
	public RealSeq(int N) {
		super(N);
	}
	
	public RealSeq(double...d) {
		super();
		for(double ele : d) {
			data.add(ele);
		}
	}
	
	public RealSeq(Collection<Double> d) {
		super(d);
	}
	
	public RealSeq(RealSeq seq) {
		super(seq);
	}

	public Double max() {
		return Collections.max(data);
	}

	public Double min() {
		return Collections.min(data);
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

	public double[] toArray() {
		return toArray(size());
	}

	public double[] toArray(int N) {
		double[] rtn = new double[N];
		int min = Math.min(N, size());
		for(int i = 0; i < min; i++) {
			rtn[i] = data.get(i);
		}
		return rtn;
	}
	
	public void saveAsBmeFile(String fileName) throws FileException {
		BmeFile.createBmeFile(fileName).writeData(toArray()).close();
	}
	
	public void saveAsBmeFile(String fileName, BmeFileHead head) throws FileException {
		BmeFile.createBmeFile(fileName, head).writeData(toArray()).close();
	}
}
