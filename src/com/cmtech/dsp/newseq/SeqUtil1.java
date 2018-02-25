package com.cmtech.dsp.newseq;

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
public class SeqUtil1 {
	private SeqUtil1() {
	}
	
	/**
	 * 
	 * add: 两个实序列相加
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param seq1
	 * @param seq2
	 * @return 和
	 * @since JDK 1.6
	 */
	public static <T> ISeq1<T> add(ISeq1<T> seq1, ISeq1<T> seq2) {
		return process(seq1, seq2, new IRealOperator() {
			@Override
			public double operator(double op1, double op2) {
				return op1+op2;
			}
		});
	}
	
	/**
	 * 
	 * subtract: 两个实序列相减
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param seq1
	 * @param seq2
	 * @return 差
	 * @since JDK 1.6
	 */
	public static RealSeq subtract(RealSeq seq1, RealSeq seq2) {
		return process(seq1, seq2, new IRealOperator() {
			@Override
			public double operator(double op1, double op2) {
				return op1-op2;
			}
		});
	}
	
	/**
	 * 
	 * multiple: 两个实序列相乘
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param seq1
	 * @param seq2
	 * @return 积
	 * @since JDK 1.6
	 */
	public static RealSeq multiple(RealSeq seq1, RealSeq seq2) {
		return process(seq1, seq2, new IRealOperator() {
			@Override
			public double operator(double op1, double op2) {
				return op1*op2;
			}
		});
	}
	
	/**
	 * 
	 * divide: 两个实序列相除
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param seq1
	 * @param seq2
	 * @return 商
	 * @since JDK 1.6
	 */
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

	/**
	 * 
	 * add: 两个复序列相加
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param seq1
	 * @param seq2
	 * @return 和
	 * @since JDK 1.6
	 */
	public static ComplexSeq add(ComplexSeq seq1, ComplexSeq seq2) {
		return process(seq1, seq2, new IComplexOperator() {
			@Override
			public Complex operator(Complex op1, Complex op2) {
				return Complex.add(op1, op2);
			}
		});	
	}
	
	/**
	 * 
	 * subtract: 两个复序列相减
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param seq1
	 * @param seq2
	 * @return 差
	 * @since JDK 1.6
	 */
	public static ComplexSeq subtract(ComplexSeq seq1, ComplexSeq seq2) {
		return process(seq1, seq2, new IComplexOperator() {
			@Override
			public Complex operator(Complex op1, Complex op2) {
				return Complex.subtract(op1, op2);
			}
		});	
	}
	
	/**
	 * 
	 * multiple: 两个复序列相相乘
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param seq1
	 * @param seq2
	 * @return 积
	 * @since JDK 1.6
	 */
	public static ComplexSeq multiple(ComplexSeq seq1, ComplexSeq seq2) {
		return process(seq1, seq2, new IComplexOperator() {
			@Override
			public Complex operator(Complex op1, Complex op2) {
				return Complex.multiple(op1, op2);
			}
		});
	}	
	
	/**
	 * 
	 * divide: 两个复序列相除
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param seq1
	 * @param seq2
	 * @return 商
	 * @since JDK 1.6
	 */
	public static ComplexSeq divide(ComplexSeq seq1, ComplexSeq seq2) {
		return process(seq1, seq2, new IComplexOperator() {
			@Override
			public Complex operator(Complex op1, Complex op2) {
				return Complex.divide(op1, op2);
			}
		});
	}
	
	private static ComplexSeq process(ComplexSeq seq1, ComplexSeq seq2, IComplexOperator op) {
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

	
	/**
	 * 
	 * conv: 求两个实序列的线性卷积和
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param seq1
	 * @param seq2
	 * @return 线性卷积和
	 * @since JDK 1.6
	 */
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
	
	/**
	 * 
	 * conv:求两个复序列的线性卷积和
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param seq1
	 * @param seq2
	 * @return 线性卷积和
	 * @since JDK 1.6
	 */
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
	
	/**
	 * 
	 * cirConvUsingDFT: 用FFT求两个序列的N点圆周卷积
	 * 但是由于FFT只能求2的整数幂长度，所以实际返回的序列长度可能比N要长
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param seq1
	 * @param seq2
	 * @param N 圆周卷积点数
	 * @return 圆周卷积
	 * @since JDK 1.6
	 */
	public static ComplexSeq cirConvUsingDFT(ISeq seq1, ISeq seq2, int N)
	{
	    ComplexSeq seq1DFT = seq1.fft(N);
	    ComplexSeq seq2DFT = seq2.fft(N);
	    ComplexSeq dft = SeqUtil1.multiple(seq1DFT, seq2DFT);
	    return dft.ifft();
	}

	/**
	 * 
	 * convUsingDFT: 用FFT求两个复序列的线性卷积和（实际上求得是圆周卷积）
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param seq1
	 * @param seq2
	 * @return 线性卷积和
	 * @since JDK 1.6
	 */
	public static ComplexSeq convUsingDFT(ComplexSeq seq1, ComplexSeq seq2)
	{
		int N = seq1.size()+seq2.size()-1;
	    return cirConvUsingDFT(seq1, seq2, N).changeSize(N);
	}
	

	/**
	 * 
	 * convUsingDFT: 用FFT求两个实序列的线性卷积和（实际上求得是圆周卷积）
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param seq1
	 * @param seq2
	 * @return 线性卷积和
	 * @since JDK 1.6
	 */
	public static RealSeq convUsingDFT(RealSeq seq1, RealSeq seq2)
	{
		int N = seq1.size()+seq2.size()-1;
		double[] data = cirConvUsingDFT(seq1, seq2, N).changeSize(N).realToArray();
		RealSeq seq = new RealSeq();
	    seq.data = data;
		return seq;
	}
}
