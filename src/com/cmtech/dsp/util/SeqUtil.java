package com.cmtech.dsp.util;

import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.ISeq;
import com.cmtech.dsp.seq.IElementBasicOperator;
import com.cmtech.dsp.seq.RealSeq;
import com.cmtech.dsp.seq.Seq;


public class SeqUtil {
	private SeqUtil() {
	}
	
	private interface IBiOperator<T> {
		T operator(T op1, T op2);
	}
	

	public static <T> ISeq<T> add(ISeq<T> seq1, ISeq<T> seq2) {
		return process(seq1, seq2, new IBiOperator<T>() {
			@Override
			public T operator(T op1, T op2) {
				return seq1.getBasicOperator().add(op1, op2);
			}
		});
	}
	

	public static <T> ISeq<T> subtract(ISeq<T> seq1, ISeq<T> seq2) {
		return process(seq1, seq2, new IBiOperator<T>() {
			@Override
			public T operator(T op1, T op2) {
				return seq1.getBasicOperator().subtract(op1, op2);
			}
		});
	}
	
	public static <T> ISeq<T> multiple(ISeq<T> seq1, ISeq<T> seq2) {
		return process(seq1, seq2, new IBiOperator<T>() {
			@Override
			public T operator(T op1, T op2) {
				return seq1.getBasicOperator().multiply(op1, op2);
			}
		});
	}
	
	public static <T> ISeq<T> divide(ISeq<T> seq1, ISeq<T> seq2) {
		return process(seq1, seq2, new IBiOperator<T>() {
			@Override
			public T operator(T op1, T op2) {
				return seq1.getBasicOperator().divide(op1, op2);
			}
		});
	}
	
	private static <T> ISeq<T> process(ISeq<T> seq1, ISeq<T> seq2, IBiOperator<T> op) {
		int N = Math.max(seq1.size(), seq2.size());
		seq1.reSize(N);
		seq2.reSize(N);
		Seq<T> out = seq1.getBasicOperator().nullSeq();
		
		for(int i = 0; i < N; i++) {
			out.append(op.operator(seq1.get(i), seq2.get(i)));
		}
		return out;	
	}
	
	public static <T> ISeq<T> conv(ISeq<T> seq1, ISeq<T> seq2) {
	    int N1 = seq1.size();
	    int N2 = seq2.size();
	    int N = N1+N2-1;
	    
	    IElementBasicOperator<T> op = seq1.getBasicOperator();
	    ISeq<T> out = op.nullSeq();
	    
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
	            		tmp = op.add(tmp, op.multiply(seq1.get(m), seq2.get(n_m)));
	        }
	        out.append(tmp);
	    }
	    return out;
	}
	
	public static <T> ComplexSeq cirConvUsingDFT(ISeq<T> seq1, ISeq<T> seq2, int N)
	{
	    ComplexSeq seq1DFT = seq1.fft(N);
	    ComplexSeq seq2DFT = seq2.fft(N);
	    ComplexSeq dft = (ComplexSeq) SeqUtil.multiple(seq1DFT, seq2DFT);
	    return dft.ifft();
	}

	public static <T> ComplexSeq convUsingDFT(ISeq<T> seq1, ISeq<T> seq2)
	{
		int N = seq1.size()+seq2.size()-1;
	    ComplexSeq out = (ComplexSeq) cirConvUsingDFT(seq1, seq2, N);
	    out.reSize(N);
	    return out;
	}
	
	public static RealSeq createZeroRealSeq(int N) {
		return new RealSeq(N);		
	}
	
	public static ComplexSeq createZeroComplexSeq(int N) {
		return new ComplexSeq(N);		
	}
	
	public static RealSeq createSinSeq(double A, double w, double initphi, int N) {
		RealSeq out = new RealSeq(N);
		for(int i = 0; i < N; i++) {
			out.set(i, A*Math.sin(w*i+initphi));
		}
		return out;
	}
	
	public static RealSeq createSinSeq(double A, double f, double initphi, double fs, int N)	{
		return createSinSeq(A, 2*Math.PI*f/fs, initphi, N);
	}
	
	public static ComplexSeq createEJWSeq(double w, double initphi, int N) {
		ComplexSeq out = new ComplexSeq(N);
		for(int i = 0; i < N; i++) {
			out.set(i, new Complex(Math.cos(w*i+initphi), Math.sin(w*i+initphi)));
		}
		return out;
	}
	
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
