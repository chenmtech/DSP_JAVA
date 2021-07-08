package com.cmtech.dsp.util;

import com.cmtech.dsp.seq.ComplexSeq;
import com.cmtech.dsp.seq.ISeq;
import com.cmtech.dsp.seq.IElementBasicOperator;
import com.cmtech.dsp.seq.RealSeq;
import com.cmtech.dsp.seq.Seq;


public class SeqUtil {
	private SeqUtil() {
	}
	
	private static interface IBiOperator<T> {
		T operator(T op1, T op2);
	}	

	/**
	 * Add two sequences
	 * @param seq1 sequence 1
	 * @param seq2 sequence 1
	 * @return sum of the two sequences
	 */
	public static <T> ISeq<T> add(ISeq<T> seq1, ISeq<T> seq2) {
		return process(seq1, seq2, new IBiOperator<T>() {
			@Override
			public T operator(T op1, T op2) {
				return seq1.getBasicOperator().add(op1, op2);
			}
		});
	}
	
	/**
	 * Subtract two sequences
	 * @param seq1 sequence 1
	 * @param seq2 sequence 1
	 * @return diff of the two sequences
	 */
	public static <T> ISeq<T> subtract(ISeq<T> seq1, ISeq<T> seq2) {
		return process(seq1, seq2, new IBiOperator<T>() {
			@Override
			public T operator(T op1, T op2) {
				return seq1.getBasicOperator().subtract(op1, op2);
			}
		});
	}
	
	/**
	 * Multiply two sequences
	 * @param seq1 sequence 1
	 * @param seq2 sequence 1
	 * @return product of the two sequences
	 */
	public static <T> ISeq<T> multiple(ISeq<T> seq1, ISeq<T> seq2) {
		return process(seq1, seq2, new IBiOperator<T>() {
			@Override
			public T operator(T op1, T op2) {
				return seq1.getBasicOperator().multiply(op1, op2);
			}
		});
	}
	
	/**
	 * Divide two sequences
	 * @param seq1 sequence 1
	 * @param seq2 sequence 1
	 * @return quotient of the two sequences
	 */
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
	
	/**
	 * Linear convolution two sequences done in the time domain
	 * @param seq1 sequence 1
	 * @param seq2 sequence 1
	 * @return convolution of the two sequences
	 */
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
	
	/**
	 * Linear convolution two sequences using DFT
	 * @param seq1 sequence 1
	 * @param seq2 sequence 1
	 * @return convolution of the two sequences
	 */
	public static <T> ComplexSeq convUsingDFT(ISeq<T> seq1, ISeq<T> seq2)
	{
		int N = seq1.size()+seq2.size()-1;
	    ComplexSeq out = (ComplexSeq) cirConvUsingDFT(seq1, seq2, N);
	    out.reSize(N);
	    return out;
	}
	
	/**
	 * Circular convolution two sequences using DFT
	 * @param seq1 sequence 1
	 * @param seq2 sequence 1
	 * @param N the length of circular convolution
	 * @return circular convolution of the two sequences
	 */
	public static <T> ComplexSeq cirConvUsingDFT(ISeq<T> seq1, ISeq<T> seq2, int N)
	{
	    ComplexSeq seq1DFT = seq1.fft(N);
	    ComplexSeq seq2DFT = seq2.fft(N);
	    ComplexSeq dft = (ComplexSeq) SeqUtil.multiple(seq1DFT, seq2DFT);
	    return dft.ifft();
	}
	
	/**
	 * create a new RealSeq with the length N
	 * @param N length of the RealSeq
	 * @return real seq
	 */
	public static RealSeq newRealSeq(int N) {
		return new RealSeq(N);		
	}
	
	/**
	 * create a new ComplexSeq with the length N
	 * @param N length of the ComplexSeq
	 * @return complex seq
	 */
	public static ComplexSeq newComplexSeq(int N) {
		return new ComplexSeq(N);		
	}
	
	/**
	 * create a sin sequence with amplitude A, digital frequency w, initial phase initphi, and length N
	 * @param A amplitude 
	 * @param w digital frequency
	 * @param initphi initial phase
	 * @param N length
	 * @return sin sequence
	 */
	public static RealSeq sinSeq(double A, double w, double initphi, int N) {
		RealSeq out = new RealSeq(N);
		for(int i = 0; i < N; i++) {
			out.set(i, A*Math.sin(w*i+initphi));
		}
		return out;
	}
	
	/**
	 * create a sin sequence with amplitude A, analog frequency f, initial phase initphi, sampling frequency fs, and length N
	 * @param A amplitude
	 * @param f analog frequency
	 * @param initphi initial phase
	 * @param fs sampling frequency
	 * @param N length
	 * @return sin sequence
	 */
	public static RealSeq sinSeq(double A, double f, double initphi, double fs, int N)	{
		return sinSeq(A, 2*Math.PI*f/fs, initphi, N);
	}
	
	/**
	 * create a complex sequence e^(i*w*n+initphi), i.e. cos(w*n+initphi) + i * sin(w*n+initphi)
	 * @param w digital frequency
	 * @param initphi initial phase
	 * @param N length
	 * @return e^(i*w*n+initphi)
	 */
	public static ComplexSeq eJWSeq(double w, double initphi, int N) {
		ComplexSeq out = new ComplexSeq(N);
		for(int i = 0; i < N; i++) {
			out.set(i, new Complex(Math.cos(w*i+initphi), Math.sin(w*i+initphi)));
		}
		return out;
	}
	
	/**
	 * create a sequence which element values are (N-1) equally segment values between [begin, end], including the values of begin and end 
	 * @param begin first value
	 * @param end last value
	 * @param N total value number
	 * @return seq
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
	 * create a random RealSeq
	 * @param N length
	 * @return random sequence
	 */
	public static RealSeq randomSeq(int N)
	{
	    RealSeq out = new RealSeq(N);
	    for(int i = 0; i < N; i++)
	    {
	        out.set(i, Math.random());
	    }    
	    return out;
	}

}
