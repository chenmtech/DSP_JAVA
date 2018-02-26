package com.cmtech.dsp.util;

import com.cmtech.dsp.seq.Seq;
import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.ISeq;
import com.cmtech.dsp.seq.ISeqBaseOperator;
import com.cmtech.dsp.seq.RealSeq;

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
	
	private interface IBiOperator<T> {
		T operator(T op1, T op2);
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
	public static <T> ISeq<T> add(ISeq<T> seq1, ISeq<T> seq2) {
		return process(seq1, seq2, new IBiOperator<T>() {
			@Override
			public T operator(T op1, T op2) {
				return seq1.getSeqBaseOperator().add(op1, op2);
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
	public static <T> ISeq<T> subtract(ISeq<T> seq1, ISeq<T> seq2) {
		return process(seq1, seq2, new IBiOperator<T>() {
			@Override
			public T operator(T op1, T op2) {
				return seq1.getSeqBaseOperator().subtract(op1, op2);
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
	public static <T> ISeq<T> multiple(ISeq<T> seq1, ISeq<T> seq2) {
		return process(seq1, seq2, new IBiOperator<T>() {
			@Override
			public T operator(T op1, T op2) {
				return seq1.getSeqBaseOperator().multiple(op1, op2);
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
	public static <T> ISeq<T> divide(ISeq<T> seq1, ISeq<T> seq2) {
		return process(seq1, seq2, new IBiOperator<T>() {
			@Override
			public T operator(T op1, T op2) {
				return seq1.getSeqBaseOperator().divide(op1, op2);
			}
		});
	}
	
	private static <T> ISeq<T> process(ISeq<T> seq1, ISeq<T> seq2, IBiOperator<T> op) {
		int N = Math.max(seq1.size(), seq2.size());
		seq1.changeSize(N);
		seq2.changeSize(N);
		Seq<T> out = seq1.getSeqBaseOperator().newInstance();
		
		for(int i = 0; i < N; i++) {
			out.append(op.operator(seq1.get(i), seq2.get(i)));
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
	public static <T> ISeq<T> conv(ISeq<T> seq1, ISeq<T> seq2) {
	    int N1 = seq1.size();
	    int N2 = seq2.size();
	    int N = N1+N2-1;
	    
	    ISeqBaseOperator<T> op = seq1.getSeqBaseOperator();
	    ISeq<T> out = op.newInstance();
	    
	    int n = 0;
	    int m = 0;
	    int n_m = 0;  
	    T tmp = op.zeroElement();
	    for(n = 0; n < N; n++)
	    {
	    		tmp = op.zeroElement();
	        for(m = 0; m < N1; m++)
	        {
	            n_m = n - m;
	            if( (n_m >= 0) && (n_m < N2) )
	            		tmp = op.add(tmp, op.multiple(seq1.get(m), seq2.get(n_m)));
	        }
	        out.append(tmp);
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
	public static <T> ComplexSeq cirConvUsingDFT(ISeq<T> seq1, ISeq<T> seq2, int N)
	{
	    ComplexSeq seq1DFT = seq1.fft(N);
	    ComplexSeq seq2DFT = seq2.fft(N);
	    ComplexSeq dft = (ComplexSeq) SeqUtil.multiple(seq1DFT, seq2DFT);
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
	public static <T> ComplexSeq convUsingDFT(ISeq<T> seq1, ISeq<T> seq2)
	{
		int N = seq1.size()+seq2.size()-1;
	    return (ComplexSeq) cirConvUsingDFT(seq1, seq2, N).changeSize(N);
	}
	
	/**
	 * 
	 * createZeroSeq:创建零序列
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param N 序列长度
	 * @param cl 序列的类型，可以实序列或复序列
	 * @return 序列
	 * @since JDK 1.6
	 */
	public static RealSeq createZeroRealSeq(int N) {
		return new RealSeq(N);		
	}
	
	public static ComplexSeq createZeroComplexSeq(int N) {
		return new ComplexSeq(N);		
	}
	
	/**
	 * 
	 * createSinSeq:创建正弦序列（指定数字角频率）
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param A 幅度
	 * @param w 数字角频率
	 * @param initphi 初始相位 
	 * @param N 序列长度
	 * @return 正弦序列
	 * @since JDK 1.6
	 */
	public static RealSeq createSinSeq(double A, double w, double initphi, int N) {
		RealSeq out = new RealSeq(N);
		for(int i = 0; i < N; i++) {
			out.set(i, A*Math.sin(w*i+initphi));
		}
		return out;
	}
	
	/**
	 * 
	 * createSinSeq:创建正弦序列（指定模拟线频率和采样频率）
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param A 幅度
	 * @param f 模拟线频率
	 * @param initphi 初始相位
	 * @param fs 采样频率
	 * @param N 序列长度
	 * @return
	 * @since JDK 1.6
	 */
	public static RealSeq createSinSeq(double A, double f, double initphi, double fs, int N)	{
		return createSinSeq(A, 2*Math.PI*f/fs, initphi, N);
	}
	
	/**
	 * 
	 * createEJWSeq:创建复指数序列e^(j*(w*n+initphi))
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param w 数字角频率
	 * @param initphi 初始相位
	 * @param N 序列长度
	 * @return 复指数序列
	 * @since JDK 1.6
	 */
	public static ComplexSeq createEJWSeq(double w, double initphi, int N) {
		ComplexSeq out = new ComplexSeq(N);
		for(int i = 0; i < N; i++) {
			out.set(i, new Complex(Math.cos(w*i+initphi), Math.sin(w*i+initphi)));
		}
		return out;
	}
	
	/**
	 * 
	 * linSpace:创建等间隔采样点构成的序列（包含指定的起点和终点）
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param begin 范围的起点
	 * @param end 范围的终点
	 * @param N 采样点数（包括起点和终点）
	 * @return 序列
	 * @since JDK 1.6
	 */
	public static RealSeq linSpace(double begin, double end, int N)
	{
	    RealSeq out = new RealSeq(N);
	    
	    double delta = (end - begin)/(N-1);

	    for(int i = 0; i < N-1; i++)
	    {
	        out.set(i, begin + delta*i);
	    }    
	    out.set(N-1, end);

	    return out;
	}
	
	/**
	 * 
	 * createRandomSeq:创建随机序列
	 * TODO(这里描述这个方法适用条件 – 可选)
	 * TODO(这里描述这个方法的执行流程 – 可选)
	 * TODO(这里描述这个方法的使用方法 – 可选)
	 * TODO(这里描述这个方法的注意事项 – 可选)
	 *
	 * @author bme
	 * @param N 序列长度
	 * @return 随机序列
	 * @since JDK 1.6
	 */
	public static RealSeq createRandomSeq(int N)
	{
	    RealSeq out = new RealSeq(N);

	    for(int i = 0; i < N; i++)
	    {
	        out.set(i, Math.random());
	    }    

	    return out;
	}

}
