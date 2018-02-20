package com.cmtech.dsp.seq;

import java.util.Arrays;

/**
 * 
 * ClassName: SeqUtil
 * Function: 序列工具类，主要用于实现两个序列之间的算术运算，比如加减乘除，以及卷积和 
 * Reason: TODO ADD REASON(可选). 
 * date: 2018年2月20日 上午11:31:37 
 *
 * @author bme
 * @version 
 * @since JDK 1.6
 */
public class SeqUtil {
	private SeqUtil() {
	}
	
	interface IRealOperator {
		double operator(double op1, double op2);
	}
	
	interface IComplexOperator {
		Complex operator(Complex op1, Complex op2);
	}
	
	public static RealSeq add(RealSeq seq1, RealSeq seq2) {
		return process(seq1, seq2, new IRealOperator() {
			@Override
			public double operator(double op1, double op2) {
				return op1+op2;
			}
		});
	}
	
	public static RealSeq subtract(RealSeq seq1, RealSeq seq2) {
		return process(seq1, seq2, new IRealOperator() {
			@Override
			public double operator(double op1, double op2) {
				return op1-op2;
			}
		});
	}
	
	public static RealSeq multiple(RealSeq seq1, RealSeq seq2) {
		return process(seq1, seq2, new IRealOperator() {
			@Override
			public double operator(double op1, double op2) {
				return op1*op2;
			}
		});
	}
	
	public static RealSeq divide(RealSeq seq1, RealSeq seq2) {
		return process(seq1, seq2, new IRealOperator() {
			@Override
			public double operator(double op1, double op2) {
				return op1/op2;
			}
		});
	}
	
	private static RealSeq process(RealSeq seq1, RealSeq seq2, IRealOperator op) {
		int N = Math.max(seq1.size(), seq2.size());
		double[] data1 = seq1.toArray(N);
		double[] data2 = seq2.toArray(N);
		for(int i = 0; i < N; i++) {
			data1[i] = op.operator(data1[i], data2[i]);
		}
		RealSeq out = new RealSeq();
		out.data = data1;
		return out;	
	}

	
	public static ComplexSeq add(ComplexSeq seq1, ComplexSeq seq2) {
		return process(seq1, seq2, new IComplexOperator() {
			@Override
			public Complex operator(Complex op1, Complex op2) {
				return Complex.add(op1, op2);
			}
		});	
	}
	
	public static ComplexSeq subtract(ComplexSeq seq1, ComplexSeq seq2) {
		return process(seq1, seq2, new IComplexOperator() {
			@Override
			public Complex operator(Complex op1, Complex op2) {
				return Complex.subtract(op1, op2);
			}
		});	
	}
	
	public static ComplexSeq multiple(ComplexSeq seq1, ComplexSeq seq2) {
		return process(seq1, seq2, new IComplexOperator() {
			@Override
			public Complex operator(Complex op1, Complex op2) {
				return Complex.multiple(op1, op2);
			}
		});
	}	
	
	public static ComplexSeq divide(ComplexSeq seq1, ComplexSeq seq2) {
		return process(seq1, seq2, new IComplexOperator() {
			@Override
			public Complex operator(Complex op1, Complex op2) {
				return Complex.divide(op1, op2);
			}
		});
	}
	
	public static ComplexSeq process(ComplexSeq seq1, ComplexSeq seq2, IComplexOperator op) {
		int N = Math.max(seq1.size(), seq2.size());
		Complex[] data1 = seq1.toArray(N);
		Complex[] data2 = seq2.toArray(N);
		for(int i = 0; i < N; i++) {
			data1[i] = op.operator(data1[i], data2[i]);
		}
		ComplexSeq out = new ComplexSeq();
		out.data = data1;
		return out;	
	}

	
	
	public static RealSeq conv(RealSeq seq1, RealSeq seq2) {
	    int N1 = seq1.size();
	    int N2 = seq2.size();
	    int N = N1+N2-1;
	    
	    RealSeq out = new RealSeq(N);
	    
	    int n = 0;
	    int m = 0;
	    int n_m = 0;  
	    double tmp = 0.0;
	    for(n = 0; n < N; n++)
	    {
	    		tmp = 0.0;
	        for(m = 0; m < N1; m++)
	        {
	            n_m = n - m;
	            if( (n_m >= 0) && (n_m < N2) )
	            		tmp += seq1.data[m]*seq2.data[n_m];
	        }
	        out.data[n] = tmp;
	    }
	    return out;
	}
	
	public static ComplexSeq conv(ComplexSeq seq1, ComplexSeq seq2) {
	    int N1 = seq1.size();
	    int N2 = seq2.size();
	    int N = N1+N2-1;
	    
	    ComplexSeq out = new ComplexSeq(N);
	    
	    int n = 0;
	    int m = 0;
	    int n_m = 0;  
	    Complex tmp = new Complex();
	    for(n = 0; n < N; n++)
	    {
	    		tmp = new Complex();
	        for(m = 0; m < N1; m++)
	        {
	            n_m = n - m;
	            if( (n_m >= 0) && (n_m < N2) )
	            		tmp.add(Complex.multiple(seq1.data[m], seq2.data[n_m]));
	        }
	        out.data[n] = tmp;
	    }
	    return out;
	}
	
	//用FFT求两个复序列的N点圆周卷积 
	//seq1：复序列1
	//seq2：复序列2
	//N：圆周卷积的点数 
	public static ComplexSeq cirConvUsingDFT(ISeq seq1, ISeq seq2, int N)
	{
	    ComplexSeq seq1DFT = seq1.fft(N);
	    ComplexSeq seq2DFT = seq2.fft(N);
	    ComplexSeq dft = SeqUtil.multiple(seq1DFT, seq2DFT);
	    return dft.ifft();
	}

	//用FFT求两个复序列的线性卷积 
	//seq1：复序列1
	//seq2：复序列2
	public static ComplexSeq convUsingDFT(ComplexSeq seq1, ComplexSeq seq2)
	{
	    return cirConvUsingDFT(seq1, seq2, seq1.size()+seq2.size()-1);
	}
	
	public static RealSeq convUsingDFT(RealSeq seq1, RealSeq seq2)
	{
		double[] data = cirConvUsingDFT(seq1, seq2, seq1.size()+seq2.size()-1).realToArray();
	    data = Arrays.copyOf(data, seq1.size()+seq2.size()-1);
		RealSeq seq = new RealSeq();
	    seq.data = data;
		return seq;
	}
}
