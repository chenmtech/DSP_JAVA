package com.cmtech.dsp.seq;

import java.util.Arrays;
import java.util.Collection;

/**
 * ClassName: ComplexSeq
 * Function: 复序列. 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月2日 上午5:56:24 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class ComplexSeq implements IComplexSeq {
	/**
	 * serialVersionUID:TODO(用一句话描述这个变量表示什么).
	 * @since JDK 1.6
	 */
	private static final long serialVersionUID = 1L;
	private Complex[] data;
	
	public ComplexSeq() {
		data = new Complex[0];
	}
	
	public ComplexSeq(int N) {
		initToZeroSequence(N);
	}
	
	public ComplexSeq(Complex... d) {
		data = Arrays.copyOf(d, d.length);
	}
	
	public ComplexSeq(Collection<Complex> d) {
		data = (Complex[]) Arrays.copyOf(d.toArray(),d.size());
	}
	
	public ComplexSeq(ComplexSeq seq) {
		data = (Complex[]) Arrays.copyOf(seq.data, seq.size());
	}
	
	public ComplexSeq(RealSeq re, RealSeq im) {
		int N = Math.max(re.size(), im.size());
		re.changeSize(N);
		im.changeSize(N);
		data = new Complex[N];
		for(int i = 0; i < N; i++) {
			data[i] = new Complex(re.get(i), im.get(i));
		}
	}
	
	public ComplexSeq(double[] re, double[] im) {
		int N = Math.max(re.length, im.length);
		data = new Complex[N];
		double R = 0.0;
		double I = 0.0;
		for(int i = 0; i < N; i++) {
			R = (i < re.length) ? re[i] : 0.0;
			I = (i < im.length) ? im[i] : 0.0;
			data[i] = new Complex(R, I);
		}
	}
	
	public ComplexSeq(RealSeq re) {
		this(re, new RealSeq(re.size()));
	}
	
	@Override
	public void initToZeroSequence(int N) {
		data = new Complex[N];
		for(int i = 0; i < N; i++) {
			data[i] = new Complex();
		}
	}

	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * @see com.cmtech.dsp.seq.ISeq#clear()
	 */
	@Override
	public void clear() {
		data = new Complex[0];
	}
	
	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * @see com.cmtech.dsp.seq.ISeq#size()
	 */
	@Override
	public int size() {
		// TODO Auto-generated method stub
		return data.length;
	}
	
	@Override
	public void changeSize(int N) {
		if(size() == N) return;
		
		Complex[] buf = new Complex[N];
		for(int i = 0; i < N; i++) {
			if(i < data.length) buf[i] = data[i];
			else buf[i] = new Complex();
		}
		data = buf;
	}
	
	@Override
	public RealSeq abs() {
		RealSeq out = new RealSeq(size());
		for(int i = 0; i < size(); i++) {
			out.set(i, data[i].abs());
		}
		return out;
	}
	
	@Override
	public RealSeq angle() {
		RealSeq out = new RealSeq(size());
		for(int i = 0; i < size(); i++) {
			out.set(i, data[i].angle());
		}
		return out;
	}
	

	@Override
	public ComplexSeq dtft(RealSeq omega) {
		int Nw = omega.size();
		ComplexSeq out = new ComplexSeq(Nw);
		int N = size();
		ComplexSeq ejwn, tmpseq;
		for(int i = 0; i < Nw; i++) {
			ejwn = SeqFactory.createEJWSeq(-omega.get(i), 0, N);
			tmpseq = SeqUtil.multiple(this, ejwn);
			out.set(i, tmpseq.sum());
		}
		return out;
	}
	
	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * @see com.cmtech.dsp.seq.ISeq#dtft(int)
	 */
	@Override
	public ComplexSeq dtft(int N) {
		//复序列的DTFT没有对称性，所以需要求0~2*PI整个周期的频谱
		return dtft(SeqFactory.linSpace(0, 2*Math.PI, N));	
	}
	
	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * @see com.cmtech.dsp.seq.IComplexSeq#get(int)
	 */
	@Override
	public Complex get(int i) {
		// TODO Auto-generated method stub
		return data[i];
	}

	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * @see com.cmtech.dsp.seq.IComplexSeq#set(int, com.cmtech.dsp.seq.Complex)
	 */
	@Override
	public boolean set(int i, Complex element) {
		data[i] = element;
		return true;
	}
	
	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * @see com.cmtech.dsp.seq.IComplexSeq#toArray()
	 */
	@Override
	public Complex[] toArray() {
		return Arrays.copyOf(data, data.length);
	}
	
	@Override
	public Complex[] toArray(int N) {
		Complex[] rtn = new Complex[N];
		for(int i = 0; i < N; i++) {
			if(i < data.length) rtn[i] = new Complex(data[i]);
			else rtn[i] = new Complex();
		}
		return rtn;
	}
	
	@Override
	public double[] realToArray() {
		double[] out = new double[data.length];
		for(int i = 0; i < data.length; i++) {
			out[i] = data[i].getReal();
		}
		return out;
	}
	
	@Override
	public double[] realToArray(int N) {
		double[] out = new double[N];
		for(int i = 0; i < N; i++) {
			if(i < data.length) out[i] = data[i].getReal();
			else out[i] = 0.0;
		}
		return out;
	}
	
	@Override
	public double[] imagToArray() {
		double[] out = new double[data.length];
		for(int i = 0; i < data.length; i++) {
			out[i] = data[i].getImag();
		}
		return out;
	}
	
	@Override
	public double[] imagToArray(int N) {
		double[] out = new double[N];
		for(int i = 0; i < N; i++) {
			if(i < data.length) out[i] = data[i].getImag();
			else out[i] = 0.0;
		}
		return out;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[ size=" + data.length + " data=" + Arrays.toString(data) + " ]";
	}

	@Override
	public ComplexSeq reverse() {
		ComplexSeq out = new ComplexSeq(size());
		for(int i = 0; i < data.length; i++)
	    {
	        out.data[i] = data[data.length-1-i];
	    }
		return out;
	}
	
	@Override
	public ComplexSeq plus(Complex a) {
		ComplexSeq out = new ComplexSeq(this);
		for(int i = 0; i < size(); i++) {
			out.data[i].add(a);
		}
		return out;
	}
	
	
	@Override
	public ComplexSeq minus(Complex a) {
		ComplexSeq out = new ComplexSeq(this);
		for(int i = 0; i < size(); i++) {
			out.data[i].subtract(a);
		}
		return out;
	}

	@Override
	public ComplexSeq multiple(Complex a) {
		ComplexSeq out = new ComplexSeq(this);
		for(int i = 0; i < size(); i++) {
			out.data[i].multiple(a);
		}
		return out;
	}
	
	@Override
	public ComplexSeq divide(Complex a) {
		ComplexSeq out = new ComplexSeq(this);
		for(int i = 0; i < size(); i++) {
			out.data[i].divide(a);
		}
		return out;
	}
	
	@Override
	public Complex sum() {
		Complex sum = new Complex();
		for(Complex ele : data) {
			sum.add(ele);
		}
		return sum;
	}
	
	public RealSeq dB() {
		RealSeq out = abs();
		double max = out.max();
		for(int i = 0; i < out.size(); i++) {			
			out.set(i, 20*Math.log10(out.get(i)/max));
		}
		return out;	
	}

	@Override
	public ComplexSeq fft() {
		/*int N = SeqUtil.findPowerOfTwo(data.length);
		double[][] buf = {realToArray(N), imagToArray(N)};
		FastFourierTransformer.transformInPlace(buf, DftNormalization.STANDARD, TransformType.FORWARD);
		ComplexSeq rtn = new ComplexSeq(new RealSeq(buf[0]), new RealSeq(buf[1]));
		return rtn;*/
		return FFT.fft(this);
	}
	
	@Override
	public ComplexSeq fft(int N) {
		return FFT.fft(this, N);
	}
}
