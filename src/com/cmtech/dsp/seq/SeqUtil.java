package com.cmtech.dsp.seq;

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
		AbstractSeq<T> out = seq1.getSeqBaseOperator().newInstance();
		
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

}
